#include <stdio.h>
#include <stdlib.h>
#include <limits.h>

typedef struct TAnswer {
    int fVal;
    int sVal;
} TAnswer;

typedef struct TGraph {
    int vertex1;
    int vertex2;
    int len;
} TGraph;

char CheckInputVal(int N, int M) {
    if (N < 0 || N > 5000) {
        printf("bad number of vertices");
        return 1;
    }
    if (M < 0 || M > N*(N+1)/2) {
        printf("bad number of edges");
        return 1;
    }
    if (N == 0 || (N > 1 && M == 0)) {
        printf("no spanning tree");
        return 1;
    }
    if (M == 0) {
        return 1;
    }
    return 0;
}

char CheckVertexVal(int ver1, int ver2, long long len, int sizeTGraph) {
    if (ver1 < 1 || ver1 > sizeTGraph || ver2 < 1 || ver2 > sizeTGraph) {
        printf("bad vertex");
        return 1;
    }
    if (len < 0 || len > INT_MAX) {
        printf("bad length");
        return 1;
    }
    if (ver1 == ver2) {
        printf("no spanning tree");
        return 1;
    }
    return 0;
}

int Comp(const TGraph* edge1, const TGraph* edge2) {
    return edge1->len - edge2->len;
}

void MakeSpanningTree(TGraph* graph, int N, int M) {
    char* visited = calloc(N, sizeof(char));
    TAnswer* answ = malloc(sizeof(TAnswer) * (N - 1));

    qsort(graph, M, sizeof(TGraph), (int (*)(const void *, const void *)) Comp);
    visited[graph[0].vertex1 - 1] = 1;

    int countVertex = 0;
    int index = 0;
    while (countVertex < N - 1 && index < M) {
        if (visited[graph[index].vertex1 - 1] + visited[graph[index].vertex2 - 1] == 1) {
            if (visited[graph[index].vertex1 - 1]) {
                visited[graph[index].vertex2 - 1] = 1;
            }
            else {
                visited[graph[index].vertex1 - 1] = 1;
            }
            answ[countVertex].fVal = graph[index].vertex1;
            answ[countVertex].sVal = graph[index].vertex2;
            countVertex += 1;
            index = 0;
        }
        else {
            index += 1;
        }
    }

    if (countVertex == N - 1) {
        for (int i = 0; i < N - 1; i++) {
            printf("%d %d\n", answ[i].fVal, answ[i].sVal);
        }
    }
    else {
        printf("no spanning tree");
    }

    free(answ);
    free(visited);
}

int main() {
    int N, M;
    if (scanf("%d %d", &N, &M) != 2) {
        return 0;
    }
    if (CheckInputVal(N, M)) {
        return 0;
    }

    TGraph* graph = malloc(M * sizeof(TGraph));

    for (int i = 0; i < M; i++) {
        int vertex1, vertex2;
        long long len;
        if (scanf("%d %d %lld", &vertex1, &vertex2, &len) != 3) {
            printf("bad number of lines");
            free(graph);
            return 0;
        }
        if (CheckVertexVal(vertex1, vertex2, len, N)) {
            free(graph);
            return 0;
        }
        
        graph[i].vertex1 = vertex1;
        graph[i].vertex2 = vertex2;
        graph[i].len = (int)len;
    }

    MakeSpanningTree(graph, N, M);
    free(graph);
    return 0;
}
