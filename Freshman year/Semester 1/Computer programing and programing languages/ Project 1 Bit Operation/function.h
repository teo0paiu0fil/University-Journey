#ifndef _function
#define _function

char* calculateOperators(unsigned int N, unsigned int inst);
void printOperators(unsigned int N, char *op, unsigned int dim);
unsigned short* getValuesPowerOf2Dim(unsigned int N, unsigned int dim, int number_of_shorts);
unsigned short*  getValuesAnyDim(unsigned int N, unsigned int dim, int number_of_shorts);
int calculate(unsigned int N, char * op, unsigned short* val);

#endif