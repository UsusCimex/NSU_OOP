#include <stdio.h>
#include <stdlib.h>
#define MAXSIZE 2000

FILE* file;
char* tableVertex;
short* visList;

void FreeMem() {
    if (tableVertex == NULL) free(tableVertex);
    if (visList == NULL) free(visList);
    fclose(file);
}

void CheckEnter(int N, int M) {
    if (N < 0 || N > MAXSIZE) {
        printf("bad number of vertices");
        fclose(file);
        exit(0);
    }
    if (M < 0 || M > N * (N + 1) / 2) {
        printf("bad number of edges");
        fclose(file);
        exit(0);
    }
}

short* TopologicalSort(int countEdge, int countVertex) {
    short* sortedVertex = malloc(sizeof(short) * (countVertex + 1));
    int index = 0;

    while (countEdge > 0) {
        char boolImpossible = 1;
        for (int i = 0; i < countVertex + 1; i++) {
            if (i == countVertex && boolImpossible) {
                printf("impossible to sort");
                free(sortedVertex);
                FreeMem();
                exit(0);
            }
            if (visList[i] == 0) {
                for (int j = 0; j < countVertex; j++) {
                    if (tableVertex[i * countVertex + j] > 0) {
                        tableVertex[i * countVertex + j] -= 1;
                        visList[j] -= 1;
                        countEdge -= 1;
                    }
                }
                visList[i] -= 1;
                sortedVertex[index] = i + 1;
                index += 1;
                boolImpossible = 0;
                break;
            }
        }
    }

    for (int i = 0; i < countVertex; i++) {
        if (visList[i] == 0) {
            sortedVertex[index] = i + 1;
            index += 1;
        }
    }

    return sortedVertex;
}

int main() {
    file = fopen("in.txt", "r");
    int countVertex, countEdge;
    if (fscanf(file, "%d\n%d", &countVertex, &countEdge) != 2) {
        printf("bad number of lines");
        FreeMem();
        return EXIT_SUCCESS;
    }
    CheckEnter(countVertex, countEdge);

    tableVertex = calloc(countVertex * countVertex, sizeof(char)); 
    visList = calloc(countVertex, sizeof(short));
    for (int i = 0; i < countEdge; i++) {
        int from, to;
        if (fscanf(file, "%d %d", &from, &to) != 2) {
            printf("bad number of lines");
            FreeMem();
            return EXIT_SUCCESS;
        }
        if (from < 1 || from > countVertex || to < 1 || to > countVertex) {
            printf("bad vertex");
            FreeMem();
            return EXIT_SUCCESS;
        }

        tableVertex[(from - 1) * countVertex + (to - 1)] = 1;
        visList[to - 1] += 1;
    }

    short* sortedVertex = TopologicalSort(countEdge, countVertex);
    
    for (int i = 0; i < countVertex; i++) {
        printf("%hd ", sortedVertex[i]);
    }

    free(sortedVertex);
    FreeMem();
    return EXIT_SUCCESS;
}
