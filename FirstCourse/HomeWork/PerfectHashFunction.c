#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define SIZEKEY 501
#define CONSTSIZE 7

unsigned int Max(int a, int b) {
    if (a > b) return a;
    return b;
}

unsigned int GetHash(char* key, int k, int n) {
    unsigned int hash = 0;
    int len = strlen(key);
    for (int i = 0; i < len; i++) {
        hash += key[i] + k;
        hash += (hash << 10);
        hash ^= (hash >> 6);
    }
    hash += (hash << 3);
    hash ^= (hash >> 11);
    hash += (hash << 15);
    return hash % n;
}

typedef struct HashTable {
    unsigned char count;
    char* string;
    struct HashTable* nextHashTable;
} HashTable;

void AddNextHash(HashTable* table, char* string, int coefHash) {
    int hash = GetHash(string, coefHash, CONSTSIZE);
    if (table[hash].count == 0) {
        table[hash].count += 1;
        table[hash].string = malloc(sizeof(char) * strlen(string) + 1);
        memcpy(table[hash].string, string, sizeof(char) * strlen(string) + 1);
    }
    else {
        if (!strcmp(table[hash].string, string)) return;
        table[hash].count += 1;
        if (table[hash].nextHashTable == NULL)
            table[hash].nextHashTable = calloc(CONSTSIZE, sizeof(HashTable));
        AddNextHash(table[hash].nextHashTable, string, coefHash + 1);
    }
}

void SearchHash(HashTable* table, char* string, int coefHash) {
    if (table == NULL) {
        printf("0");
        return;
    }
    int hash = GetHash(string, coefHash, CONSTSIZE);
    if (table[hash].count == 0) {
        printf("0");
    }
    else {
        if (!strcmp(table[hash].string, string))
            printf("1");
        else SearchHash(table[hash].nextHashTable, string, coefHash + 1);
    }
}

int main() {
    int N, M;
    if (scanf("%d %d", &N, &M) != 2) return 0;
    HashTable* table = calloc(N * 2, sizeof(HashTable));

    unsigned int coeffHash = 2;
    char string[SIZEKEY];
    for (int i = 0; i < N; i++) {
        if (scanf("%s", string) != 1) return 0;
        int hash = GetHash(string, coeffHash, N * 2);
        if (table[hash].count == 0) {
            table[hash].string = malloc(sizeof(char) * strlen(string) + 1);
            memcpy(table[hash].string, string, sizeof(char) * strlen(string) + 1);
            table[hash].count += 1;
        }
        else {
            if (table[hash].nextHashTable == NULL)
                table[hash].nextHashTable = calloc(CONSTSIZE, sizeof(HashTable));
            AddNextHash(table[hash].nextHashTable, string, coeffHash);
        }
    }

    for (int i = 0; i < M; i++) {
        if (scanf("%s", string) != 1) return 0;
        int hash = GetHash(string, coeffHash, N * 2);
        if (table[hash].count == 0) printf("0");
        else if (!strcmp(table[hash].string, string)) printf("1");
        else SearchHash(table[hash].nextHashTable, string, coeffHash);
    }

    free(table);
    return EXIT_SUCCESS;
}