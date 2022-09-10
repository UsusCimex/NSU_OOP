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
            int* tmp = (int*)realloc(arr, capacity);

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
        return nullptr;
    }

    bool pop(int val) {
        auto temp = find(val);
        if (temp == nullptr) return 0;
        while (temp + 1 != nullptr) {
            *temp = *(temp + 1);
            temp++;
        }
        sizeArray -= 1;
        return 1;
    }

    bool pop(int* val) {
        if (val == nullptr) return 0;
        while (val + 1 != nullptr) {
            *val = *(val + 1);
            val++;
        }
        sizeArray -= 1;
        return 1;
    }

    void sort(bool reverse = 0) { //Bad sort, I know)
        for (int i = 0; i < sizeArray; ++i) {
            for (int j = i + 1; j < sizeArray; ++j) {
                if ((arr[i] <= arr[j] && !reverse) || (arr[i] >= arr[j] && reverse)) {
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

    cout << myArr.at(4) << endl;
    PrintArray(myArr);
    cout << myArr.at(4) << endl;

    cout << myArr.size() << endl; //5
    cout << myArr.pop_back() << endl; //40

    myArr.sort();

    cout << myArr.size() << endl; //4

    PrintArray(myArr); //Sorted array

    cout << myArr.at(4) << endl; //Error
    cout << myArr.at(3) << endl; //21

    PrintArray(myArr);
    return 0;
}