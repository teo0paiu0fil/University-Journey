#include "utils.h"
#include "math.h"

static struct topic *topics;
static int number_of_topics = 0;

static Client *clients;
static int nrREGclients = 0;

list cons(void *element, list l)
{
    list temp = malloc(sizeof(struct cell));
    temp->element = element;
    temp->next = l;
    return temp;
}

list cdr_and_free(list l)
{
    list temp = l->next;
    free(l);
    return temp;
}

queue queue_create(void)
{
    queue q = malloc(sizeof(struct queue));
    q->head = q->tail = NULL;
    return q;
}

int queue_empty(queue q)
{
    return q->head == NULL;
}

void queue_enq(queue q, void *element)
{
    if (queue_empty(q))
    {
        q->head = q->tail = cons(element, NULL);
    }
    else
    {
        q->tail->next = cons(element, NULL);
        q->tail = q->tail->next;
    }
}

void *queue_deq(queue q)
{
    if (!queue_empty(q))
    {
        void *temp = q->head->element;
        q->head = cdr_and_free(q->head);
        return temp;
    }
    else
    {
        return NULL;
    }
}

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

int create_udp_socket(uint16_t port)
{
    int sockfd;
    struct sockaddr_in servaddr;

    sockfd = socket(AF_INET, SOCK_DGRAM, 0);
    DIE(sockfd < 0, "Socket invalid");

    memset(&servaddr, 0, sizeof(struct sockaddr_in));
    servaddr.sin_family = AF_INET;
    servaddr.sin_addr.s_addr = INADDR_ANY;
    servaddr.sin_port = htons(port);

    int rc = bind(sockfd, (struct sockaddr *)&servaddr, sizeof(servaddr));
    DIE(rc < 0, "Bind error");

    return sockfd;
}

int create_tcp_socket(uint16_t port)
{
    int sockfd;
    struct sockaddr_in servaddr;

    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    DIE(sockfd < 0, "Socket invalid");

    memset(&servaddr, 0, sizeof(struct sockaddr_in));
    servaddr.sin_family = AF_INET;
    servaddr.sin_addr.s_addr = INADDR_ANY;
    servaddr.sin_port = htons(port);

    int rc = bind(sockfd, (struct sockaddr *)&servaddr, sizeof(servaddr));
    DIE(rc < 0, "Bind error");

    return sockfd;
}

char *parse_message(MsgUDP msg)
{
    char *ms = calloc(2000, sizeof(char));

    switch (msg->type)
    {
    case 0:
    {
        sprintf(ms, "%s:%hu - %s - INT - %d", msg->ip, msg->port, msg->topic, msg->content[0] == 1 ? (ntohl(*(int *)(msg->content + 1))) * (-1) : ntohl(*(int *)(msg->content + 1)));
    }
    break;
    case 1:
    {
        sprintf(ms, "%s:%hu - %s - SHORT_REAL - %.2f", msg->ip, msg->port, msg->topic, (1.0 * ntohs((*(int *)(msg->content)))) / 100);
    }
    break;
    case 2:
    {
        int sign = msg->content[0];
        float nr = ntohl(*(int *)(msg->content + 1));
        uint8_t exp = msg->content[5];
        nr /= pow(10, exp);
        sprintf(ms, "%s:%hu - %s - FLOAT - %.4f", msg->ip, msg->port, msg->topic, sign == 0 ? nr : -1 * nr);
    }
    break;
    case 3:
    {
        sprintf(ms, "%s:%hu - %s - STRING - %s", msg->ip, msg->port, msg->topic, msg->content);
    }
    break;
    default:
        free(ms);
        return NULL;
    }
    return ms;
}

void unsubscribe_from_topic(char *topic, char *id) {
    for (int i = 0; i < number_of_topics; i++)
    {
        if (strcmp(topic, topics[i].topic) == 0) {
            list p = topics[i].clients, k;

            for(;p != NULL; p = p->next) {
                if(strcmp(id, ((Info)p->element)->client->id) == 0) {
                    k->next = p->next;
                    free(p);
                    break;
                }
                k = p;
            }
            
            return;
        }
    }
}

void subscribe_to_topic(char *topic, char *id, int sf) {
    Client a = NULL;

    for (int i = 0; i < nrREGclients; i++)
    {
        if(strcmp(clients[i]->id, id) == 0) {
            a = clients[i];
        }
    }

    DIE(!a, "no client found");

    Info inf = calloc(1, sizeof(Info));
   
    inf->client = a;
    inf->sf = sf;

    for (int i = 0; i < number_of_topics; i++)
    {
        if (strcmp(topic, topics[i].topic) == 0) {
            topics[i].clients = cons((void *)inf, topics[i].clients);
            return;
        }
    }

    struct topic *top = calloc(1, sizeof(struct topic));
    top->clients = calloc(1, sizeof(struct cell));

    strcpy(top->topic, topic);
    top->clients->element = inf;

    topics[number_of_topics++] = *top;
}

void handle_command(MsgTCP msg)
{
    switch (msg->command)
    {
    case 0:
        unsubscribe_from_topic(msg->content, msg->id);
        break;
    case 1:
        subscribe_to_topic(msg->content, msg->id, msg->sf);
        break;
    default:
        break;
    }
}

void send_topic_to_subscribers(MsgUDP msg) {
    char* message = parse_message(msg);
    MsgTCP msg_tcp = calloc(1, sizeof(struct msg_tcp));
    strcpy(msg_tcp->content, message);

    for (int i = 0; i < number_of_topics; i++)
    {
        if (strcmp(msg->topic, topics[i].topic) == 0) {
            list p = topics[i].clients;
            
            for(; p != NULL; p = p->next) {
                Client a = ((Info)p->element)->client;
                if (a->curr_fd != -1) {
                    send_all(a->curr_fd, msg_tcp, sizeof(*msg_tcp));
                } else if (((Info)p->element)->sf == 1) {
                    if (queue_empty(a->msg))
                        a->msg = queue_create();
                    queue_enq(a->msg, msg_tcp);  
                }
            }

            break;
        }
    }

    free(message);
}

void start_server(int socket_udp, int socket_tcp)
{
    int n = 1, m = 1, number_of_clients = 3, rc;
    struct pollfd *poll_fds = calloc(MAX_CLIENTS, sizeof(struct pollfd)); // the initial number of clients is 5

    char buffer[1600];
    MsgTCP msg_tcp = calloc(1, sizeof(struct msg_tcp));
    char buf_command[20];

    poll_fds[0].fd = socket_udp; // adding the udp port
    poll_fds[0].events = POLLIN; // and waiting for conections

    poll_fds[1].fd = socket_tcp; // adding the tcp port
    poll_fds[1].events = POLLIN; // and waiting for conections

    int value = 1;
    rc = setsockopt(socket_tcp, IPPROTO_TCP, TCP_NODELAY, (char *)&value, sizeof(int));
    DIE(rc < 0, "Fail Neagle");

    poll_fds[2].fd = STDIN_FILENO; // checking the standart input
    poll_fds[2].events = POLLIN;   // and waiting for conections

    while (1)
    {
        rc = poll(poll_fds, number_of_clients, -1); // we are waiting for an event on one of the monitored fd
        DIE(rc < 0, "poll error");
        memset(buffer, 0, 1600);

        for (int i = 0; i < number_of_clients; i++)
        {
            if ((poll_fds[i].revents & POLLIN) != 0)
            {
                struct sockaddr_in from_udp_client;
                socklen_t sock_len = sizeof(struct sockaddr_in);
                
                if (i == 0)
                { // check for udp clients                    
                    rc = recvfrom(poll_fds[i].fd, buffer, sizeof(buffer), 0, (struct sockaddr *)&from_udp_client, &sock_len); // recv message
                    DIE(rc < 0, "recv");

                    MsgUDP msg_udp = calloc(1, sizeof(struct msg_udp));

                    memcpy(msg_udp->topic, buffer, 50);
                    memcpy(&msg_udp->type, buffer + 50, 1);
                    memcpy(msg_udp->content, buffer + 50 + 1, 1501);
                    msg_udp->port = ntohs(from_udp_client.sin_port);
                    strcpy(msg_udp->ip, inet_ntoa(from_udp_client.sin_addr));

                    send_topic_to_subscribers(msg_udp);
                }
                else if (i == 1)
                {
                    int newsockfd = accept(poll_fds[i].fd, (struct sockaddr *)&from_udp_client, &sock_len);
                    DIE(newsockfd < 0, "accept");

                    int value = 1;
                    rc = setsockopt(newsockfd, IPPROTO_TCP, TCP_NODELAY, (char *)&value, sizeof(int));
                    DIE(rc < 0, "Fail Neagle");

                    rc = recv_all(newsockfd, msg_tcp, sizeof(struct msg_tcp));
                    DIE(rc < 0, "recv");

                    Client a = calloc(1, sizeof(struct client));

                    a->curr_fd = newsockfd;
                    a->port = ntohs(from_udp_client.sin_port);
                    a->msg = queue_create();
                    strcpy(a->ip, inet_ntoa(from_udp_client.sin_addr));
                    strcpy(a->id, msg_tcp->id);

                    int check = 1;

                    for (int j = 0; j < nrREGclients; j++) {
                        if (strcmp(a->id, clients[j]->id) == 0 && clients[j]->curr_fd != -1) {
                            printf("Client %s already connected.\n", a->id);
                            close(newsockfd);
                            check = 0;
                            break;

                        } else if (strcmp(a->id, clients[j]->id) == 0) {
                            clients[j]->curr_fd = newsockfd;

                            while (!queue_empty(clients[j]->msg)) {
                                MsgTCP msg = (MsgTCP)queue_deq(clients[j]->msg);
                                send_all(clients[j]->curr_fd, msg, sizeof(*msg));
                            }

                            poll_fds[number_of_clients].fd = newsockfd;
                            poll_fds[number_of_clients].events = POLLIN;

                            number_of_clients++;

                            if (number_of_clients == MAX_CLIENTS * n)
                            {
                                n++;
                                poll_fds = realloc(poll_fds, (MAX_CLIENTS * n) * (sizeof(struct pollfd)));
                            }

                            printf("New client %s connected from %s:%d.\n", a->id, a->ip, a->port);
                            check= 0;
                            break;
                        }
                    }

                    if (check == 0) {
                        break;
                    }

                    poll_fds[number_of_clients].fd = newsockfd;
                    poll_fds[number_of_clients].events = POLLIN;

                    number_of_clients++;

                    if (number_of_clients == MAX_CLIENTS * n)
                    {
                        n++;
                        poll_fds = realloc(poll_fds, (MAX_CLIENTS * n) * (sizeof(struct pollfd)));
                    }

                    clients[nrREGclients] = a;
                    nrREGclients++;

                    if (nrREGclients == MAX_CLIENTS * m) {
                        m++;
                        clients = realloc(clients, (MAX_CLIENTS * m) * (sizeof(struct client)));
                    }

                    printf("New client %s connected from %s:%d.\n", a->id, a->ip, a->port);
                }
                else if (i == 2)
                { // reciev a command from stdin -> maybe close the program and clients
                    fgets(buf_command, sizeof(buf_command), stdin);

                    if (strcmp("exit\n", buf_command) == 0)
                    { 
                        for (int i = 3; i < number_of_clients; i++)
                        {
                            close(poll_fds[i].fd);
                        }
                        return;
                    }
                }
                else
                { // reciev something from an tcp client
                    rc = recv_all(poll_fds[i].fd, msg_tcp, sizeof(struct msg_tcp));
                    DIE(rc < 0, "recv");

                    if (rc == 0)
                    {
                        for (int j = 0; j < nrREGclients; j++) {
                            if (poll_fds[i].fd == clients[j]->curr_fd) {
                                printf("Client %s disconnected.\n", clients[j]->id);
                                clients[j]->curr_fd = -1;
                                break;
                            }
                        }

                        close(poll_fds[i].fd);

                        for (int j = i; j < number_of_clients - 1; j++)
                        {
                            poll_fds[j] = poll_fds[j + 1];
                        }

                        number_of_clients--;
                    }
                    else
                    {
                        handle_command(msg_tcp);
                    }
                }
            }
        }
    }
    
    free(poll_fds);
    free(msg_tcp);
}

int main(int argc, char *argv[])
{
    if (argc != 2)
    {
        printf("\n Usage: %s <port>\n", argv[0]);
        return 1;
    }

    setvbuf(stdout, NULL, _IONBF, BUFSIZ); // disable buffering

    topics = calloc(100, sizeof(struct topic));
    clients = calloc(MAX_CLIENTS, sizeof(Client));

    uint16_t port; // parse the port
    int rc = sscanf(argv[1], "%hu", &port);
    DIE(rc != 1, "Given port is invalid");

    int sockfd_tcp = create_tcp_socket(port); // socket tcp (subscribers)
    int sockfd_udp = create_udp_socket(port); // socket udp

    rc = listen(sockfd_tcp, MAX_CONECTIONS); // i listen for the tcp clients
    DIE(rc < 0, "listen");

    start_server(sockfd_udp, sockfd_tcp);

    close(sockfd_tcp);
    close(sockfd_udp);

    free(clients);
    free(topics);

    return 0;
}