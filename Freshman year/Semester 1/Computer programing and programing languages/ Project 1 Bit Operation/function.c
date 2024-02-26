#include <stdio.h>
#include <stdlib.h>
#include <string.h>

char* calculateOperators(unsigned int N, unsigned int inst) {
    char* op = calloc(N, sizeof(char));
    if(!op) return NULL;

    for(int i = 0; i < N; i++) {
        unsigned int bit_op = inst << (3 + i * 2);
        bit_op = bit_op >> 30;
        switch (bit_op)
        {
        case 0:
            op[i] = '+';
            break;
        case 1:
            op[i] = '-';
            break;
        case 2:
            op[i] = '*';
            break;
        case 3:
            op[i] = '/';
            break;

        default:
            break;
        }
    }

    return op;
}

void printOperators(unsigned int N, char *op, unsigned int dim) {
    printf("%u ", N);
    for(int i = 0; i < N; i++)
        printf("%c ", op[i]);
    printf("%u\n", dim);
}

unsigned short* getValuesPowerOf2Dim(unsigned int N, unsigned int dim, int number_of_shorts) {
    void* nums = (void*)calloc(number_of_shorts, sizeof(unsigned short));
    if(!nums) return NULL;

    unsigned short* val = calloc(N+1, sizeof(unsigned short));
    if(!val) { free(nums); return NULL;}

    for(int i = 0; i < number_of_shorts; i ++) 
        scanf("%hu", (unsigned short*)(nums + i * sizeof(unsigned short)));

    for(int i = 0; i < N + 1; i++) {
        for(int j = 0; j < 16/dim ; j++) {
            unsigned short mask = (1 << dim ) - 1;
            mask = mask << (16/dim - j - 1) * dim;
            mask &= *(unsigned short*)(nums + i * sizeof(unsigned short));
            mask = mask >> (16/dim - j - 1) * dim;
            val[i*(16/dim)+j] = mask;
        }
    }
    free(nums);
    return val;
}

int calculate(unsigned int N, char * op, unsigned short* val) {
    int rez = val[0];
    for(int i = 0; i < N ; i++){
        switch (op[i])
        {
        case '+':
            rez += val[i+1];
            break;
        case '-':
            rez -= val[i+1];
            break;
        case '*':
            rez *= val[i+1];
            break;
        case '/':
            rez /= val[i+1];
            break;
        default:
            break;
        }
    }
    return rez;
}