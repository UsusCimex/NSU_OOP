#include <stdio.h>
#include <stdlib.h>

void QuickSort(int *arr, int first, int last)
{
    if (first < last)
    {
        int left = first, right = last, middle = arr[(left + right) / 2];
        while (left <= right) {
            while (arr[left] < middle) left++;
            while (arr[right] > middle) right--;
            if (left <= right)
            {
                int tmp = arr[left];
                arr[left] = arr[right];
                arr[right] = tmp;
                left++;
                right--;
            }
        }
        QuickSort(arr, first, right);
        QuickSort(arr, left, last);
    }
}

int main()
{
    int size;
    if (scanf("%d", &size) != 1) exit(0);

    int* arr = malloc(sizeof(int) * size);
    for (int i = 0; i < size; i++)
    {
        if (scanf("%d", &arr[i]) != 1) exit(0);
    }

    QuickSort(arr, 0, size-1);
    for (int i = 0; i < size; i++)
    {
        printf("%d ", arr[i]);
    }
    free(arr);
}
