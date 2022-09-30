#ifndef FLATMAP_H
#define FLATMAP_H

#define DEFAULTSIZE 1

template <class Key, class Value>
class FlatMap
{
public:
    FlatMap() : capacity(DEFAULTSIZE)
    {
        key = new Key[capacity];
        value = new Value[capacity];
    }
    ~FlatMap()
    {
        delete[] key;
        delete[] value;
    }
    
    FlatMap(const FlatMap& b) : capacity(b.capacity), sizeArray(b.sizeArray)
    {
        key = new Key[capacity];
        value = new Value[capacity];
        std::copy(b.key, b.key + sizeArray, key);
        std::copy(b.value, b.value + sizeArray, value);
    }
    FlatMap(FlatMap&& b) : capacity(b.capacity), sizeArray(b.sizeArray)
    {
        key = b.key;
        value = b.value;

        b.key = nullptr;
        b.value = nullptr;
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
        if (&b == this) return *this;
        capacity = b.capacity;
        sizeArray = b.sizeArray;
        key = new Key[capacity];
        value = new Value[capacity];
        
        std::copy(b.key, b.key + sizeArray, key);
        std::copy(b.value, b.value + sizeArray, value);

        return *this;
    }
    FlatMap& operator=(FlatMap&& b)
    {
        if (b == *this) return *this;
        
        capacity = b.capacity;
        sizeArray = b.sizeArray;

        key = b.key;
        value = b.value;

        b.key = nullptr;
        b.value = nullptr;

        return *this;
    }


    // Очищает контейнер.
    void clear()
    {
        if (sizeArray == 0) return;
    
        ReallocArray(DEFAULTSIZE);
        sizeArray = 0;
    }
    // Удаляет элемент по заданному ключу.
    bool erase(const Key& k)
    {
        if (sizeArray == 0) return false;
        size_t index = BinarySearch(k);
        if (key[index] != k) return false;

        std::copy(key + index + 1, key + sizeArray, key + index);
        std::copy(value + index + 1, value + sizeArray, value + index);
        sizeArray--;

        if (sizeArray < capacity / 2) //optimise memory
        {
            ReallocArray(capacity / 2);
        }

        return true;
    }
    // Вставка в контейнер. Возвращаемое значение - успешность вставки.
    bool insert(const Key& k, const Value& v)
    {
        size_t index = BinarySearch(k);
        if (key[index] == k) return false; //mb old value needs to be replaced with a new one

        if (sizeArray == capacity)
        {
            ReallocArray(capacity * 2);
        }
        
        Key tempKey = k;
        Value tempValue = v;
        for (size_t i = index; i <= sizeArray; ++i)
        {
            std::swap(key[i], tempKey);
            std::swap(value[i], tempValue);
        }
        sizeArray++;
        return true;
    }

    // Проверка наличия значения по заданному ключу.
    bool contains(const Key& k) const
    {
        size_t index = BinarySearch(k);
        if (key[index] == k) return true;
        return false;
    }

    // Возвращает значение по ключу. Небезопасный метод.
    // В случае отсутствия ключа в контейнере, следует вставить в контейнер
    // значение, созданное конструктором по умолчанию и вернуть ссылку на него. 
    Value& operator[](const Key& k)
    {
        size_t index = BinarySearch(k);
        if (key[index] == k) return value[index];

        if (sizeArray == capacity)
        {
            ReallocArray(capacity * 2);
        }
        
        Key tempKey = k;
        Value tempValue = 0;
        for (size_t i = index; i <= sizeArray; ++i)
        {
            std::swap(key[i], tempKey);
            std::swap(value[i], tempValue);
        }
        sizeArray++;

        return value[index];
    }

    // Возвращает значение по ключу. Бросает исключение при неудаче.
    Value& at(const Key& k)
    {
        size_t index = BinarySearch(k);
        if (key[index] == k) return value[index];
        else throw std::out_of_range("Key isn't available");
    }
    const Value& at(const Key& k) const
    {
        size_t index = BinarySearch(k);
        if (key[index] == k) return value[index];
        else throw std::out_of_range("Key isn't available");
    }

    size_t size() const
    {
        return sizeArray;
    }
    bool empty() const
    {
        if (sizeArray == 0) return true;
        return false;
    }

    friend bool operator==(const FlatMap& a, const FlatMap& b)
    {
        if (a.sizeArray != b.sizeArray) 
        {
            return false;
        }

        for (size_t i = 0; i < a.sizeArray; ++i)
        {
            if (a.key[i] != b.key[i] || a.value[i] != b.value[i])
            {
                return false;
            }
        }

        return true;
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
            sizeArray = std::min(sizeArray, newSize);
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

    size_t BinarySearch(const Key k) const
    {
        if (sizeArray == 0) return 0;
        if (k == key[0]) return 0;
        if (k == key[sizeArray - 1]) return sizeArray - 1;

        size_t r = sizeArray - 1;
        size_t l = 0;
        size_t mid = 0;
        while (l < r)
        {
            mid = l + ((r - l) / 2);
            if (k == key[mid]) return mid;
            else if (k > key[mid]) l = mid + 1;
            else r = mid;
        }
        
        return l;
    }
};

#endif