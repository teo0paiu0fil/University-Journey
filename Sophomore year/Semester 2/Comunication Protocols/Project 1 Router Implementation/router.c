#include "utils.h"

static uint8_t BROADCAST[MAC_SIZE]   = {0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF};
static uint8_t ARP_REQUEST_MAC[MAC_SIZE] = {0, 0, 0, 0, 0, 0};

static struct route_table_entry *rtable = NULL;
static int rtable_len = 0;

static list arp_cache = NULL;
static queue packets  = NULL;

static Trie t = NULL;

void complete_ether_hdr(struct ether_header *meta, void *dest, void *src, uint16_t type) {
	memcpy(meta->ether_dhost, dest, MAC_SIZE);
	memcpy(meta->ether_shost, src, MAC_SIZE);
	meta->ether_type = htons(type);
}

void complete_ip_hdr(struct iphdr *meta, int ip_dest, int ip_src, uint8_t ttl, uint8_t protocol, uint16_t payload) {
	meta->version  = 4;
	meta->ihl      = 5;
	meta->tos      = 0;
	meta->tot_len  = htons(sizeof(struct iphdr) + payload);
	meta->id       = htons(ID);
	meta->frag_off = 0;
	meta->ttl      = ttl;
	meta->protocol = protocol;
	meta->check    = 0;
	meta->saddr    = ip_src;
	meta->daddr    = ip_dest;

	meta->check = htons(checksum((void*)meta, sizeof(struct iphdr)));
}

void complete_arp_hdr(struct arp_header *meta, int ip_sender, int ip_target, uint8_t mac_target[MAC_SIZE], uint8_t mac_sender[MAC_SIZE], uint16_t op) {
	meta->htype = htons(ETHERNET_HRD);
	meta->ptype = htons(IPv4);
	meta->hlen  = MAC_SIZE;
	meta->plen  = IP_SIZE;
	meta->op    = htons(op);
	memcpy(meta->sha, mac_sender, MAC_SIZE);
	meta->spa   = ip_sender;
	memcpy(meta->tha, mac_target, MAC_SIZE);
	meta->tpa   = ip_target;
}

struct arp_entry *get_arp_cache(struct route_table_entry *route) {
	list aux = arp_cache;
	struct arp_entry *arp = NULL;

	for (; aux != NULL; aux = aux->next) {
		if (((struct arp_entry*)(aux->element))->ip == route->next_hop)
			arp = aux->element;
	}

	return arp;
}

void send_arp_request(struct route_table_entry *route) {
	void* buff = calloc(1, sizeof(struct ether_header) + sizeof(struct arp_header));
	DIE(!buff, "alocare");

	struct ether_header *eth_buf = (struct ether_header*) buff;
	struct arp_header *arp_buf = (struct arp_header*)(buff + sizeof(struct ether_header));

	uint8_t *mac = calloc(6, sizeof(uint8_t));
	get_interface_mac(route->interface, mac);

	complete_ether_hdr(eth_buf, BROADCAST, mac, ARP);

	complete_arp_hdr(arp_buf, inet_addr(get_interface_ip(route->interface)), route->next_hop, ARP_REQUEST_MAC, mac, ARP_REQ_OP);
	
	int rc = send_to_link(route->interface, buff, sizeof(struct ether_header) + sizeof(struct arp_header)); 
	if (rc < 0) {
		fprintf(stderr, "eroare la trimitere\n");
	}
}

void send_icmp(int interface, char buf[MAX_PACKET_LEN], size_t len, uint8_t type) {
	uint8_t* mac = calloc(6 ,  sizeof(char));
	get_interface_mac(interface, mac);

	struct ether_header * eth_hdr = (struct ether_header*) buf ;
	struct iphdr * ip_hdr = (struct iphdr *)(buf + sizeof(struct ether_header));
	struct icmphdr* icmp_hdr = (struct icmphdr* )(buf + sizeof(struct ether_header) + sizeof(struct iphdr));

	complete_ether_hdr(eth_hdr, eth_hdr->ether_shost, mac, IPv4);
	complete_ip_hdr(ip_hdr, ip_hdr->saddr, inet_addr(get_interface_ip(interface)), 64, ICMP_ID, sizeof(struct icmphdr));

	icmp_hdr->checksum = 0;
	icmp_hdr->code = 0;
	icmp_hdr->type = type;
	icmp_hdr->checksum = htons(checksum((void*)icmp_hdr, sizeof(struct icmphdr)));

	int rc = send_to_link(interface, buf, sizeof(struct icmphdr) + sizeof(struct ether_header) + sizeof(struct iphdr));
	if ( rc < 0) {
		fprintf(stderr, "eroare la trimitere\n");
	}
}

void deal_with_ip4(int interface, char buf[MAX_PACKET_LEN], size_t len, struct ether_header *eth_hdr) {

	struct iphdr *ip_hdr = (struct iphdr *)(buf + sizeof(struct ether_header));

	u_int8_t *mac = calloc(6, sizeof(uint8_t));
	DIE(!mac, "eroare alocare");

	get_interface_mac(interface, mac);

	if (memcmp((char *)mac, (char*)&eth_hdr->ether_dhost, 6) != 0) {
		printf("Adresele difera\n");
		print_mac(mac);
		print_mac(eth_hdr->ether_dhost);
		return;
	}

	if (checksum((void*)ip_hdr, sizeof(struct iphdr)) != 0) {   // verific checksum-ul
		fprintf(stderr, "checksum gresit\n");
		return;
	}

	ip_hdr->ttl--;					
	if (ip_hdr->ttl <= 1) {
		fprintf(stderr, "ttl a expirat\n");
		send_icmp(interface, buf, len, 11);
		return;
	}

	ip_hdr->check = 0;
	ip_hdr->check = htons(checksum((void*)ip_hdr, sizeof(struct iphdr)));		// recalculez checksum-ul

	if (ip_hdr->daddr == inet_addr(get_interface_ip(interface))) {

		struct icmphdr* aux =  ((struct icmphdr *) (buf + sizeof(struct ether_header) + sizeof(struct iphdr))); 
		if (aux->type == 8) {
			send_icmp(interface, buf, len, 0);
			return;
		}
	}

	struct route_table_entry *route = search(ip_hdr->daddr, t);
	
	if(route) {
        printf("%u %d\n",ntohl(route->prefix), route->interface);
	} else
        printf("null\n");

	if (!route || route->next_hop == 0) {
		fprintf(stderr, "fara ruta\n");
		send_icmp(interface, buf, len, 3);
		return;
	}

	struct arp_entry* arp = get_arp_cache(route);

	if (!arp) {
		send_arp_request(route);

		Packet_t packet = calloc(1, sizeof(struct pachet_t));
		memcpy(packet->buf, buf, len);
		packet->len = len;
		packet->route = route;

		queue_enq(packets, packet);
		return;
	}

	complete_ether_hdr(eth_hdr, &arp->mac, mac, IPv4);

	int rc = send_to_link(route->interface, buf, len); 
	if ( rc < 0) {
		fprintf(stderr, "eroare la trimitere\n");
	}
}

void deal_with_arp(int interface, char buf[MAX_PACKET_LEN], size_t len,  struct ether_header *eth_buf) {

	struct arp_header* arp_hdr = (struct arp_header*)(buf + sizeof(struct ether_header));
	
	if (ntohs(arp_hdr->op) == ARP_RLY_OP) {
		struct arp_entry* arp_aux = calloc(1, sizeof(struct arp_entry));
		
		arp_aux->ip = arp_hdr->spa;
		memcpy(&arp_aux->mac, arp_hdr->sha, 6);
		
		arp_cache = cons(arp_aux, arp_cache);
		queue aux = queue_create();

		while (!queue_empty(packets)) {
			Packet_t temp = (Packet_t)queue_deq(packets);

			if (temp->route->next_hop == arp_aux->ip) {
				memcpy(((struct ether_header*)temp->buf)->ether_dhost, &arp_aux->mac, 6);  // copiez mac-ul next hop-ului in destinatie

				int rc = send_to_link(temp->route->interface, temp->buf, temp->len); // trimit pachetul la urmatoarea interfata
				if ( rc < 0) {
					fprintf(stderr, "eroare la trimitere\n");
				}
				continue;
			}
			queue_enq(aux, temp);
		}

		free(packets);
		packets = aux;
		
	} else if (ntohs(arp_hdr->op) == ARP_REQ_OP && arp_hdr->tpa == inet_addr(get_interface_ip(interface))) {
		struct arp_header *arp_buf = (struct arp_header*)(buf + sizeof(struct ether_header));
	
		u_int8_t *mac = calloc(6, sizeof(uint8_t));
		DIE(!mac, "eroare alocare");

		void *buff = calloc(1, sizeof(struct ether_header) + sizeof(struct arp_header));
	
		struct ether_header* eth_hdr = (struct ether_header*) buff;
		struct arp_header *arp_hdr = (struct arp_header*)(buff + sizeof(struct ether_header));

		get_interface_mac(interface, mac);
		
		complete_ether_hdr(eth_hdr, eth_buf->ether_shost, mac, ARP);
		print_mac(arp_buf->sha);
		print_mac(mac);
	
		complete_arp_hdr(arp_hdr, inet_addr(get_interface_ip(interface)), arp_buf->spa, arp_buf->sha, mac, ARP_RLY_OP);
		
		int rc = send_to_link(interface, buff, sizeof(struct ether_header) + sizeof(struct arp_header)); 
		if ( rc < 0) {
			fprintf(stderr, "eroare la trimitere\n");
		}
	}
}

int main(int argc, char *argv[])
{
	char buf[MAX_PACKET_LEN];

	// Do not modify this line
	init(argc - 2, argv + 2);

	rtable = calloc(sizeof(struct route_table_entry), 100000);
	DIE(rtable == NULL, "memory");

	rtable_len = read_rtable(argv[1], rtable);
	packets = queue_create();

	t = fill_up_trie(rtable, rtable_len);
	
	while (1) {
		
		int interface;
		size_t len;
		
		interface = recv_from_any_link(buf, &len);
		DIE(interface < 0, "recv_from_any_links");
		
		struct ether_header *eth_hdr = (struct ether_header *) buf;

		if (ntohs(eth_hdr->ether_type) == IPv4) {
			deal_with_ip4(interface, buf, len, eth_hdr);
		} else if (ntohs(eth_hdr->ether_type) == ARP) {
			deal_with_arp(interface, buf, len, eth_hdr);
		}
	}
}

