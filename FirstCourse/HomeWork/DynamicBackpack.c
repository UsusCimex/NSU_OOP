#include <stdio.h>
#include <stdlib.h>

int Max(int a, int b) {
    if (a > b) return a;
    return b;
}

typedef struct Items {
    int weight;
    int cost;
} Items;

int main() {
    int N, W;
    if (scanf("%d %d", &N, &W) != 2) return 0;

    unsigned int* bag = calloc((W + 1) * (N + 1), sizeof(int));
    Items* items = malloc(sizeof(Items) * N);

    for (int i = 1; i <= N; i++) {
        int weight, cost;
        if (scanf("%d %d", &weight, &cost) != 2) return 0;
        items[i-1].weight = weight;
        items[i-1].cost = cost;

        for (int j = 0; j <= W; j++) {
            if (j >= weight)
                bag[i*(W+1) + j] = Max(bag[(i-1)*(W+1) + j], bag[(i-1)*(W+1) + j - weight] + cost);
            else
                bag[i*(W+1) + j] = bag[(i-1)*(W+1) + j];
        }
    }

    int item = N;
    int itemWeight = W;
    printf("%d\n", bag[(N+1)*(W+1) - 1]);
    while (item > 0) {
        if (bag[item*(W+1) + itemWeight] == bag[(item-1)*(W+1) + itemWeight]) {
            item -= 1;
        }
        else {
            itemWeight -= items[item - 1].weight;
            printf("%d %d\n", items[item - 1].weight, items[item - 1].cost);
            item -= 1;
        }
    }

    free(bag);
    free(items);
    return EXIT_SUCCESS;
}
