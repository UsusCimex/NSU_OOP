#include "myClasses.h"

namespace myCls {
    Array::Array(size_t initial_capacity) 
    {
        capacity = initial_capacity;
        arr = new int[capacity];
    }

    Array::Array(const Array & oldArray) : Array(oldArray.sizeArray) 
    {
        memcpy(arr, oldArray.arr, oldArray.sizeArray * sizeof(*oldArray.arr));
        sizeArray = oldArray.sizeArray;
    }

    Array::~Array()
    {
        delete[] arr;
    }

    // Array & Array::operator=(const Array & oldArray) 
    // {
    //     //
    // }

    void Array::push_back(int value) {
        if (sizeArray == capacity) {
            capacity *= 2;
            int* tmp = (int*)realloc(arr, capacity * sizeof(*arr));

            if (tmp == NULL) {
                std::cout << "Realloc error!.." << std::endl;
                return;
            }

            arr = tmp;
        }
        arr[sizeArray++] = value;
    }

    int Array::at(size_t index) {
        if (index >= sizeArray) {
            std::cout << "index > size array. Error code: ";
            return 0;
        }
        return arr[index];
    }

    size_t Array::size() {
        return sizeArray;
    }

    int Array::pop_back() {
        if (sizeArray == 0) {
            std::cout << "Array is empty" << std::endl;
            return 0;
        }
        return arr[--sizeArray];
    }

    int* Array::find(int value) {
        for (int i = 0; i < sizeArray; ++i) {
            if (arr[i] == value) {
                return &arr[i];
            }
        }
        return NULL;
    }

    bool Array::pop(int value) {
        auto temp = find(value);
        if (temp == &arr[sizeArray-1]) return 0;
        while (temp != &arr[sizeArray-1]) {
            *temp = *(temp + 1);
            temp++;
        }
        sizeArray -= 1;
        return 1;
    }

    bool Array::pop(int* value) {
        if (value == &arr[sizeArray-1]) return 0;
        while (value != &arr[sizeArray-1]) {
            *value = *(value + 1);
            value++;
        }
        sizeArray -= 1;
        return 1;
    }

    void Array::sort(bool reverse) {
        for (int i = 0; i < sizeArray; ++i) {
            for (int j = i + 1; j < sizeArray; ++j) {
                if ((arr[i] >= arr[j] && !reverse) || (arr[i] <= arr[j] && reverse)) {
                    std::swap(arr[i], arr[j]);
                }
            }
        }
    }

    bool Array::empty() {
        if (sizeArray == 0) return 0;
        return 1;
    }
}