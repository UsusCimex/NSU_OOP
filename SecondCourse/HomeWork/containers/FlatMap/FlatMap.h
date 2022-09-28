#ifndef FLATMAP_H
#define FLATMAP_H

#include <string>
#include <cstdlib>
#include <iostream>

template <class Key, class Value>
class FlatMap
{
public:
    FlatMap() : capacity(1)
    {
        key = new Key[capacity];
        value = new Value[capacity];
    }
    ~FlatMap()
    {
        delete[] key;
        delete[] value;
    }
    
    FlatMap(const FlatMap& b) : capacity(b.capacity)
    {
        key = new Key[capacity];
        value = new Value[capacity];
        for (size_t i = 0; i < b.sizeArray; i++)
        {
            key[i] = b.key[i];
            value[i] = b.value[i];
        }
    }
    FlatMap(FlatMap&& b) : capacity(b.capacity)
    {
        key = std::move(b.key);
        value = std::move(b.value);
        capacity = b.capacity;
        sizeArray = b.sizeArray;
        
        b.capacity = 0ull;
        b.sizeArray = 0ull;
    }

    // Обменивает значения двух флетмап.
    void swap(FlatMap& b)
    {
        auto temp = std::move(b);
        b = std::move(*this);
        *this = std::move(temp);
    }

    FlatMap& operator=(const FlatMap& b)
    {
        if (b == *this) return *this;
        capacity = b.capacity;
        sizeArray = b.sizeArray;
        key = new Key[capacity];
        value = new Value[capacity];
        
        for (size_t i = 0; i < sizeArray; ++i)
        {
            key[i] = b.key[i];
            value[i] = b.value[i];
        }

        return *this;
    }
    FlatMap& operator=(FlatMap&& b)
    {
        if (b == *this) return *this;
        
        capacity = b.capacity;
        sizeArray = b.sizeArray;
        b.capacity = 0ull;
        b.sizeArray = 0ull;

        key = std::move(b.key);
        value = std::move(b.value);

        return *this;
    }


    // Очищает контейнер.
    void clear()
    {
        if (sizeArray == 0) return;
    
        ReallocArray(1);
        sizeArray = 0;
    }
    // Удаляет элемент по заданному ключу.
    bool erase(const Key& k)
    {
         if (sizeArray == 0)
        {
            return 0;
        }

        size_t r = sizeArray;
        size_t l = 0;
        size_t indexSearch = (r + l) / 2;
        while (r - l > 0) {
            if (key[indexSearch] == k)
            {
                for (size_t i = indexSearch; i < sizeArray; ++i)
                {
                    std::swap(key[i], key[i+1]);
                    std::swap(value[i], value[i+1]);
                }
                sizeArray--;

                if (sizeArray < capacity / 2) //optimise memory
                {
                    ReallocArray(capacity / 2);
                }

                return 1;
            }
            else if (key[indexSearch] > k)
            {
                if (r == indexSearch) indexSearch--;
                r = indexSearch;
            }
            else
            {
                if (l == indexSearch) indexSearch++;
                l = indexSearch;
            }
            indexSearch = (r + l) / 2;
        }
        return 0;
    }
    // Вставка в контейнер. Возвращаемое значение - успешность вставки.
    bool insert(const Key& k, const Value& v)
    {
        if (sizeArray == 0)
        {
            key[0] = k;
            value[0] = v;
            sizeArray += 1;
        }
        else
        {
            size_t r = sizeArray;
            size_t l = 0;
            size_t indexSearch = (r + l) / 2;
            while (r - l > 0) {
                if (key[indexSearch] == k)
                {
                    return 0; //mb old value needs to be replaced with a new one
                }
                else if (key[indexSearch] > k)
                {
                    if (r == indexSearch) indexSearch--;
                    r = indexSearch;
                }
                else
                {
                    if (l == indexSearch) indexSearch++;
                    l = indexSearch;
                }
                indexSearch = (r + l) / 2;
            }
            if (r == l)
            {
                if (sizeArray == capacity)
                {
                    ReallocArray(capacity * 2);
                }
                
                Key tempKey = k;
                Value tempValue = v;
                for (size_t i = indexSearch; i <= sizeArray; ++i)
                {
                    std::swap(key[i], tempKey);
                    std::swap(value[i], tempValue);
                }
                sizeArray++;
            }
        }
        return 1;
    }

    // Проверка наличия значения по заданному ключу.
    bool contains(const Key& k) const
    {
        if (sizeArray != 0)
        {
            size_t r = sizeArray;
            size_t l = 0;
            size_t indexSearch = (r + l) / 2;
            while (r - l > 0) {
                if (key[indexSearch] == k)
                {
                    return 1;
                }
                else if (key[indexSearch] > k)
                {
                    if (r == indexSearch) indexSearch--;
                    r = indexSearch;
                }
                else
                {
                    if (l == indexSearch) indexSearch++;
                    l = indexSearch;
                }
                indexSearch = (r + l) / 2;
            }
        }
        return 0;
    }

    // Возвращает значение по ключу. Небезопасный метод.
    // В случае отсутствия ключа в контейнере, следует вставить в контейнер
    // значение, созданное конструктором по умолчанию и вернуть ссылку на него. 
    Value& operator[](const Key& k)
    {
        size_t indexSearch = 0;
    
        if (sizeArray == 0)
        {
            key[0] = k;
            sizeArray += 1;
        }
        else
        {
            size_t r = sizeArray;
            size_t l = 0;
            indexSearch = (r + l) / 2;
            while (r - l > 0) {
                if (key[indexSearch] == k)
                {
                    return value[indexSearch];
                }
                else if (key[indexSearch] > k)
                {
                    if (r == indexSearch) indexSearch--;
                    r = indexSearch;
                }
                else
                {
                    if (l == indexSearch) indexSearch++;
                    l = indexSearch;
                }
                indexSearch = (r + l) / 2;
            }
            if (r == l)
            {
                if (sizeArray == capacity)
                {
                    ReallocArray(capacity*2);
                }
                
                Key tempKey = k;
                Value tempValue = 0;
                for (size_t i = indexSearch; i <= sizeArray; ++i)
                {
                    std::swap(key[i], tempKey);
                    std::swap(value[i], tempValue);
                }
                sizeArray++;
            }
        }
        return value[indexSearch];
    }

    // Возвращает значение по ключу. Бросает исключение при неудаче.
    Value& at(const Key& k)
    {
        size_t r = sizeArray;
        size_t l = 0;
        size_t indexSearch = (r + l) / 2;
        while (r - l > 0) {
            if (key[indexSearch] == k)
            {
                return value[indexSearch];
            }
            else if (key[indexSearch] > k)
            {
                if (r == indexSearch) indexSearch--;
                r = indexSearch;
            }
            else
            {
                if (l == indexSearch) indexSearch++;
                l = indexSearch;
            }
            indexSearch = (r + l) / 2;
        }
        if (r == l)
        {
            throw std::out_of_range("Key isn't available");
        }
        return value[indexSearch];
    }
    const Value& at(const Key& k) const
    {
        size_t r = sizeArray;
        size_t l = 0;
        size_t indexSearch = (r + l) / 2;
        while (r - l > 0) {
            if (key[indexSearch] == k)
            {
                return value[indexSearch];
            }
            else if (key[indexSearch] > k)
            {
                if (r == indexSearch) indexSearch--;
                r = indexSearch;
            }
            else
            {
                if (l == indexSearch) indexSearch++;
                l = indexSearch;
            }
            indexSearch = (r + l) / 2;
        }
        if (r == l)
        {
            throw std::out_of_range("Key isn't available");
        }
        return value[indexSearch];
    }

    size_t size() const
    {
        return sizeArray;
    }
    bool empty() const
    {
        if (sizeArray == 0) return 1;
        return 0;
    }

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

    void ReallocArray(size_t newSize)
    {
        if (newSize == capacity) return;

        Key* tempKey = new Key[newSize];
        Value* tempValue = new Value[newSize];

        if (newSize > capacity)
        {
            for (size_t i = 0; i < sizeArray; ++i)
            {
                tempKey[i] = key[i];
                tempValue[i] = value[i];
            }
        }
        else //lose some value
        {
            sizeArray = newSize;
            for (size_t i = 0; i < newSize; ++i)
            {
                tempKey[i] = key[i];
            }
        }

        delete[] key;
        delete[] value;

        key = tempKey;
        value = tempValue;

        capacity = newSize;
    }

};

#endif