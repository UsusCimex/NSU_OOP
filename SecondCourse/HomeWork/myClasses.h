#include <iostream>
#include <cstdlib>

#ifndef MYCLASS_H
#define MYCLASS_H

namespace myCls
{
    class Array
    {
    private:
        int* arr = nullptr; //Данные/date
        size_t sizeArray = 0ull;
        size_t capacity = 0ull;
    public:
        Array(size_t initial_capacity = 1); //Конструктор
        Array(const Array & oldArray); //Конструктор копирования
        ~Array(); //Деструктор

        Array & operator=(const Array & oldArray); //Оператор
        void push_back(int value); //Методы
        void sort(bool reverse = false);
        size_t size();
        int at(size_t index);
        int pop_back();
        int* find(int value);
        bool pop(int value);
        bool pop(int* value);
        bool empty();
    };
}

#endif