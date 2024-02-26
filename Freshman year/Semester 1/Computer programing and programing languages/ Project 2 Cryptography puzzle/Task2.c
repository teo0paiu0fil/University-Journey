#include "utils.h"

char * caeser(char *textC, int key) {
    int len =strlen(textC);
    for (int i = 0; i < len; i++) {
        if (textC[i] >= '0' && textC[i] <= '9') {
            textC[i] -= key;
            while (textC[i] < '0') textC[i] += ('9' - '0') + 1;
        } else if (textC[i] >= 'a' && textC[i] <= 'z') {
            textC[i] -= key;
            while (textC[i] < 'a') textC[i] += ('z' - 'a') + 1;
        } else if (textC[i] >= 'A' && textC[i] <= 'Z') {
            textC[i] -= key;
            while (textC[i] < 'A') textC[i] += ('Z' - 'A') + 1;
        }
    }
    return textC;
}

void vigenere(char *textC, char* key) {
    int len = strlen(textC);
    int lenK = strlen(key);
    for (int i = 0; i < len; i++) {
        if (textC[i] >= '0' && textC[i] <= '9') {
            textC[i] -= key[i % lenK] - 'A';
            while (textC[i] < '0') textC[i] += ('9' - '0') + 1;
        } else if (textC[i] >= 'a' && textC[i] <= 'z') {
            textC[i] -= key[i % lenK] - 'A';
            while (textC[i] < 'a') textC[i] += ('z' - 'a') + 1;
        } else if (textC[i] >= 'A' && textC[i] <= 'Z') {
            textC[i] -= key[i % lenK] - 'A';
            while (textC[i] < 'A') textC[i] += ('Z' - 'A') + 1;
        }
    }
    printf("%s\n", textC);

}


void addStringNumbers(char *textC1,char *textC2) {
    int len1 = strlen(textC1);
    int len2 = strlen(textC2);
    int n = (len1 > len2) ? len1 : len2; 
    int m = n;
    char *rez = calloc(n, sizeof(char));
    if(!rez) return;
    int carry = 0;

    while(len1 > 0 || len2 > 0 || carry) {
        if(len1 > 0 && len2 > 0) {
            rez[n-1] = (textC1[len1 -1] + textC2[len2 - 1] - '0' + carry);
        } else  if (len1 > 0 && len2 <= 0) {
            rez[n-1] = (textC1[len1 -1] + carry);
        } else if (len1 <= 0 && len2 > 0) {
            rez[n-1] = (textC2[len2 -1] + carry);
        } else if (len1 <= 0 && len2 <= 0) {
            rez = realloc(rez, sizeof(char)*(m+1));
            strcpy(rez +1 , rez);
            rez[0] = carry;
            break;
        }
        carry = (rez[n-1] > '9') ? 1 : 0;
        if(carry) rez[n-1] -= '9' - '0' + 1;
        len1--;
        len2--;
        n--;
    }
    n = 0;

    while(rez[0] == '0') {
        strcpy(rez, rez+1);
        n++;
    }

    rez = realloc(rez, sizeof(char)*(m+1-n));
    printf("%s\n", rez);
    free(rez);
}


void SolveTask2() {
    int key = 0;
    char *KEY = calloc(10, sizeof(char));
    char * textC = calloc(1000, sizeof(char));
    char * textC1 = calloc(1000, sizeof(char));
    char* type = calloc(10, sizeof(char));
    if(!type) return;
  
    scanf("%s", type);

    switch (type[0])
    {
    case 'c':
        scanf("%d", &key);
        scanf("%s", textC);
        textC = caeser(textC, key);
        printf("%s\n", textC);
        break;
    case 'v':
        scanf("%s", KEY);
        scanf("%s", textC);
        vigenere(textC, KEY);
        break;
    case 'a':
        scanf("%d", &key);
        scanf("%s", textC);
        scanf("%s", textC1);
        textC = caeser(textC, key);
        textC1 = caeser(textC1, key);
        addStringNumbers(textC, textC1);
        break;

    default:
        break;
    }

    free(KEY);
    free(textC);
    free(textC1);
    free(type);
}
