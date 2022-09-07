#include <stdio.h>
#include <stdlib.h>

FILE* fIn;
FILE* fOut;

typedef struct Memory {
    int numbOperation;
    int address;
    int memCount;
    struct Memory* next;
} Memory;

typedef struct Heap {
    Memory* emptyMemory;
    Memory* filledMemory;
} Heap;

Heap* heap;

/* Поиск ячейки памяти по номеру операции(заполненная) */
Memory* SearchMemory(int numbOperation) {
    Memory* search = heap->filledMemory;
    while (search) {
        if (search->numbOperation == numbOperation)
            return search;
        search = search->next;
    }
    return NULL;
}

/* Очистка памяти */
void ClearMemory(int addres) {
    Memory* search = heap->filledMemory;
    if (search->address == addres) {
        Memory* temp = search->next;
        free(heap->filledMemory);
        heap->filledMemory = temp;
    }
    else {
        while (search->next) {
            if (search->next->address == addres) {
                Memory* temp = search->next->next;
                free(search->next);
                search->next = temp;
                return;
            }
            search = search->next;
        }
    }
}

/* Сортировка всего свободного пространства */
void SortFreeMemory() {
    Memory* editor = heap->emptyMemory;
    if (editor == NULL) return;
    while (editor->next) {
        if (editor->memCount < editor->next->memCount) {
            Memory temp = *editor->next;
            editor->next->address = editor->address;
            editor->next->memCount = editor->memCount;
            editor->next->numbOperation = editor->numbOperation;
            editor->address = temp.address;
            editor->memCount = temp.memCount;
            editor->numbOperation = temp.numbOperation;
            SortFreeMemory();
        }
        if (editor->memCount == editor->next->memCount) {
            if (editor->address > editor->next->address) {
                Memory temp = *editor->next;
                editor->next->address = editor->address;
                editor->next->memCount = editor->memCount;
                editor->next->numbOperation = editor->numbOperation;
                editor->address = temp.address;
                editor->memCount = temp.memCount;
                editor->numbOperation = temp.numbOperation;
            }
        }
        editor = editor->next;
    }
}

/* Заполнение пустой ячейки */
void FillingMemory(int address, int countMemory) {
    Memory* search = heap->emptyMemory;
    if (search->address == address) {
        if (search->memCount == countMemory) {
            Memory* temp = search->next;
            free(heap->emptyMemory);
            heap->emptyMemory = temp;
            search = heap->emptyMemory;
        }
        else {
            heap->emptyMemory->memCount -= countMemory;
            heap->emptyMemory->address += countMemory;
        }
    }
    else {
        while (search->next) {
            if (search->next->address == address) {
                if (search->next->memCount == countMemory) {
                    Memory* temp = search->next->next;
                    free(search->next);
                    search->next = temp;
                }
                else {
                    search->next->memCount -= countMemory;
                    search->next->address += countMemory;
                }
                break;
            }
            search = search->next;
        }
    }
    SortFreeMemory();
}

/* Записать выделенную память */
void PushFillMemory(int address, Memory* memory) {
    Memory* search = heap->filledMemory;
    memory->next = NULL;

    if (search == NULL) {
        heap->filledMemory = memory;
    }
    else {
        if (search->address >= address) {
            memory->next = heap->filledMemory;
            heap->filledMemory = memory;
        }
        else {
            while (search->next) {
                if (search->next->address >= address) {
                    memory->next = search->next;
                    search->next = memory;
                    return;
                }
                search = search->next;
            }
            search->next = memory;
        }
    }
}

/* Записать очищенную память */
void PushEmptyMemory(Memory* memory) {
    Memory* search = heap->emptyMemory;
    if (search == NULL) {
        memory->next = NULL;
        heap->emptyMemory = memory;
    }
    else {
        if (search->memCount < memory->memCount) {
            memory->next = search;
            heap->emptyMemory = memory;
        }
        else {
            while (search->next) {
                if (search->next->memCount < memory->memCount) {
                    memory->next = search->next;
                    search->next = memory;
                    break;
                }
                search = search->next;
            }
            if (search->next == NULL) {
                memory->next = NULL;
                search->next = memory;
            }
        }
    }
}

/* Удаление ячейки с пустой памятью */
void PopNull(int addres) {
    Memory* search = heap->emptyMemory;
    if (search->address == addres) {
        Memory* temp = search->next;
        free(heap->emptyMemory);
        heap->emptyMemory = temp;
    }
    else {
        while (search->next) {
            if (search->next->address == addres) {
                Memory* temp = search->next->next;
                free(search->next);
                search->next = temp;
                return;
            }
            search = search->next;
        }
    }
}

/* Объединяем очищенную память */
int UnionFreeMemory(int address, int memCount) {
    Memory* search = heap->emptyMemory;
    while (search) {
        if (search->address == address + memCount) {
            search->address -= memCount;
            search->memCount += memCount;
            if (UnionFreeMemory(search->address, search->memCount)) {
                PopNull(search->address);
            }
            return 1;
        }
        if (search->address + search->memCount == address) {
            search->memCount += memCount;
            if (UnionFreeMemory(search->address, search->memCount)) {
                PopNull(search->address);
            }
            return 1;
        }
        search = search->next;
    }
    return 0;
}

int FMalloc(int memoryCount, int numberOperation) {
    Memory* byFilled = heap->emptyMemory;
    if (byFilled == NULL) return -1;
    if (byFilled->memCount < memoryCount) return -1;

    Memory* filled = malloc(sizeof(Memory));
    filled->address = byFilled->address;
    filled->memCount = memoryCount;
    filled->numbOperation = numberOperation;
    
    FillingMemory(byFilled->address, memoryCount);
    PushFillMemory(byFilled->address, filled);
    return filled->address;
}

void FFree(int opearion) {
    Memory* byEmpty = SearchMemory(opearion);
    if (byEmpty == NULL) return;

    Memory* empty = malloc(sizeof(Memory));
    empty->address = byEmpty->address;
    empty->memCount = byEmpty->memCount;
    empty->numbOperation = byEmpty->numbOperation;

    ClearMemory(byEmpty->address);
    if (!UnionFreeMemory(empty->address, empty->memCount))
        PushEmptyMemory(empty);
    SortFreeMemory();
}

void FreeMemory(Memory* mem) {
    if (mem == NULL) return;
    FreeMemory(mem->next);
    free(mem);
}

void FreeHeap() {
    FreeMemory(heap->emptyMemory);
    FreeMemory(heap->filledMemory);
    free(heap);
}

void GenerateHeap(int memoryCount) {
    heap = malloc(sizeof(Heap));
    heap->emptyMemory = malloc(sizeof(Memory));
    heap->emptyMemory->address = 1;
    heap->emptyMemory->memCount = memoryCount;
    heap->emptyMemory->numbOperation = 0;
    heap->emptyMemory->next = NULL;
    heap->filledMemory = NULL;
}

int main() {
    fIn = fopen("input.txt", "r");
    fOut = fopen("output.txt", "w");

    int N, M;
    if (fscanf(fIn, "%d %d", &N, &M) != 2) 
        return 0;

    GenerateHeap(N);
    int counter = 0;
    for (int i = 0; i < M; i++) {
        int operation;
        if (fscanf(fIn, "%d", &operation) != 1)
            return 0;
        if (operation >= 0) {
            if (counter == 497)
                counter+=1;
            fprintf(fOut, "%d\n", FMalloc(operation, i + 1));
            counter += 1;
        }
        else
            FFree(-operation);
    }

    FreeHeap();
    fclose(fIn);
    fclose(fOut);
    return 0;
}
