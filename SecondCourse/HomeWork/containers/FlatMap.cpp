#include "FlatMap.h"

template <typename Key, typename Value>
FlatMap<Key, Value>::FlatMap() : capacity(1)
{
    key = new Key[capacity];
    value = new Value[capacity];
}

template <typename Key, typename Value>
FlatMap<Key, Value>::~FlatMap()
{
    delete[] key;
    delete[] value;
}

template <typename Key, typename Value>
FlatMap<Key, Value>::FlatMap(const FlatMap& b) : capacity(b.capacity)
{
    key = new Key[capacity];
    value = new Value[capacity];
    for (size_t i = 0; i < b.sizeArray; i++)
    {
        key[i] = b.key[i];
        value[i] = b.value[i];
    }
}

template <typename Key, typename Value>
FlatMap<Key, Value>::FlatMap(FlatMap&& b) : capacity(b.capacity)
{
    key = b.key;
    value = b.value;
    b.key = nullptr;
    b.value = nullptr;
    
    b.capacity = 0ull;
    b.sizeArray = 0ull;
}

template <typename Key, typename Value>
void FlatMap<Key, Value>::swap(FlatMap& b) {
    auto temp = std::move(b);
    b = std::move(*this);
    *this = std::move(temp);
}

template <typename Key, typename Value>
FlatMap<Key, Value>& FlatMap<Key, Value>::operator=(const FlatMap& b)
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

template <typename Key, typename Value>
FlatMap<Key, Value>& FlatMap<Key, Value>::operator=(FlatMap&& b) 
{
    if (b == *this) return *this;
    
    capacity = b.capacity;
    sizeArray = b.sizeArray;
    b.capacity = 0ull;
    b.sizeArray = 0ull;

    key = b.key;
    b.key = nullptr;
    value = b.value;
    b.value = nullptr;

    return *this;
}

template <typename Key, typename Value>
void FlatMap<Key, Value>::clear()
{
    if (sizeArray == 0) return;
    
    ReallocArray(1);
    sizeArray = 0;
}

template <typename Key, typename Value>
bool FlatMap<Key, Value>::erase(const Key& k) 
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
            for (int i = indexSearch; i < sizeArray; ++i)
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

template <typename Key, typename Value>
bool FlatMap<Key, Value>::insert(const Key& k, const Value& v) 
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
            for (int i = indexSearch; i <= sizeArray; ++i)
            {
                std::swap(key[i], tempKey);
                std::swap(value[i], tempValue);
            }
            sizeArray++;
        }
    }
    return 1;
}

template <typename Key, typename Value>
bool FlatMap<Key, Value>::contains(const Key& k) const 
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

template <typename Key, typename Value>
Value& FlatMap<Key, Value>::operator[](const Key& k) 
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
            for (int i = indexSearch; i <= sizeArray; ++i)
            {
                std::swap(key[i], tempKey);
                std::swap(value[i], tempValue);
            }
            sizeArray++;
        }
    }
    return value[indexSearch];
}

template <typename Key, typename Value>
Value& FlatMap<Key, Value>::at(const Key& k) 
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

template <typename Key, typename Value>
const Value& FlatMap<Key, Value>::at(const Key& k) const //old realisation
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

template <typename Key, typename Value>
size_t FlatMap<Key, Value>::size() const
{
    return sizeArray;
}

template <typename Key, typename Value>
bool FlatMap<Key, Value>::empty() const 
{
    if (sizeArray == 0) return 1;
    return 0;
}

template <typename Key, typename Value>
void FlatMap<Key, Value>::ReallocArray(size_t newSize)
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
        for (int i = 0; i < newSize; ++i)
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

template <typename Key, typename Value>
bool operator==(const FlatMap<Key, Value>& a, const FlatMap<Key, Value>& b)
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

template <typename Key, typename Value>
bool operator!=(const FlatMap<Key, Value>& a, const FlatMap<Key, Value>& b)
{
    return !(a == b);
}