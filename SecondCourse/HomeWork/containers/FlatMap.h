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

    FlatMap(const FlatMap&);
    FlatMap(FlatMap&&);

    // Обменивает значения двух флетмап.
    void swap(FlatMap&);

    FlatMap& operator=(const FlatMap&);
    FlatMap& operator=(FlatMap&&);


    // Очищает контейнер.
    void clear();
    // Удаляет элемент по заданному ключу.
    bool erase(const Key&);
    // Вставка в контейнер. Возвращаемое значение - успешность вставки.
    bool insert(const Key&, const Value&);

    // Проверка наличия значения по заданному ключу.
    bool contains(const Key&) const;

    // Возвращает значение по ключу. Небезопасный метод.
    // В случае отсутствия ключа в контейнере, следует вставить в контейнер
    // значение, созданное конструктором по умолчанию и вернуть ссылку на него. 
    Value& operator[](const Key&);

    // Возвращает значение по ключу. Бросает исключение при неудаче.
    Value& at(const Key&);
    const Value& at(const Key&) const;

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

    void ReallocArray(size_t);
};

#endif