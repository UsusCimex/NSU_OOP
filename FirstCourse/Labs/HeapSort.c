#include <stdio.h>
#include <stdlib.h>

void Swap(int* a, int* b) {
    int tmp = *a;
    *a = *b;
    *b = tmp;
}

void ToDown(int *arr, int this, int latest)
{
    int maxChild;
    int done = 0;
    while ((this * 2 <= latest) && (!done)) 
    {
        if (this * 2 == latest) maxChild = this * 2;
        else if (arr[this * 2] > arr[this * 2 + 1]) maxChild = this * 2;
        else maxChild = this * 2 + 1;
        if (arr[this] < arr[maxChild]) {
            Swap(&arr[this], &arr[maxChild]);
            this = maxChild;
        }
        else done = 1;
    }
}

void HeapSort(int *arr, int array_size) 
{
    for (int i = (array_size / 2); i >= 0; i--)
        ToDown(arr, i, array_size - 1);
    for (int i = array_size - 1; i >= 1; i--) {
        Swap(&arr[0], &arr[i]);
        ToDown(arr, 0, i - 1);
    }
}

int main(void) {
    int size;
    if (scanf("%d", &size) != 1) exit(0);
    
    int* arr = malloc(sizeof(int) * size);
    for (int i = 0; i < size; i++) {
        if(scanf("%d", &arr[i]) != 1) exit(0);
    }

    HeapSort(arr, size);
    for (int i = 0; i < size; i++) {
        printf("%d ", arr[i]);
    }
    free(arr);
    return EXIT_SUCCESS;
}
