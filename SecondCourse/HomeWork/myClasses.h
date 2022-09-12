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

        Array & operator=(const Array & oldArray); //Операторы
        int operator[](const size_t index);
        void push_back(int value); //Методы
        void insert(size_t index, int value);
        void sort(bool reverse = false);
        size_t size();
        int at(size_t index);
        int pop_back();
        int pop(size_t index);
        bool pop_value(int value, size_t count = 0);
        int* find(int value);
        bool empty();
    };
}

#endif