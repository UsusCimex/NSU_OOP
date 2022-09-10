#include <stdlib.h>
#include <iostream>

using namespace std;

class Array {
public:
    //Конструкторы
    Array(size_t initial_capacity = 1) {
        capacity = initial_capacity;
        arr = (int*) malloc(capacity * sizeof(*arr));
    }

    //Деструктор
    ~Array() {
        free(arr);
    }

    //Конструктор копирования
    Array(const Array & oldArr) : Array(oldArr.sizeArray) {
        memcpy(arr, oldArr.arr, oldArr.sizeArray * sizeof(*oldArr.arr));
        sizeArray = oldArr.sizeArray;
    }

    //Методы
    void push_back(int X) {
        if (sizeArray == capacity) {
            capacity *= 2;
            int* tmp = (int*)realloc(arr, capacity * sizeof(*arr));

            if (tmp == NULL) {
                cout << "Realloc error!.." << endl;
                return;
            }

            arr = tmp;
        }
        arr[sizeArray++] = X;
    }

    int at(int index) {
        if (index >= sizeArray) {
            cout << "index > size array. Error code: ";
            return 0;
        }
        return arr[index];
    }

    size_t size() {
        return sizeArray;
    }

    int pop_back() {
        if (sizeArray == 0) {
            cout << "Array is empty" << endl;
            return 0;
        }
        return arr[--sizeArray];
    }

    int* find(int val) {
        for (int i = 0; i < sizeArray; ++i) {
            if (arr[i] == val) {
                return &arr[i];
            }
        }
        return NULL;
    }

    bool pop(int val) {
        auto temp = find(val);
        if (temp == NULL) return 0;
        while (*temp != NULL) {
            *temp = *(temp + 1);
            temp++;
        }
        sizeArray -= 1;
        return 1;
    }

    bool pop(int* val) {
        if (val == NULL) return 0;
        while (*val != NULL) {
            *val = *(val + 1);
            val++;
        }
        sizeArray -= 1;
        return 1;
    }

    void sort(bool reverse = 0) { //Bad sort, I know)
        for (int i = 0; i < sizeArray; ++i) {
            for (int j = i + 1; j < sizeArray; ++j) {
                if ((arr[i] >= arr[j] && !reverse) || (arr[i] <= arr[j] && reverse)) {
                    swap(&arr[i], &arr[j]);
                }
            }
        }
    }

    bool empty() {
        if (sizeArray == 0) return 0;
        return 1;
    }

private:
    //Данные
    int* arr = nullptr;
    size_t sizeArray = 0ull;
    size_t capacity = 0ull;

    void swap(int* a, int* b) {
        int c = *a;
        *a = *b;
        *b = c;
    }
};

void PrintArray(Array arr) {
    for (int i = 0; i < arr.size(); ++i) {
        cout << arr.at(i) << " ";
    }
    cout << endl;
}

int main() {
    Array myArr(5);

    myArr.push_back(32);
    myArr.push_back(22);
    myArr.push_back(22);
    myArr.push_back(21);
    myArr.push_back(40);

    myArr.sort();
    myArr.push_back(25);
    myArr.sort();

    cout << myArr.pop_back() << endl;
    cout << myArr.pop_back() << endl;

    myArr.push_back(39);
    myArr.sort();

    PrintArray(myArr);
    auto el = myArr.find(21);
    myArr.pop(el);
    PrintArray(myArr);
    myArr.pop(22);
    PrintArray(myArr);

    myArr.push_back(204);
    myArr.sort();

    PrintArray(myArr);
    return 0;
}