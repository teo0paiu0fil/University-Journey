#include "utils.h"

int recv_all(int sockfd, void *buffer, size_t len)
{

	size_t bytes_received = 0;
	size_t bytes_remaining = len;
	char *buff = buffer;

	while (bytes_remaining)
	{
		int rc = recv(sockfd, buff + bytes_received, bytes_remaining, 0);
		DIE(rc <= -1, "naspa");

		bytes_received += rc;
		bytes_remaining -= rc;

		if (rc == 0)
			break;
	}

	return bytes_received;
}

int send_all(int sockfd, void *buffer, size_t len)
{
	size_t bytes_sent = 0;
	size_t bytes_remaining = len;
	char *buff = buffer;

	while (bytes_remaining)
	{
		int rc = send(sockfd, buff + bytes_sent, bytes_remaining, 0);
		DIE(rc <= -1, "naspa");

		bytes_sent += rc;
		bytes_remaining -= rc;

		if (rc == 0)
			break;
	}

	return bytes_sent;
}

int create_tcp_socket(uint16_t port, uint64_t addr)
{
	int sockfd;
	struct sockaddr_in servaddr;

	sockfd = socket(AF_INET, SOCK_STREAM, 0);
	DIE(sockfd < 0, "Socket invalid");

	memset(&servaddr, 0, sizeof(struct sockaddr_in));
	servaddr.sin_family = AF_INET;
	servaddr.sin_addr.s_addr = addr;
	servaddr.sin_port = htons(port);

	int rc = connect(sockfd, (struct sockaddr *)&servaddr, sizeof(servaddr));
	DIE(rc < 0, "connect");

	return sockfd;
}

void start_client(int fd, char *id)
{
	int rc;
	struct pollfd poll_fds[2];
	char buf[100];

	MsgTCP msg_tcp = calloc(1, sizeof(struct msg_tcp));
	msg_tcp->command = 0; // conection request

	strcpy(msg_tcp->id, id);
	strcpy(msg_tcp->content, "Conection Request");

	send_all(fd, msg_tcp, sizeof(struct msg_tcp));

	int value = 1;
	rc = setsockopt(fd, IPPROTO_TCP, TCP_NODELAY, (char *)&value, sizeof(int)); // dezactivating the Nagle alg
	DIE(rc < 0, "Fail Neagle");

	poll_fds[0].fd = fd;		 // adding the udp port
	poll_fds[0].events = POLLIN; // and waiting for conections

	poll_fds[1].fd = STDIN_FILENO; // checking the standart input
	poll_fds[1].events = POLLIN;   // and waiting for conections

	while (1)
	{
		rc = poll(poll_fds, 2, -1);
		DIE(rc < 0, "poll");

		if ((poll_fds[0].revents & POLLIN) != 0) // we are ready to recv msg
		{
			memset(msg_tcp, 0, sizeof(struct msg_tcp));
			rc = recv_all(poll_fds[0].fd, msg_tcp, sizeof(struct msg_tcp));

			// printing the message or exiting;
			if (msg_tcp->command == 2 || rc == 0) {
				break;
			}
			else
			{
				printf("%s\n", msg_tcp->content);
			}
		}
		else if ((poll_fds[1].revents & POLLIN) != 0) // we are ready to recv comand from std input
		{
			memset(msg_tcp, 0, sizeof(struct msg_tcp));
			memset(buf, 0, sizeof(buf));
			fgets(buf, sizeof(buf), stdin);

			char *token;
			token = strtok(buf, " \n");
			if (token == NULL)
				continue;

			if (strcmp(token, "subscribe") == 0)
			{
				token = strtok(NULL, " \n");
				if (token == NULL)
					continue;

				strcpy(msg_tcp->content, token);
				msg_tcp->command = 1;
				token = strtok(NULL, " \n");
				if (token == NULL)
					continue;
				msg_tcp->sf = atoi(token);
				strcpy(msg_tcp->id, id);

				send_all(fd, msg_tcp, sizeof(struct msg_tcp));
				printf("Subscribed to topic.\n");
			}
			else if (strcmp(token, "unsubscribe") == 0)
			{
				token = strtok(NULL, " \n");
				if (token == NULL)
					continue;
				strcpy(msg_tcp->content, token);
				msg_tcp->command = 0;
				strcpy(msg_tcp->id, id);

				send_all(fd, msg_tcp, sizeof(struct msg_tcp));
				printf("Unsubscribed from topic.\n");
			}
			else if (strcmp(buf, "exit") == 0 || strcmp(buf, "exit\n") == 0)
			{
				break;
			}
		}
	}
	free(msg_tcp);
}

int main(int argc, char *argv[])
{
	if (argc != 4)
	{
		printf("\n Usage: %s <id_client> <ip_server> <port_server>\n", argv[0]);
		return 1;
	}

	setvbuf(stdout, NULL, _IONBF, BUFSIZ); // disable buffering

	uint16_t port;
	int rc = sscanf(argv[3], "%hu", &port); // parse port to number
	DIE(rc != 1, "Given port is invalid");

	uint64_t addr;
	rc = inet_pton(AF_INET, argv[2], &addr); // parse ip
	DIE(rc <= 0, "inet_pton");

	char *client_id = argv[1];

	int sockfd = create_tcp_socket(port, addr);

	start_client(sockfd, client_id);

	close(sockfd);

	return 0;
}