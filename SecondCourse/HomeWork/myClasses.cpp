#include "myClasses.h"

namespace myCls {
    Array::Array(size_t initial_capacity) 
    {
        capacity = initial_capacity;
        arr = new int[capacity];
    }

    Array::Array(const Array & oldArray) : Array(oldArray.capacity) 
    {
        memcpy(arr, oldArray.arr, oldArray.capacity * sizeof(*oldArray.arr));
        sizeArray = oldArray.sizeArray;
    }

    Array::~Array()
    {
        delete[] arr;
    }

    Array & Array::operator=(const Array & oldArray)
    {
        sizeArray = oldArray.sizeArray;
        capacity = oldArray.capacity;
        arr = new int[capacity];
        for (int i = 0; i < sizeArray; ++i) {
            arr[i] = oldArray.arr[i];
        }
        return *this;
    }

    int Array::operator[](const size_t index)
    {
        return arr[index];
    }

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

    void Array::insert(size_t index, int value) {
        if (index > sizeArray) return;

        int temp = arr[index];
        arr[index] = value;
        for (size_t i = index + 1; i < sizeArray; i++) {
            std::swap(temp, arr[i]);
        }
        push_back(temp);
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

    int Array::pop(size_t index) {
        if (index >= sizeArray) return 0;
        int temp = arr[index];
        size_t beginner = index;
        while (beginner != sizeArray) {
            arr[beginner] = arr[beginner + 1];
            beginner++;
        }
        return temp;
    }

    bool Array::pop_value(int value, size_t count) { //if count == 0 : pop all value
        if (sizeArray == 0) return 0;
        int* tempArr = new int[sizeArray];
        memcpy(tempArr, arr, sizeArray * sizeof(*arr));
        size_t counter = 0;
        for (counter; (counter < count) || (count == 0); ++counter) {
            size_t index;
            bool flag = 0;
            for (int i = 0; i < sizeArray; ++i) {
                if (tempArr[i] == value) {
                    index = i;
                    flag = 1;
                    break;
                }
            }
            if (flag == 0 && count == 0) break;
            if (flag == 0) {
                delete[] tempArr;
                return 0;
            }

            for (index; index < sizeArray; ++index) {
                tempArr[index] = tempArr[index + 1];
            }
        }
        sizeArray -= counter;
        memcpy(arr, tempArr, sizeArray * sizeof(*arr));
        delete[] tempArr;
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