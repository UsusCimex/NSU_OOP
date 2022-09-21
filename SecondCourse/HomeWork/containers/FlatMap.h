#include <string>
#include <cstdlib>
#include <iostream>

#ifndef FLATMAP_H
#define FLATMAP_H

typedef std::string Key;
typedef int Value;

class FlatMap
{
public:
    FlatMap();
    ~FlatMap();

    FlatMap(const FlatMap& b);
    FlatMap(FlatMap&& b);

    // Обменивает значения двух флетмап.
    void swap(FlatMap& b);

    FlatMap& operator=(const FlatMap& b);
    FlatMap& operator=(FlatMap&& b);


    // Очищает контейнер.
    void clear();
    // Удаляет элемент по заданному ключу.
    bool erase(const Key& k);
    // Вставка в контейнер. Возвращаемое значение - успешность вставки.
    bool insert(const Key& k, const Value& v);

    // Проверка наличия значения по заданному ключу.
    bool contains(const Key& k) const;

    // Возвращает значение по ключу. Небезопасный метод.
    // В случае отсутствия ключа в контейнере, следует вставить в контейнер
    // значение, созданное конструктором по умолчанию и вернуть ссылку на него. 
    Value& operator[](const Key& k);

    // Возвращает значение по ключу. Бросает исключение при неудаче.
    Value& at(const Key& k);
    const Value& at(const Key& k) const;

    size_t size() const;
    bool empty() const;

    friend bool operator==(const FlatMap& a, const FlatMap& b)
    {
        if (a.sizeArray != b.sizeArray) 
        {
            return 0;
        }

        for (size_t i = 0; i < a.sizeArray; ++i)
        {
            if (a.key[i] != b.key[i] || a.value[i] != b.value[i])
            {
                return 0;
            }
        }

        return 1;
    }
    friend bool operator!=(const FlatMap& a, const FlatMap& b)
    {
        return !(a == b);
    }
private:
    size_t capacity = 0ull;
    size_t sizeArray = 0ull;
    Key* key = nullptr;
    Value* value = nullptr;

    void ReallocArray(size_t newSize);
};

#endif