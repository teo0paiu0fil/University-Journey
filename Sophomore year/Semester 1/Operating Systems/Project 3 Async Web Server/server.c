#include "headers.h"

static int _listen_fd;
static int _epoll_fd;

static char pathname[BUFSIZ];

static http_parser _http_parser;
static http_parser_settings _http_settings = {
	/* on_message_begin */ 0,
	/* on_header_field */ 0,
	/* on_header_value */ 0,
	/* on_path */ on_path_cb,
	/* on_url */ 0,
	/* on_fragment */ 0,
	/* on_query_string */ 0,
	/* on_body */ 0,
	/* on_headers_complete */ 0,
	/* on_message_complete */ 0
};

static int on_path_cb(http_parser *p, const char *buf, size_t len)
{
	assert(p == &_http_parser);
	memset(pathname, 0, BUFSIZ);
	strcpy(pathname, AWS_DOCUMENT_ROOT);
	strncat(pathname, buf, len);
	// strcat(pathname, "dat");

	return 0;
}

static struct connection *connection_create(int sockfd)
{
	struct connection *conn = calloc(1, sizeof(*conn));

	DIE(conn == NULL, "calloc");

	conn->sockfd = sockfd;
	memset(conn->recv_buffer, 0, BUFSIZ);
	memset(conn->send_buffer, 0, BUFSIZ);

	return conn;
}

static void connection_remove(struct connection *conn)
{
	close(conn->sockfd);
	close(conn->fd);
	conn->state = STATE_CONNECTION_CLOSED;
	free(conn);
}

static enum connection_state receive_message(struct connection *conn)
{
	ssize_t bytes_recv;
	int rc;
	char abuffer[64];

	rc = get_peer_address(conn->sockfd, abuffer, 64);
	if (rc < 0) {
		ERR("get_peer_address");
		goto remove_connection;
	}

	bytes_recv = recv(conn->sockfd, conn->recv_buffer, BUFSIZ, 0);
	if (bytes_recv < 0) {		/* error in communication */
		dlog(LOG_ERR, "Error in communication from: %s\n", abuffer);
		goto remove_connection;
	}
	if (bytes_recv == 0) {		/* connection closed */
		dlog(LOG_INFO, "Connection closed from: %s\n", abuffer);
		goto remove_connection;
	}

	dlog(LOG_DEBUG, "Received message from: %s\n", abuffer);

	printf("--\n%s--\n", conn->recv_buffer);

	conn->recv_len = bytes_recv;
	conn->state = STATE_DATA_RECEIVED;

	return STATE_DATA_RECEIVED;

remove_connection:
	rc = w_epoll_remove_ptr(_epoll_fd, conn->sockfd, conn);
	DIE(rc < 0, "w_epoll_remove_ptr");

	/* remove current connection */
	connection_remove(conn);

	return STATE_CONNECTION_CLOSED;
}

static void handle_new_connection(void) // functie inspirata din sample
{
	static int sockfd;
	socklen_t addrlen = sizeof(struct sockaddr_in);
	struct sockaddr_in addr;
	struct connection *conn;
	int rc;

	/* accept new connection */
	sockfd = accept(_listen_fd, (SSA *) &addr, &addrlen);
	DIE(sockfd < 0, "accept");

	dlog(LOG_ERR, "Accepted connection from: %s:%d\n",
		inet_ntoa(addr.sin_addr), ntohs(addr.sin_port));

	/* instantiate new connection handler */
	conn = connection_create(sockfd);

	/* add socket to epoll */
	rc = w_epoll_add_ptr_in(_epoll_fd, sockfd, conn);
	DIE(rc < 0, "w_epoll_add_in");
}

void handle_http_parser(struct connection *conn) {

	http_parser_init(&_http_parser, HTTP_REQUEST);

	http_parser_execute(&_http_parser, &_http_settings, conn->recv_buffer, conn->recv_len);
	dlog(LOG_DEBUG, "calea fisierului : %s\n", pathname);
	conn->fd = open(pathname, O_RDONLY);
	if (conn->fd == -1) {
		strcpy(conn->send_buffer, "HTTP/1.0 404 Not Found\r\n\r\n");
		conn->send_len = strlen("HTTP/1.0 404 Not Found\r\n\r\n");
	} else {
		strcpy(conn->send_buffer, "HTTP/1.0 200 OK\r\n\r\n");
		conn->send_len = strlen("HTTP/1.0 200 OK\r\n\r\n");
	}
}

static void handle_client_request(struct connection *conn)
{
	int rc;
	enum connection_state ret_state;

	ret_state = receive_message(conn);
	if (ret_state == STATE_CONNECTION_CLOSED)
		return;

	dlog(LOG_INFO, "message primit: %s\n", conn->recv_buffer);
	handle_http_parser(conn);

	/* add socket to epoll for out events */
	rc = w_epoll_update_ptr_inout(_epoll_fd, conn->sockfd, conn);
	DIE(rc < 0, "w_epoll_add_ptr_inout");
}

void send_static(struct connection *conn) {
	struct stat _fstat;
	int rc = fstat(conn->fd, &_fstat);
	DIE(rc == -1, "fstat error");

	rc = 1;
	while (rc > 0) {
		rc = sendfile(conn->sockfd, conn->fd, 0, _fstat.st_size);
		DIE(rc == -1, "sendfile error");
	}
}

void send_dynamic(struct connection *conn) {
	dlog(LOG_CRIT, "need to be implemented\n");
	DIE(1 == 1, "nasol");
}

static enum connection_state send_message(struct connection *conn)
{
	ssize_t bytes_sent;
	int rc;
	char abuffer[64];

	rc = get_peer_address(conn->sockfd, abuffer, 64);
	if (rc < 0) {
		ERR("get_peer_address");
		goto remove_connection;
	}

	bytes_sent = send(conn->sockfd, conn->send_buffer, conn->send_len, 0);
	if (bytes_sent < 0) {		/* error in communication */
		dlog(LOG_ERR, "Error in communication to %s\n", abuffer);
		goto remove_connection;
	}
	if (bytes_sent == 0) {		/* connection closed */
		dlog(LOG_INFO, "Connection closed to %s\n", abuffer);
		goto remove_connection;
	}
	if (conn->fd == -1) {
		dlog(LOG_ERR, "File not found\n");
		goto remove_connection;
	}

	dlog(LOG_DEBUG, "Sending message to %s\n", abuffer);

	if (strstr(pathname, AWS_REL_STATIC_FOLDER) != NULL) {
		send_static(conn);
		goto remove_connection;
	} else 
		send_dynamic(conn);
	
	dlog(LOG_DEBUG, "message sent %s\n", abuffer);

	/* all done - remove out notification */
	rc = w_epoll_update_ptr_in(_epoll_fd, conn->sockfd, conn);
	DIE(rc < 0, "w_epoll_update_ptr_in");

	conn->state = STATE_DATA_SENT;

	return STATE_DATA_SENT;

remove_connection:
	rc = w_epoll_remove_ptr(_epoll_fd, conn->sockfd, conn);
	DIE(rc < 0, "w_epoll_remove_ptr");

	dlog(LOG_INFO, "Connection closed from: %s\n", abuffer);
	/* remove current connection */
	connection_remove(conn);

	return STATE_CONNECTION_CLOSED;
}

int main(void)
{

	int res = 0; // variabila pentru verificarea codurilor returnate de functile apelate

	/* creez socket-ul pentru server */
	_listen_fd = tcp_create_listener(AWS_LISTEN_PORT, DEFAULT_LISTEN_BACKLOG);
	DIE(_listen_fd < 0, "tcp_create_listener");

	/* marchez socket-ul ca find nonblocant conform precizarilor din cerinta*/
	// int flags = fcntl(_listen_fd, F_GETFL);
	// res = fcntl(_listen_fd, F_SETFL, flags | O_NONBLOCK);
	// DIE(res == -1, "error settings flag");

	/* initializez epoll-ul */
	_epoll_fd = w_epoll_create();
	DIE(_epoll_fd < 0, "w_epoll_create");

	res = w_epoll_add_fd_in(_epoll_fd, _listen_fd);
	DIE(res < 0, "w_epoll_add_fd_in");

	dlog(LOG_INFO, "Server waiting for connections on port %d\n", AWS_LISTEN_PORT);

	while (1)
	{
		struct epoll_event rev;

		res = w_epoll_wait_infinite(_epoll_fd, &rev);
		DIE(res < 0, "w_epoll_wait_infinite");

		if (rev.data.fd == _listen_fd)
		{
			dlog(LOG_DEBUG, "New connection\n");
			if (rev.events & EPOLLIN)
				handle_new_connection();
		}
		else
		{
			if (rev.events & EPOLLIN)
			{
				dlog(LOG_DEBUG, "New message\n");
				handle_client_request(rev.data.ptr);
			}
			if (rev.events & EPOLLOUT)
			{
				dlog(LOG_DEBUG, "Ready to send message\n");
				send_message(rev.data.ptr);
			}
		}
	}

	return 0;
}