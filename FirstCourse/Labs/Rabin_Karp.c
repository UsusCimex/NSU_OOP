#include <stdio.h>
#include <math.h>
#include <string.h>

int GetHash(unsigned char *string, int len) {
    int hash = 0, pow = 1;
    for (int i = 0; i < len; ++i) {
        hash += (string[i] % 3) * pow;
        pow *= 3;
    }
    return hash;
}

int main() {
    unsigned char sample[17] = { 0 };
    int len = 0;
    int c;
    while ((c = getchar()) != '\n') {
        sample[len] = c;
        len++;
    }
    int myPow = pow(3, len - 1);

    unsigned char text[17] = { 0 };
    for (int m = 0; m < len; ++m) {
        text[m] = getchar();
    }

    int mainHash = GetHash(sample, len);
    printf("%d ", mainHash);
    int thisHash = GetHash(text, len);

    int mainPos = 0;
    while (text[len - 1] != (unsigned char)EOF){
        if (mainHash == thisHash) {
            for(int j = 0; j < len; ++j) {
                int cur = j + mainPos + 1;
                printf("%d ", cur);
                if (sample[j] != text[j])
                    break;
            }
        }

        thisHash -= text[0] % 3;
        thisHash /= 3;

        memmove(text, text + sizeof(char), len);
        text[len - 1] = getchar();

        thisHash += (text[len - 1] % 3) * myPow;
        mainPos += 1;
    }
    return 0;
}
