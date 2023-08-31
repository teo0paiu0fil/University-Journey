#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include <sys/types.h>
#include <unistd.h>
#include <sys/stat.h>
#include <sys/epoll.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <sys/sendfile.h>
#include <arpa/inet.h>
#include <fcntl.h>

#include "aws.h"
#include "debug.h"
#include "util.h"
#include "sock_util.h"
#include "w_epoll.h"
#include "http_parser.h"

enum connection_state {
	STATE_DATA_RECEIVED,
	STATE_DATA_SENT,
	STATE_CONNECTION_CLOSED
};

struct connection {
    /* socketi deschisi pentru comunicarea cu clientul si fisierul cerut de acesta*/
	int sockfd;
    int fd;

	/* buffers used for receiving messages and then echoing them back */
	char recv_full_message[BUFSIZ];
	size_t recv_full_len;
	char recv_buffer[BUFSIZ];
	size_t recv_len;
	char send_buffer[BUFSIZ];
	size_t send_len;
	enum connection_state state;
};

static int on_path_cb(http_parser *p, const char *buf, size_t len);
