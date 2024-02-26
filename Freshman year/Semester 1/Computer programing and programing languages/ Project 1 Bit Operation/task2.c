#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
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

    int number_of_shorts = (int)ceil((double)((N+1)*dim)/16);
    printf("Numbers to enter = %d\n", number_of_shorts);

    printf("Enter the number: ");
    unsigned short *val = getValuesPowerOf2Dim(N, dim, number_of_shorts);
    if(!val) return -1;

    printf("Operands:");
    for(int i = 0 ; i < N + 1 ; i++)
        printf(" %hu", val[i]);

    int rez = calculate(N, op, val);

    printf("\nresult: %d\n", rez);

    return 0;
}