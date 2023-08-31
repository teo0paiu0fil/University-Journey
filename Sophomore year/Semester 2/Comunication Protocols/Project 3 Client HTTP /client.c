#include <stdio.h>      /* printf, sprintf */
#include <stdlib.h>     /* exit, atoi, malloc, free */
#include <unistd.h>     /* read, write, close */
#include <string.h>     /* memcpy, memset */
#include <sys/socket.h> /* socket, connect */
#include <netinet/in.h> /* struct sockaddr_in, struct sockaddr */
#include <netdb.h>      /* struct hostent, gethostbyname */
#include <arpa/inet.h>
#include <poll.h>
#include "helpers.h"
#include "requests.h"

#define HOST_IP "34.254.242.81"
#define HOST_PORT 8080
#define INPUT_LEN 150

char *is_error(char* data) {
    if (!data) return NULL;
    return strstr(data, "\"error\"");
}

void parse_response(char* response, int code, char **token, char **cookie) {
    char *response_json = basic_extract_json_response(response); // geting the payload

    if (response_json && is_error(response_json)) { // checking if it is an error
        char *err = strstr(response_json, ":\"");
        err += 2;
        err = strtok(err, "\"");

        fprintf(stderr, "\nServer: \033[0;31mERROR: %s\033[0m\n", err); 
    } else if (response_json || response) { // if not printing the response
        switch (code)
        {
            case 1:
                printf("Server: \033[0;32m201 - User registred.\033[0m\n");
                break;
            case 2:
                char *session_cookie = strstr(response, "connect.sid"); // geting the session cookie
                session_cookie = strtok(session_cookie, ";");

                *cookie = session_cookie; // saving the session cookie
                printf("Server: \033[0;32m200 - You are logged in!\033[0m\n");
                break;
            case 3:
                printf("Server: \033[0;32m200 - You are logget out!\033[0m\n");
                break;
            case 4:
                char *msg = strstr(response_json, ":\"");
                msg += 2;
                msg = strtok(msg, "\""); 

                *token = msg;
                printf("Server: \033[0;32m200 - Entered library!\033[0m\n");
                break;
            case 5:
                printf("Server: \033[0;32m200 - Operation succesfully!\033[0m\n");

                response_json = strstr(response, "[") + 2;
                char *token = strtok(response_json, "{");

                printf("[\n");
                while (token) {
                    printf("\t{ %s", token);

                    token = strtok(NULL, "{]");
                    if (!token) {
                        printf("\n");
                        break;
                    }
                    printf("\n");
                }
                printf("]\n");

                break;
            case 6:
                printf("Server: \033[0;32m200 - Book found!\033[0m\n");
                printf("%s\n", response_json);
                break;
            case 7:
                printf("Server: \033[0;32m201 - Book added.\033[0m\n");
                break;
            case 8:
                printf("Server: \033[0;32m200 - Book deleted.\033[0m\n");
                break;
            default:
                break;
        }
    } else {
        printf("An error has occure, try again!!!\n");
    }
}

char *create_register_request(char* username, char* password) {
    char *data = (char*)calloc(400, sizeof(char));
    sprintf(data, "{\"username\":\"%s\",\"password\":\"%s\"}", username, password);
    
    char *request = compute_post_request(HOST_IP, "/api/v1/tema/auth/register", "application/json", data, NULL, NULL);
    free(data);

    return request;
}

char *create_login_request(char* username, char* password) {
    char *data = (char*)calloc(250, sizeof(char));
    sprintf(data, "{\"username\":\"%s\",\"password\":\"%s\"}", username, password);  // making the payload

    char *request = compute_post_request(HOST_IP, "/api/v1/tema/auth/login", "application/json", data,NULL, NULL);
    free(data);

    return request;
}

char *create_logout_request(char *cookies) {
    return compute_get_request(HOST_IP, "/api/v1/tema/auth/logout", cookies, NULL);
}

char *create_enter_lib_request(char *cookies, char *token) {
    return compute_get_request(HOST_IP, "/api/v1/tema/library/access", cookies, token);
}

char *create_get_books_request(char *cookies, char *token) {
    return compute_get_request(HOST_IP, "/api/v1/tema/library/books", cookies, token);
}

char *create_get_book_request(char *id, char *cookies, char *token) {
    char *url = (char*)calloc(250, sizeof(char));
    int id_i = atoi(id);
    if (id_i == 0)
        return NULL;

    sprintf(url, "/api/v1/tema/library/books/%d", id_i);
   
    char *request = compute_get_request(HOST_IP, url, cookies, token);
    free(url);

    return request;
}

char *create_add_book_request(char *title, char *author, char *genre,
                        char *publisher, int pages, char *cookies, char *token) {
    char *data = (char*)calloc(1000, sizeof(char));
    sprintf(data, "{\"title\":\"%s\",\"author\":\"%s\",\"genre\":\"%s\",\"page_count\":\"%d\",\"publisher\":\"%s\"}", 
        title, author, genre, pages, publisher);

    char *request = compute_post_request(HOST_IP, "/api/v1/tema/library/books", "application/json", data, cookies, token);
    free(data);

    return request;
}

char* create_delete_book_request(char *id, char *cookies, char *token) {
    char *url = (char*)calloc(250, sizeof(char));
    int id_i = atoi(id);
    if (id_i == 0)
        return NULL;

    sprintf(url, "/api/v1/tema/library/books/%d", id_i);
   
    char *request = compute_delete_request(HOST_IP, url, cookies, token);
    free(url);

    return request;
}

int main (void) {
    
    // opens a connection with the server 
    int fdsock = open_connection(HOST_IP, HOST_PORT, AF_INET, SOCK_STREAM, 0);

    char *buff = (char*)calloc(2000, sizeof(char));
    char *command = (char*)calloc(150, sizeof(char));

    char *cookies = NULL; // actuall cookie content
    char *token = NULL; // authentification token for library

    char* request = NULL;

    struct pollfd poll_fds[2]; // multiplexing
    int nr_of_clients = 2;
    
    poll_fds[0].fd = fdsock;
    poll_fds[0].events = POLLIN;

    poll_fds[1].fd = STDIN_FILENO;
    poll_fds[1].events = POLLIN;

    while (1)
    {
        // waiting for a reading event on the fdsock or stdin
        int rc = poll(poll_fds, nr_of_clients, -1);

        if (poll_fds[0].revents & POLLIN) {
            rc = recv(fdsock, buff, 2000, 0);
            if (rc == 0) {
                fdsock = open_connection(HOST_IP, HOST_PORT, AF_INET, SOCK_STREAM, 0);
                poll_fds[0].fd = fdsock;
            }

        } else if ((poll_fds[1].revents & POLLIN)) {
            // waiting for command
            scanf("%s", command);
            int command_code = 0;

            if(strcmp(command, "exit") == 0) { 
                // if we recv exit from stdin we break the loop
                break;

            } else if (strcmp(command, "register") == 0) { 
                // if we recv register i will read the username and password from the user
                char *user = (char*)calloc(120, sizeof(char));
                char *pass = (char*)calloc(120, sizeof(char));

                printf("username=");
                scanf("%s", user);
             
                if(!user) {
                    fprintf(stderr, "\033[0;31mERROR: Input invalid: Aborded!\n\033[0m");
                    continue;
                }

                printf("password=");
                scanf("%s", pass);

                if(!pass) {
                    fprintf(stderr, "\033[0;31mERROR: Input invalid: Aborded!\n\033[0m");
                    continue;
                }

                request = create_register_request(user, pass); //sending the register from
                command_code = 1;

                free(user);
                free(pass);

            } else if (strcmp(command, "login") == 0) {
                // if we recv login i will read the username and password from the user
                if (cookies) {
                    fprintf(stderr, "\033[0;33mERROR: You cannot login if you are already logged in, pls logout!\033[0m\n");
                    continue;
                }

                char *user = (char*)calloc(120, sizeof(char));
                char *pass = (char*)calloc(120, sizeof(char));

                printf("username=");
                scanf("%s", user);
             
                if(!user) {
                    fprintf(stderr, "\033[0;31mERROR: Input invalid: Aborded!\n\033[0m");
                    continue;
                }

                printf("password=");
                scanf("%s", pass);

                if(!pass) {
                    fprintf(stderr, "\033[0;31mERROR: Input invalid: Aborded!\n\033[0m");
                    continue;
                }

                request = create_login_request(user, pass); // sending the login form
                command_code = 2;
                free(user);
                free(pass);
                
            } else if (strcmp(command, "logout") == 0) {
                if (cookies) {
                    request = create_logout_request(cookies);
                    command_code = 3;
                    
                    cookies = NULL;
                    token = NULL;
                } else {
                    fprintf(stderr, "\033[0;31mERROR: You are not authentificated, pls login before enter this command!\n\033[0m"); 
                }
            } else if (strcmp(command, "enter_library") == 0) {
                if (!cookies) {
                    fprintf(stderr, "\033[0;33mWARNING: You are not authentificated, plz login before entering this command!\033[0m\n");
                    continue;
                }
                request = create_enter_lib_request(cookies, token);
                command_code = 4;
            } else if (strcmp(command, "get_books") == 0) {
                if (!token || !cookies) {
                    fprintf(stderr, "\033[0;33mWARNING: You don't have acces to the library!  Try to login or/and entry_library!\033[0m\n");
                    continue;
                }
                request = create_get_books_request(cookies, token);
                command_code = 5;
            } else if (strcmp(command, "get_book") == 0) {
                if (!token || !cookies) {
                    fprintf(stderr, "\033[0;33mWARNING: You don't have acces to the library! Try to login or/and entry_library!\033[0m\n");
                    continue;
                }

                char *id = (char*)calloc(120, sizeof(char));
                printf("id=");
                scanf("%s", id);

                if(!id) {
                    fprintf(stderr, "\033[0;31mERROR: Input invalid: Aborded!\n\033[0m");
                    continue;
                }

                request = create_get_book_request(id, cookies, token);
                if (!request) {
                    fprintf(stderr, "\033[0;33mERROR: Input is not a number!\033[0m\n");
                    continue;
                }
                command_code = 6;

                free(id);
            } else if (strcmp(command, "add_book") == 0) {
                if (!token || !cookies) {
                    fprintf(stderr, "\033[0;33mWARNING: You don't have acces to the library! Try to login or/and entry_library!\033[0m\n");
                    continue;
                }
                // if we recv login i will read the username and password from the user
                char *title = (char*)calloc(300, sizeof(char));
                
                getchar();
                printf("title=");
                fgets(title, 300, stdin);
                title[strcspn(title, "\n")] = '\0';

                if(!title) {
                    fprintf(stderr, "\033[0;31mERROR: Input invalid: Aborded!\n\033[0m");
                    continue;
                }
                char *author = (char*)calloc(120, sizeof(char));

                printf("author=");
                scanf("%s", author);
    
                if(!author) {
                    fprintf(stderr, "\033[0;31mERROR: Input invalid: Aborded!\n\033[0m");
                    continue;
                }
                char *genre = (char*)calloc(120, sizeof(char));

                printf("genre=");
                scanf("%s", genre);

                if(!genre) {
                    fprintf(stderr, "\033[0;31mERROR: Input invalid: Aborded!\n\033[0m");
                    continue;
                }
                char *publisher = (char*)calloc(120, sizeof(char));

                printf("publisher=");
                scanf("%s", publisher);

                if(!publisher) {
                    fprintf(stderr, "\033[0;31mERROR: Input invalid: Aborded!\n\033[0m");
                    continue;
                }
                char *page_count = (char*)calloc(120, sizeof(char));

                printf("page_count=");
                scanf("%s", page_count);

                if(!page_count) {
                    fprintf(stderr, "\033[0;31mERROR: Input invalid: Aborded!\n\033[0m");
                    continue;
                }

                int page = atoi(page_count);

                if (page == 0) {
                    fprintf(stderr, "\033[0;33mERROR: Input is not a number!\033[0m\n");
                    continue;
                }

                request = create_add_book_request(title, author, genre, publisher, page, cookies, token);

                free(title);
                free(author);
                free(genre);
                free(publisher);
                free(page_count);

                command_code = 7;
            } else if (strcmp(command, "delete_book") == 0) {
                if (!token || !cookies) {
                    fprintf(stderr, "\033[0;33mWARNING: You don't have acces to the library! Try to login or/and entry_library!\033[0m\n");
                    continue;
                }
                char *id = (char*)calloc(120, sizeof(char));

                printf("id=");   
                scanf("%s", id);
    
                if(!id) {
                    fprintf(stderr, "\033[0;31mERROR: Input invalid: Aborded!\n\033[0m");
                    continue;
                }

                request = create_delete_book_request(id, cookies, token);

                if (!request) {
                    fprintf(stderr, "\033[0;33mERROR: Input is not a number!\033[0m\n");
                    continue;
                }
                command_code = 8;

                free(id);
            }

            if (command_code != 0) {
                close_connection(fdsock);
                fdsock = open_connection(HOST_IP, HOST_PORT, AF_INET, SOCK_STREAM, 0);
                poll_fds[0].fd = fdsock;

                send_to_server(fdsock, request);
                char *response = receive_from_server(fdsock);

                parse_response(response, command_code, &token, &cookies);
            } else {
                fprintf(stderr, "\033[0;31mERROR: Command not found!\n\033[0m");
            }
        }
    }

    free(buff);
    free(command);

    return 0;
}

/*
login
u=TeoFIL
p=TeoFIL
enter_library
add_book
i=I love you more, Bonita!
a=Tio
g=hehe
p=1000
p=1000
*/