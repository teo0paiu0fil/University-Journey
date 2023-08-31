#include <stdio.h>     
#include <stdlib.h>     
#include <unistd.h>     
#include <string.h>     
#include <sys/types.h>     
#include <sys/socket.h>     
#include <arpa/inet.h>     
#include <netinet/in.h>
#include <netinet/tcp.h>
#include <poll.h> 

#define MAX_CLIENTS 10
#define MAX_CONECTIONS 1000
#define MAX_CONTENTS 50

#define DIE(assertion, call_description)                                       \
  do {                                                                         \
    if (assertion) {                                                           \
      fprintf(stderr, "(%s, %d): ", __FILE__, __LINE__);                       \
      perror(call_description);                                                \
      exit(EXIT_FAILURE);                                                      \
    }                                                                          \
  } while (0)


// structs from the prev router dataplan project

typedef struct cell {
  void *element;
  struct cell *next;
} *list;

typedef struct queue {
	list head;
	list tail;
} *queue;

// end of inspiration

/**
 * Structure for sending messsages from clients to the server
*/
typedef struct msg_tcp { 
  uint8_t sf;   // one octet that is set on 1 or 0 for SF
  uint8_t command;  // the comand it comes with
  char id[11]; // if the sender is 0 this is the id of the client
  char content[2000]; // the content of the message
} *MsgTCP;

/**
 *  Structure for recieving messages from the UDP machines for the server
*/
typedef struct msg_udp {
  char topic[50]; // the topic
  uint8_t type;    // the type of content
  uint8_t content[1501];  // the content
  uint16_t port; // the port of the udp sender
  char ip[16]; // the ip of udp sender
} *MsgUDP;

/**
 * A struct to memorize important aspect of the client
*/
typedef struct client {
  char id[11];   // his id
  queue msg;// a queue of messages the stack while he was ofline from his topics where he set the sf on 1
  int curr_fd; // his current file descriptor
  uint16_t port; // the port of the client
  char ip[16]; // the ip of the client
} *Client;

/**
 * A struct that represents the topics and keep counter of the clients subscribed to the topic
*/
struct topic {
  char topic[50]; // name of the topic
  list clients;// a list of clients
};

/**
 * A strict that holds infos about a client on a topic
*/
typedef struct infos {
  Client client; // the client
  int sf;       // sf 
} *Info;