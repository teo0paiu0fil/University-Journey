#include <stdlib.h>     /* exit, atoi, malloc, free */
#include <stdio.h>
#include <unistd.h>     /* read, write, close */
#include <string.h>     /* memcpy, memset */
#include <sys/socket.h> /* socket, connect */
#include <netinet/in.h> /* struct sockaddr_in, struct sockaddr */
#include <netdb.h>      /* struct hostent, gethostbyname */
#include <arpa/inet.h>
#include "helpers.h"
#include "requests.h"

char *compute_get_request(char *host, char *url, char *cookies, char *auth)
{
    char *message = calloc(BUFLEN, sizeof(char));
    char *line = calloc(LINELEN, sizeof(char));

    // write the method name, URL, and protocol type
    sprintf(line, "GET %s HTTP/1.1", url);
    compute_message(message, line);

    // add the host
    sprintf(line, "Host: %s", host);
    compute_message(message, line);

    // if needed put the authorization token
    if (auth != NULL) {
        sprintf(line, "Authorization: Bearer %s", auth);
        compute_message(message, line);
    }

    // if needed add cookie
    if (cookies != NULL) {
        sprintf(line, "Cookie: %s", cookies);
        compute_message(message, line);
    }

    // add final new line
    compute_message(message, "");
    return message;
}

char *compute_post_request(char *host, char *url, char* content_type, char *body_data, char *cookies, char *auth)
{
    char *message = calloc(BUFLEN, sizeof(char));
    char *line = calloc(LINELEN, sizeof(char));

    // write the method name, URL and protocol type
    sprintf(line, "POST %s HTTP/1.1", url);
    compute_message(message, line);
    
    //  add the host
    sprintf(line, "Host: %s", host);
    compute_message(message, line);

    // if needed put the authorization token
    if (auth != NULL) {
        sprintf(line, "Authorization: Bearer %s", auth);
        compute_message(message, line);
    }

    // add necessary headers Content-Type and Content-Length
    sprintf(line, "Content-Type: %s", content_type);
    compute_message(message, line);

    sprintf(line, "Content-Length: %ld", strlen(body_data) + 1);
    compute_message(message, line);
    
    // if needed add cookies
    if (cookies != NULL) {
        sprintf(line, "Cookie: %s", cookies);
        compute_message(message, line);
    }

    // add new line at end of header
    compute_message(message, "");

    // add the actual payload data
    compute_message(message, body_data);

    free(line);
    return message;
}

char *compute_delete_request(char *host, char *url, char *cookies, char *auth) 
{
    char *message = calloc(BUFLEN, sizeof(char));
    char *line = calloc(LINELEN, sizeof(char));

    sprintf(line, "DELETE %s HTTP/1.1", url);
    compute_message(message, line);
    
    sprintf(line, "Host: %s", host);
    compute_message(message, line);

    if (auth != NULL) {
        sprintf(line, "Authorization: Bearer %s", auth);
        compute_message(message, line);
    }

    if (cookies != NULL) {
        sprintf(line, "Cookie: %s", cookies);
        compute_message(message, line);
    }
    
    compute_message(message, "");

    free(line);
    return message;
}