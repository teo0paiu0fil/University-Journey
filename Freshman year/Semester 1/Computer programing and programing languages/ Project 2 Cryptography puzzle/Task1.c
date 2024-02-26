#include "utils.h"

void printMatrix(int *mat, int m, int n) {
    for(int i = 0; i < m; i++) {
        for(int j = 0; j < n; j++) {
	printf("%d ", *(mat + i * n +j));
        }
        printf("\n");
    }
}

int MAX(char* tok) {
    int aux = 0;
    int j = 0;
    for(int i = 0; i < 4; i++) {
        if(tok[i] > j) {
            j = tok[i];
            aux = i;
        }
    }
    return aux;
}

int palindrom(char* token) {
    int len = strlen(token);
        for(int i = 0; i < len / 2; i++)
            if(token[i] != token[len - i - 1])
                return 0;
    return 1;
}

int isPrime(char* token) {
    int n = atoi(token);
    for (int i = 2; i <= n / 2; ++i) {
        if (n % i == 0) {
            return 0;
        }
    }
    return 1;
}

int SumOfK(char* token, int n, int k) {
    int s = 0;
    for(int i = 0; i < k; i++) {
        s += CharToInt(token[(i * k) % n]);
    }
    return s % 4;
}

void typeA(char * token, int *i, int *j) {
    int max = MAX(token);
    switch (max)
    {
    case 0:
        (*j)++;
        break;
    
    case 1:
        (*i)--;
        break;

    case 2:
        (*j)--;
        break;
    
    case 3:
        (*i)++;
        break;
    
    default:
        break;
    }

}

void typeB(char * token, int *i, int *j) {
    int prim = isPrime(token + strlen(token) - 2);
    int pal = palindrom(token);
    if ( pal == 0 && prim == 0) {
        (*i)++;
    } else if ( pal == 1 && prim == 0) {
        (*j)++;
    } else if ( pal == 1 && prim == 1) {
        (*j)--;
    } else if ( pal == 0 && prim == 1) {
        (*i)--;
    }
}

void typeC(char * token, int *i, int *j) {
    int rez = SumOfK(token+2, CharToInt(token[0]), CharToInt(token[1]));
    switch (rez) {
	case 0:
	    (*j)--;
	    break;

	case 1:
	    (*i)--;
	    break;

	case 2:
	    (*j)++;
	    break;

        case 3:
	    (*i)++;
	    break;

	default:
	    break;
    }
	
}

void SolveTask1() {
    int m,n,i = 0, j = 0, crt_mov = 2;
    char *line = NULL;
    size_t len = 0;
    scanf("%d%d", &m, &n);

    int *Matrix = calloc(m * n, sizeof(int));
    if(!Matrix) return;
   
    getline(&line, &len, stdin);
    getline(&line, &len, stdin);
    char* token = strtok(line, " ");

    Matrix[0] = 1;

    while(token) {
        switch (token[0])
        {
        case 'a':
            typeA(token + 1, &i, &j);
            break;
	case 'b':
            typeB(token + 1, &i, &j);
            break;
        case 'c':
            typeC(token + 1, &i, &j);
            break;
        default:
            break;
        }

        Matrix[i * n + j] = crt_mov++;

        token = strtok(NULL , " \n");
    }
    free(line);
    printMatrix(Matrix, m, n);
    free(Matrix);
}
