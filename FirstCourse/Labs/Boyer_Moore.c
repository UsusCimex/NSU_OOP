#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define MAX_LEN 65536

void MakeAscii(unsigned char* sample, int* ascii){
    int len = strlen((char*)sample);
    for (int i = len - 2; i >= 0; i--) {
        if (ascii[(int)sample[i]] == 0) {
            ascii[(int)sample[i]] = len - 1 - i;
        }
    }

    for (int i = 0; i < 256; i++) {
        if (ascii[i] == 0) ascii[i] = len;
    }
}

int BoyerMoor(unsigned char* str, unsigned char* sample, int* ascii, int* pos){
    int size = strlen((char*)sample);
    int len = strlen((char*)str);
    int move = 0;
    while (len - move >= size){
        int i = size;
        do {
            printf("%d ", *pos + move + i);
            i--;
        } while (str[move + i] == sample[i] && i);
        move += ascii[(int)str[move + size - 1]];
    }
    *pos += move;
    return len - move;
}

int main(void){
    FILE* fIn = fopen("in.txt", "r");

    unsigned char sample[17];
    if(fscanf(fIn, "%16[^\n]", sample) == 0) exit(0);
    if (fgetc(fIn) == 0) exit(0);

    int ascii[256] = { 0 };
    MakeAscii(sample, ascii);
    
    int shift = 0;
    int mainPos = 0;
    unsigned char str[MAX_LEN] = {0};
    while(!feof(fIn)) {
        int tmp;
        if ((tmp = fread(&str[shift], sizeof(char), MAX_LEN - shift - 1, fIn)) == 0) exit(0);
        str[shift + tmp] = '\0';
        shift = BoyerMoor(str, sample, ascii, &mainPos);
        memmove(str, str + MAX_LEN - shift - 1, shift);
    }

    fclose(fIn);
    return EXIT_SUCCESS;
}
