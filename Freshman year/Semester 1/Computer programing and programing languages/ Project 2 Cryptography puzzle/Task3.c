#include "utils.h"

void SolveTask3() {

    int i = 0;
    char *map[1000];
    int vec[1000] = {0};

    char *cuv = calloc(100, sizeof(char));
    if (!cuv) return;
    char *ant = calloc(100, sizeof(char));
    if (!ant) { free(cuv); return; }
    scanf("%s", ant);

    while (scanf("%s", cuv) == 1) {
        int k = 0;
        char* aux = strtok(cuv," ,.?:!\n");
        strcat(ant, " ");
        strcat(ant, cuv);
        for(int j = 0; j < i; j++)
            if (strcmp(map[j], ant) == 0) {
                vec[j] += 1;
                k++;
            }
	    if (k == 0) {
            map[i] = strdup(ant);
            vec[i] = 1;
            i++;
        }
        strcpy(ant, cuv);
    }

    for (int j = 0; j < i; j++) {
        printf("%s ", map[j]);
        printf("%d\t\n", vec[j]);
    }

    free(cuv);
    free(ant);

}
