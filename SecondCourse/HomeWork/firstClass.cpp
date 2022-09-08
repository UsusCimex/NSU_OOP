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
            return 1;
        }
        return arr[index];
    }

    size_t size() {
        return sizeArray;
    }

    int pop_back() {
        if (sizeArray == 0) {
            cout << "File is empty" << endl;
            return 0;
        }
        return arr[--sizeArray];
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
};

int main() {
    Array myArr(5);

    myArr.push_back(21);
    myArr.push_back(22);
    myArr.push_back(40);

    cout << myArr.size() << endl; //3
    cout << myArr.pop_back() << endl; //40
    cout << myArr.size() << endl; //2

    cout << myArr.at(2) << endl; //Error
    cout << myArr.at(1) << endl; //22
    return 0;
}