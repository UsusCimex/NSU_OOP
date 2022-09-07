#include <stdlib.h>
#include <stdio.h>
#include <string.h>

void Swap(char* symA, char* symB)
{
    char temp = *symA;
    *symA = *symB;
    *symB = temp;
}

int main(void) {
    char word[17] = { 0 };
    if (scanf("%16[^\n]s", word) != 1) exit(0);

    int count = 0;
    if (scanf("%d", &count) != 1) exit(0);

    char test[10] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    for (unsigned int i = 0; i < strlen(word); i++)
    {
        if ((word[i] < '0') || (word[i] > '9') || (test[word[i] - '0']++ > 0)) {printf("bad input"); exit(0);}
    }

    while (count > 0){
        for (unsigned int i = strlen(word) - 1; i > 0; i--)
        {
            if (word[i] > word[i - 1])
            {
                int dotPer = i;
                for (unsigned int j = i; j < strlen(word); j++)
                {
                    if (word[j] > word[i - 1] && word[j] < word[dotPer]) dotPer = j;
                }
                Swap(&word[i - 1], &word[dotPer]);
                for (unsigned int j = strlen(word)-1; i < j; i++, j--)
                {
                    Swap(&word[i], &word[j]);
                }
                printf("%s\n", word);
                break;
            }
        }
        count--;
    }
    return EXIT_SUCCESS;
}
