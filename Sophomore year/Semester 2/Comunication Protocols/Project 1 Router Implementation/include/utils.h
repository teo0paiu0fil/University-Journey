#ifndef _SKEL_UTILS_
#define _SKEL_UTILS_
#include <arpa/inet.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>

#include "trie.h"
#include "queue.h"
#include "list.h"
#include "lib.h"
#include "protocols.h"

#define IPv4 0x0800
#define ARP  0x0806

#define ARP_REQ_OP 1
#define ARP_RLY_OP 2

#define MAC_SIZE 6
#define IP_SIZE 4

#define ID 1
#define ETHERNET_HRD 1
#define IP_HRD 4
#define ICMP_ID 1

#define print_mac(mac) \
    do { \
        fprintf(stderr, "%02x:%02x:%02x:%02x:%02x:%02x\n", \
            (unsigned char)(mac[0]), \
            (unsigned char)(mac[1]), \
            (unsigned char)(mac[2]), \
            (unsigned char)(mac[3]), \
            (unsigned char)(mac[4]), \
            (unsigned char)(mac[5])); \
    } while (0)


typedef struct pachet_t {
    char buf[MAX_PACKET_LEN];
    size_t len;
    struct route_table_entry* route;
} *Packet_t;


#endif