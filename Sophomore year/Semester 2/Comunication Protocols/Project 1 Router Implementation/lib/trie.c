#include <utils.h>

Trie create_trie() {
    Trie t = calloc(sizeof(struct _node_16), 1);
    t->path = calloc(sizeof(struct _node_1), 65536);
    return t;
}

void insert(struct route_table_entry *elem, Trie t) {
    int count = 0;
    Trie aux = t;

    for (int counter = 31; counter >= 0; counter--)
    {
        if (((elem->mask >> counter) & 1) != 0) {
            count++;
        }
    }

    count -= 16;
    uint32_t prefix = ntohl(elem->prefix);
    uint32_t index = (prefix & 0xFFFF0000);
    index = index >> 16;
    struct _node_1* next = aux->path + index;
    if (!next)
        next = calloc(1, sizeof(struct _node_1));

    for (int i = 15; i >= 16 - count; i--) {
        int y = (prefix & (1 << i));
        if (y == 0) {
            if (!next->zero) 
                next->zero = calloc(1, sizeof(struct _node_1));
            next = next->zero;
        } else {
            if (!next->one) 
                next->one = calloc(1, sizeof(struct _node_1));
            next = next->one;
        }
    }
    next->element = elem;
}

Trie fill_up_trie(struct route_table_entry *rtable, size_t len) {
    Trie t = create_trie();
    for (int i = 0; i < len; i++) {
        insert(&rtable[i], t);
    }
    return t;
}

struct route_table_entry* search(uint32_t ip, Trie t) {
    struct route_table_entry* tmp = NULL;
    Trie aux = t;
    uint32_t prefix = ntohl(ip);
    uint32_t index = (prefix & 0xFFFF0000);
    index = index >> 16;
    struct _node_1* next = aux->path + index;
    int i = 15;
    
    while (1) {
        if (i == -1) break;
        int y = (prefix & (1 << i));

        if (y == 0) {
            if (next->element) {
                tmp = (struct route_table_entry*)next->element;
            }       
            if (!next->zero) 
               break;
            next = next->zero;
        } else {
            if (next->element) {
                tmp = (struct route_table_entry*)next->element;
            }
            if (!next->one) 
               break;
            next = next->one;
        }
        i--;
    }
    return tmp;
}