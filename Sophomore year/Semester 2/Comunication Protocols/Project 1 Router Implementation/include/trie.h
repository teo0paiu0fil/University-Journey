#ifndef _TREE_UTILS_
#define _TREE_UTILS_

struct _node_1 {
    void* element;
    struct _node_1* zero;
    struct _node_1* one;
};

typedef struct _node_16 {
    void* element;
    struct _node_1 *path;
}* Trie;

struct route_table_entry* search(uint32_t ip, Trie t);
Trie fill_up_trie(struct route_table_entry *rtable, size_t len);

#endif