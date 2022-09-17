#include "FlatMap.h"

FlatMap::FlatMap() : capacity(1), sizeArray(0)
{
    key = new Key[capacity];
    key[0] = 0;
    value = new Value[capacity];
}
FlatMap::FlatMap(size_t initial_capacity) : capacity(initial_capacity), sizeArray(0)
{
    key = new Key[capacity];
    for (size_t i = 0; i < capacity; ++i) key[i] = 0;
    value = new Value[capacity];
}
FlatMap::~FlatMap()
{
    delete[] key;
    delete[] value; 
}

FlatMap::FlatMap(const FlatMap& b) : capacity(b.capacity), sizeArray(b.sizeArray)
{
    key = new Key[capacity];
    for (size_t i = 0; i < capacity; ++i) key[i] = 0;
    value = new Value[capacity];
    for (size_t i = 0; i < sizeArray; i++)
    {
        key[i] = b.key[i];
        value[i] = b.value[i];
    }
}
FlatMap::FlatMap(FlatMap&& b) : capacity(b.capacity), sizeArray(b.sizeArray)
{
    key = b.key;
    value = b.value;
    b.key = nullptr;
    b.value = nullptr;

    b.capacity = 0ull;
    b.sizeArray = 0ull;
}


// Обменивает значения двух флетмап.
// Подумайте, зачем нужен этот метод, при наличии стандартной функции
// std::swap.
void FlatMap::swap(FlatMap& b) {
    auto temp = move(b);
    b = move(*this);
    *this = move(temp);
}

FlatMap& FlatMap::operator=(const FlatMap& b)
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
FlatMap&& FlatMap::operator=(FlatMap&& b) 
{
    if (b == *this) return *this;
    
    capacity = b.capacity;
    b.capacity = 0ull;
    sizeArray = b.sizeArray;
    b.sizeArray = 0ull;

    key = b.key;
    b.key = nullptr;
    value = b.value;
    b.value = nullptr;

    return *this;
}


// Очищает контейнер.
void FlatMap::clear() 
{
    if (sizeArray == 0) return;

    for (size_t i = 0; i < sizeArray; ++i) 
    {
        key[i] = 0;
        value[i] = 0;
    }
    
    sizeArray = 0ull;
}
// Удаляет элемент по заданному ключу.
bool FlatMap::erase(const Key& k) 
{
    
}
// Вставка в контейнер. Возвращаемое значение - успешность вставки.
bool FlatMap::insert(const Key& k, const Value& v) 
{

}

// Проверка наличия значения по заданному ключу.
bool FlatMap::contains(const Key& k) const 
{

}

// Возвращает значение по ключу. Небезопасный метод.
// В случае отсутствия ключа в контейнере, следует вставить в контейнер
// значение, созданное конструктором по умолчанию и вернуть ссылку на него. 
Value& FlatMap::operator[](const Key& k) 
{
    size_t index = 0;
    while (index < capacity)
    {
        if (key[index] == 0) 
        {
            key[index] = k;
            break;
        }
        if (key[index] == k)
        {
            break;
        }
        else if (key[index] > k)
        {
            index = index * 2 + 1;
        }
        else
        {
            index = index * 2 + 2;
        }
    }

    if (index >= capacity)
    {
        capacity *= 2;
        key = (Key*)realloc(key, capacity * sizeof(*key));
        for (size_t i = capacity / 2; i < capacity; ++i)
        {
            key[i] = 0;
        }
        value = (Value*)realloc(value, capacity * sizeof(*value));
        key[index] = k;
    }

    return value[index];
}

// Возвращает значение по ключу. Бросает исключение при неудаче.
Value& FlatMap::at(const Key& k) 
{
    size_t index = 0;
    if (sizeArray == 0)
    {
        key[sizeArray] = k;
    }
    else 
    {
        while (index < capacity)
        {
            if (key[index] == )
            if (key[index] == k)
            {
                break;
            }
            else if (key[index] > k)
            {
                index = index * 2 + 1;
            }
            else
            {
                index = index * 2 + 2;
            }
        }
        if (index >= capacity)
        {
            throw std::runtime_error("Key is not available");
        }
    }

    return key[index];
}
const Value& FlatMap::at(const Key& k) const 
{

}

size_t FlatMap::size() const 
{
    return sizeArray;
}
bool FlatMap::empty() const 
{
    if (sizeArray == 0) return 1;
    return 0;
}

friend bool FlatMap::operator==(const FlatMap& a, const FlatMap& b);
friend bool FlatMap::operator!=(const FlatMap& a, const FlatMap& b);