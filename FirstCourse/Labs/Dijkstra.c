#include <stdio.h>
#include <stdlib.h>
#include <limits.h>

unsigned int Min(long long a, long long b) {
    if (a < b) {
        return a;
    }
    return b;
}

char CheckVertex(int vertex, int limit) {
    if (vertex < 1 || vertex > limit) {
        printf("bad vertex");
        return 1;
    }
    return 0;
}

char CheckEdge(int vertex1, int vertex2, long long len, int limit) {
    if (len < 1 || len > INT_MAX) {
        printf("bad length");
        return 1;
    }
    if (CheckVertex(vertex1, limit) || CheckVertex(vertex2, limit)) {
        printf("bad vertex");
        return 1;
    }
    return 0;
}

void FindMinPath(unsigned int* graph, int N, int S, int F) { //Dijkstra
    int* vertex = calloc(N + 1, sizeof(int));
    unsigned int* lenght = malloc((N + 1) * sizeof(int));
    char* visited = calloc(N + 1, sizeof(char));
    for (int i = 0; i < N + 1; i++) {
        lenght[i] = (unsigned)INT_MAX + 2;
    }

    visited[S] = 1;
    vertex[S] = S;
    lenght[S] = 0;

    int nextVertex = S;
    unsigned int curSum = 0;
    for (int countVertex = 0; countVertex < N; countVertex++) {
        unsigned int minSum = (unsigned)INT_MAX + 2;
        int savedVertex = 0;
        for(int i = 0; i < N; i++) {
            if (visited[i + 1] == 0 && graph[(nextVertex - 1) * N + i]) {
                if (lenght[i + 1] > Min((unsigned)INT_MAX + 1, graph[(nextVertex - 1) * N + i] + curSum)) {
                    lenght[i + 1] = Min((unsigned)INT_MAX + 1, graph[(nextVertex - 1) * N + i] + curSum);
                    vertex[i + 1] = nextVertex;
                }
            }
            if (visited[i + 1] == 0 && lenght[i + 1] < minSum) {
                minSum = lenght[i + 1];
                savedVertex = i + 1;
            }
        }

        if (savedVertex) {
            visited[savedVertex] = 1;
            curSum = minSum;
            nextVertex = savedVertex;
        }
        else {
            break;
        }
    }

    for (int i = 0; i < N; i++) {
        if (lenght[i + 1] == (unsigned)INT_MAX + 1) {
            printf("INT_MAX+ ");
        }
        else if (lenght[i + 1] > (unsigned)INT_MAX + 1) {
            printf("oo ");
        }
        else {
            printf("%u ", lenght[i + 1]);
        }
    }
    printf("\n");

    int way = F;
    if (vertex[way] == 0) {
        printf("no path");
    }
    else {
        int checkOverflow = 0;
        for (int i = 0; i < N; i++) {
            if (graph[(way - 1) * N + i] && (lenght[way] + graph[(way - 1) * N + i] >= (unsigned)INT_MAX + 1)) {
                checkOverflow += 1;
            }
        }
        if (lenght[way] > INT_MAX && checkOverflow > 1) {
            printf("overflow");
        }
        else {
            while (way != S && way != 0) {
                printf("%d ", way);
                way = vertex[way];
            }
            printf("%d", way);
        }
    }
    free(visited);
    free(vertex);
    free(lenght);
} 

int main() {
    int N;
    if (scanf("%d", &N) != 1) {
        return 0;
    }
    if (N < 0 || N > 5000) {
        printf("bad number of vertices");
        return 0;
    }

    int S, F;
    if (scanf("%d %d", &S, &F) != 2) {
        return 0;
    }
    if (CheckVertex(S, N) || CheckVertex(F, N)) {
        return 0;
    }

    int M;
    if (scanf("%d", &M) != 1) {
        return 0;
    }
    if (M < 0 || M > (N * (N + 1) / 2)) {
        printf("bad number of edges");
        return 0;
    }

    unsigned int* graph = calloc(N * N, sizeof(int));
    
    for (int i = 0; i < M; i++) {
        int from, to;
        long long len;
        if (scanf("%d %d %lld", &from, &to, &len) != 3) {
            printf("bad number of lines");
            free(graph);
            return 0;
        }
        if (CheckEdge(from, to, len, N)) {
            free(graph);
            return 0;
        }

        graph[(from - 1) * N + (to - 1)] = len;
        graph[(to - 1) * N + (from - 1)] = len;
    }

    FindMinPath(graph, N, S, F);
    free(graph);
    return 0;
}
