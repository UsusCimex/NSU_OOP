#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define SIZEASCII 256

FILE* fIn;
FILE* fOut;

typedef struct TTree {
    unsigned char value;
    int count;
    char* code;
    struct TTree* left;
    struct TTree* right;
} TTree;

void FreeDecodingTree(TTree* tree) {
    if (tree->left)
        FreeDecodingTree(tree->left);
    if (tree->right)
        FreeDecodingTree(tree->right);
    free(tree);
}

void FreeEncodingTree(TTree* tree) {
    if (tree->left)
        FreeEncodingTree(tree->left);
    if (tree->right)
        FreeEncodingTree(tree->right);
    if (tree->left != NULL && tree->right != NULL)
        free(tree);
    else
        free(tree->code);
}

/* Проверка на наличие значения */
char CheckIfLowest(TTree* tree) {
    if (tree->left == NULL && tree->right == NULL)
        return 1;
    return 0;
}

/* Заполнение дерева элементов, их соответствующими кодами */
void GenerateCodeElements(TTree* tree, char* curCode, unsigned char height) {
    if (CheckIfLowest(tree)) {
        tree->code = calloc(height + 1, sizeof(char));
        memcpy(tree->code, curCode, sizeof(char) * height);
    }
    if (tree->left) {
        curCode[height] = '0';
        GenerateCodeElements(tree->left, curCode, height + 1);
    }
    if (tree->right) {
        curCode[height] = '1';
        GenerateCodeElements(tree->right, curCode, height + 1);
    }
}

/* Создание таблицы кодировок всех символов, для обращение O(1) */
void EditCodeTable(TTree* tree, TTree* codeTable, int size) {
    if (CheckIfLowest(tree)) {
        int index = tree->value;
        int len = strlen((char*)tree->code);
        codeTable[index].code = malloc(sizeof(char) * len + 1);
        memcpy(codeTable[index].code, tree->code, len + 1);
    }
    if (tree->left)
        EditCodeTable(tree->left, codeTable, size);
    if (tree->right)
        EditCodeTable(tree->right, codeTable, size);
}

/* Вывод байта */
void PrintByte(char* byte, int* index) {
    fprintf(fOut, "%c", *byte);
    *byte = 0;
    *index = 0;
}

void FillWithOne(char* byte, int* index) {
    *byte |= (1 << (8 - *index - 1));
    *index += 1;
}

/* Заполнение байта кодом символа */
void UppendByte(char* byte, int* index, unsigned char sym, TTree* table) {
    for (unsigned int i = 0; i < strlen(table[(int)sym].code); i++) {
        if (*index == 8)
            PrintByte(byte, index);
        if (table[(int)sym].code[i] == '1')
            *byte |= (1 << (8 - *index - 1));
        *index += 1;
    }
}

/* Заполнение байта символом(Случай в дереве) */
void UppendTreeByte(char* byte, int* index, unsigned char sym) {    
    for (int i = 0; i < 8; i++) {
        if (*index == 8)
            PrintByte(byte, index);
        if (sym & (1 << (8 - i - 1)))
            *byte |= (1 << (8 - *index - 1));
        *index += 1;
    }
    if (*index == 8)
        PrintByte(byte, index);
}

/* Кодировка дерева */
void PrintZipTree(TTree* tree, char* byte, int* index) {
    if (*index == 8)
        PrintByte(byte, index);
    
    if (tree->left) {
        FillWithOne(byte, index);
        PrintZipTree(tree->left, byte, index);
    }
    
    if (tree->right) {
        FillWithOne(byte, index);
        PrintZipTree(tree->right, byte, index);
    }
     
    if (CheckIfLowest(tree)) {
        *index += 1;
        UppendTreeByte(byte, index, tree->value);
    }
}

/* Кодировка сообщения */
void PrintZip(TTree* table, char* byte, int* index) {
    char sym = fgetc(fIn);
    while (!feof(fIn)) {
        UppendByte(byte, index, sym, table);
        sym = fgetc(fIn);
    }
    PrintByte(byte, index);
}

typedef struct QueuePriority {
    TTree* element;
    struct QueuePriority* next;
} TQueue;

TQueue* PushElem(TQueue* root, TTree* tree) {
    if (root == NULL) {
        root = malloc(sizeof(TQueue));
        root->element = tree;
        root->next = NULL;
    }
    else {
        if (root->element->count < tree->count) {
            root->next = PushElem(root->next, tree);
        }
        else {
            TQueue* newElem = malloc(sizeof(TQueue));
            newElem->element = tree;
            newElem->next = root;
            root = newElem;
        }
    }
    return root;
}

TTree* PopElem(TQueue** root) {
    if (root == NULL)
        return NULL;
    TTree* subTree = (*root)->element;
    TQueue* tempFree = (*root)->next;
    free(*root);
    *root = tempFree;
    return subTree;
}

TQueue* GetQueue(TTree* table, int size){
    TQueue* resTable = NULL;
    for (int i = 0; i < size; i++)
        if (table[i].count != 0)
            resTable = PushElem(resTable, &table[i]);
    return resTable;
}

/* Подсчитывает количество незначимых нулей */
int CalcOst(TTree* codeTable) {
    int ost = 0;
    int counter = 0;
    for (int i = 0; i < SIZEASCII; i++) {
        if (codeTable[i].count != 0) {
            ost += (codeTable[i].count * strlen(codeTable[i].code));
            ost %= 8;
            counter += 1;
        }
    }

    ost += 3*(counter - 1) + 1; //Универсальная формула нахождение длины закодированного дерева
    ost += 1;
    ost %= 8;

    ost = (8 - ost) % 8;
    return ost;
}

char CheckCoding(TTree* table) {
    for (int i = 1; i < SIZEASCII; i++)
        if (table[i-1].count != table[i].count)
            return 0;
    return 1;
}

void Encode() {
    unsigned char sym = fgetc(fIn);
    if (feof(fIn)) { //Проверка на пустой файл
        return;
    }
    TTree* tableCode = calloc(SIZEASCII, sizeof(TTree));
    while (!feof(fIn)) {
        tableCode[(int)sym].value = sym;
        tableCode[(int)sym].count += 1;
        sym = fgetc(fIn);
    }

    if (CheckCoding(tableCode)) { //Проверка на необходимость кодировки
        fseek(fIn, 1L, SEEK_SET);
        char sym = fgetc(fIn);
        while (!feof(fIn)){
            fprintf(fOut, "%c", sym);
            sym = fgetc(fIn);
        }
        free(tableCode);
        return;
    }

    TQueue* elems = GetQueue(tableCode, SIZEASCII);

    while (elems->next != NULL) { 
        TTree* sub1 = PopElem(&elems);
        TTree* sub2 = PopElem(&elems);
        TTree* newTree = malloc(sizeof(TTree));
        newTree->code = NULL;
        newTree->count = sub1->count + sub2->count;
        newTree->value = 0;
        newTree->left = sub1;
        newTree->right = sub2;
        elems = PushElem(elems, newTree);
    }

    TTree* tree = elems->element;
    if (CheckIfLowest(tree)) { //Проверка на присутстиве единственного элемента
        tree->code = calloc(2, sizeof(char));
        *tree->code = '0';
    }
    else {
        char codeHelp[SIZEASCII];
        GenerateCodeElements(tree, codeHelp, 0);
    }

    char byte = 0;
    int index = CalcOst(tableCode);
    fseek(fIn, 1L, SEEK_SET);
    FillWithOne(&byte, &index);
    if (index == 8) PrintByte(&byte, &index);
    PrintZipTree(tree, &byte, &index);
    PrintZip(tableCode, &byte, &index);

    FreeEncodingTree(tree);
    free(elems);
    free(tableCode);
}

int Pow(int a, int b) {
    int res = 1;
    for (int i = 1; i <= b; i++)
        res *= a;
    return res;
}

/* Счёт символа */
unsigned char DependByte (char* byte, int* index) {
    unsigned char res = 0;
    for (int i = 0; i < 8; i++) {
        if (*index == 8) {
            *byte = fgetc(fIn);
            *index = 0;
        }

        if (*byte & (1 << (8 - *index - 1))) {
            res += Pow(2, 8 - i - 1);
        }
        *index += 1;
    }

    if (*index == 8) {
        *byte = fgetc(fIn);
        *index = 0;
    }
    return res;
}

/* Создание дерева */
void MakeTree(TTree** tree, char* byte, int* index) {
    if ((*tree) == NULL) {
        (*tree) = malloc(sizeof(TTree));
        (*tree)->left = NULL;
        (*tree)->right = NULL;
        (*tree)->value = 0;
        (*tree)->count = 0;
    }
    if (*index == 8) {
        *byte = fgetc(fIn);
        *index = 0;
    }

    if (*byte & (1 << (8 - *index - 1))) {
        if ((*tree)->left == NULL) {
            *index += 1;
            MakeTree(&((*tree)->left), byte, index);
        }
        if ((*tree)->right == NULL) {
            *index += 1;
            MakeTree(&((*tree)->right), byte, index);
        }
    }
    else {
        *index += 1;
        (*tree)->value = DependByte(byte, index);
    }
}

void RunDecoding(TTree* tree, char* byte, int* index) {
    TTree* temp = tree;
    while (!feof(fIn)){
        if (*index == 8) {
            *byte = fgetc(fIn);
            *index = 0;
            if (feof(fIn)) break;
        }
        if (*byte & (1 << (8 - *index - 1))) {
            temp = temp->right;
            *index+=1;
        }
        else if (temp->left){
            temp = temp->left;
            *index += 1;
        }
        if (CheckIfLowest(temp)) {
            fprintf(fOut, "%c", temp->value);
            if (tree->left == NULL && tree->right == NULL)
                *index += 1;
            temp = tree;
        }
    }
}

void Decode() {
    char byte = fgetc(fIn);
    if (feof(fIn)) return;
    int index = 0;
    while (!(byte & (1 << (8 - index - 1)))) {
        index += 1;
        if (index == 8) { //Проверка на наличие кодировки
            fprintf(fOut, "%c", byte);
            char sym = fgetc(fIn);
            while (!feof(fIn)) {
                fprintf(fOut, "%c", sym);
                sym = fgetc(fIn);
            }
            return;
        }
    }
    index += 1;
    if (index == 8) {
        byte = fgetc(fIn);
        index = 0;
    }
    TTree* tree = NULL;
    if (byte & (1 << (8 - index - 1))) //Проверка на единственность элемента
        MakeTree(&tree, &byte, &index);
    else {
        tree = malloc(sizeof(TTree));
        index += 1;
        tree->value = DependByte(&byte, &index);
        tree->left = NULL;
        tree->right = NULL;
        tree->count = 0;
    }

    RunDecoding(tree, &byte, &index);
    FreeDecodingTree(tree);
}

int main() { //Huffman
    fIn = fopen("in.txt", "rb");
    fOut = fopen("out.txt", "wb");

    char status = fgetc(fIn);
    if (status == 'c')
        Encode();
    else if (status == 'd')
        Decode();
    
    fclose(fIn);
    fclose(fOut);
    return EXIT_SUCCESS;
}
