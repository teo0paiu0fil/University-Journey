#include <stdio.h>
#include <stdlib.h>
#include "function.h"

int main(int argc, char* argv[]) {

    unsigned int inst;
    scanf("%u", &inst); 

    unsigned int N = (inst >> 29) + 1;
    char* op = calculateOperators(N, inst);
    if(!op) return -1;

    unsigned dim = inst << ( 3 + N * 2);
    dim = (dim >> 28) + 1;

    printOperators(N, op, dim);

    return 0;
}