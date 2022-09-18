#include "FlatMap.h"

FlatMap::FlatMap() : capacity(1)
{
    key = new Key[capacity];
    key[0] = 0;
    value = new Value[capacity];
}
FlatMap::~FlatMap()
{
    delete[] key;
    delete[] value; 
}

FlatMap::FlatMap(const FlatMap& b) : capacity(b.capacity)
{
    key = new Key[capacity];
    value = new Value[capacity];
    for (size_t i = 0; i < capacity; i++)
    {
        key[i] = b.key[i];
        value[i] = b.value[i];
    }
}
FlatMap::FlatMap(FlatMap&& b) : capacity(b.capacity)
{
    key = b.key;
    value = b.value;
    b.key = nullptr;
    b.value = nullptr;

    b.capacity = 0ull;
}

void FlatMap::swap(FlatMap& b) {
    auto temp = move(b);
    b = move(*this);
    *this = move(temp);
}

FlatMap& FlatMap::operator=(const FlatMap& b)
{
    if (b == *this) return *this;
    capacity = b.capacity;
    key = new Key[capacity];
    value = new Value[capacity];

    for (size_t i = 0; i < capacity; ++i)
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

    key = b.key;
    b.key = nullptr;
    value = b.value;
    b.value = nullptr;

    return *this;
}

void FlatMap::clear() 
{
    if (key[0] == 0) return;

    for (size_t i = 0; i < capacity; ++i) 
    {
        key[i] = 0;
        value[i] = 0;
    }
}

bool FlatMap::erase(const Key& k) 
{
    
}

bool FlatMap::insert(const Key& k, const Value& v) 
{

}

bool FlatMap::contains(const Key& k) const 
{

}

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

Value& FlatMap::at(const Key& k) 
{
    size_t index = 0;
    if (key[0] == 0)
    {
        key[0] = k;
    }
    else
    {
        while (index < capacity)
        {
            if (key[index] == 0)
            {
                throw std::runtime_error("Key is not available");
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
            throw std::runtime_error("Key is not available");
        }
    }
    return key[index];
}
const Value& FlatMap::at(const Key& k) const //Та же реализация, что без const //temp
{
    size_t index = 0;
    if (key[0] == 0)
    {
        key[0] = k;
    }
    else 
    {
        while (index < capacity)
        {
            if (key[index] == 0)
            {
                throw std::runtime_error("Key is not available");
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
            throw std::runtime_error("Key is not available");
        }
    }

    return key[index];
}

size_t FlatMap::size() const 
{
    //обход дерева, либо оставить переменную sizeArray
}
bool FlatMap::empty() const 
{
    if (key[0] == 0) return 1;
    return 0;
}

friend bool FlatMap::operator==(const FlatMap& a, const FlatMap& b);
friend bool FlatMap::operator!=(const FlatMap& a, const FlatMap& b);