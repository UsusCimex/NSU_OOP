#include "FlatMap.h"

FlatMap::FlatMap() : capacity(1)
{
    key = new Key[capacity];
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
    for (size_t i = 0; i < b.sizeArray; i++)
    {
        key[i] = b.key[i];
        value[i] = b.value[i];
    }
}
// FlatMap::FlatMap(FlatMap&& b) : capacity(b.capacity)
// {
//     key = b.key;
//     value = b.value;
//     b.key = nullptr;
//     b.value = nullptr;

//     b.capacity = 0ull;
//     b.sizeArray = 0ull;
// }

void FlatMap::swap(FlatMap& b) { //mb fix
    auto temp = std::move(b);
    b = std::move(*this);
    *this = std::move(temp);
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
// FlatMap&& FlatMap::operator=(FlatMap&& b) 
// {
//     if (b == this) return this;
    
//     capacity = b.capacity;
//     sizeArray = b.sizeArray;
//     b.capacity = 0ull;
//     b.sizeArray = 0ull;

//     key = b.key;
//     b.key = nullptr;
//     value = b.value;
//     b.value = nullptr;

//     return *this;
// }

void FlatMap::clear() //mb fix
{
    if (sizeArray == 0) return;
    
    sizeArray = 0;
}

bool FlatMap::erase(const Key& k) 
{
    if (sizeArray == 0)
    {
        return 0;
    }

    size_t r = sizeArray;
    size_t l = 0;
    indexSearch = (r + l) / 2;
    while (r - l > 0) {
        if (key[indexSearch] == k)
        {
            for (int i = indexSearch; i < sizeArray; ++i)
            {
                std::swap(key[i], key[i+1]);
                std::swap(value[i], value[i+1]);
            }
            sizeArray--;
            return 1;
        }
        else if (key[indexSearch] > k)
        {
            r = indexSearch;
        }
        else
        {
            l = indexSearch;
        }
        indexSearch = (r + l) / 2;
    }
    return 0;
}

bool FlatMap::insert(const Key& k, const Value& v) 
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
        indexSearch = (r + l) / 2;
        while (r - l > 0) {
            if (key[indexSearch] == k)
            {
                return 0; //mb old value needs to be replaced with a new one
            }
            else if (key[indexSearch] > k)
            {
                r = indexSearch;
            }
            else
            {
                l = indexSearch;
            }
            indexSearch = (r + l) / 2;
        }
        if (r == l)
        {
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

bool FlatMap::contains(const Key& k) const 
{
    if (sizeArray != 0)
    {
        size_t r = sizeArray;
        size_t l = 0;
        indexSearch = (r + l) / 2;
        while (r - l > 0) {
            if (key[indexSearch] == k)
            {
                return 1;
            }
            else if (key[indexSearch] > k)
            {
                r = indexSearch;
            }
            else
            {
                l = indexSearch;
            }
            indexSearch = (r + l) / 2;
        }
    }
    return 0;
}

Value& FlatMap::operator[](const Key& k) 
{
    if (sizeArray == capacity)
    {
        ReallocArray(capacity*2);
    }

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
                r = indexSearch;
            }
            else
            {
                l = indexSearch;
            }
            indexSearch = (r + l) / 2;
        }
        if (r == l)
        {
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

Value& FlatMap::at(const Key& k) 
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
            r = indexSearch;
        }
        else
        {
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
const Value& FlatMap::at(const Key& k) const //old realisation
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
            r = indexSearch;
        }
        else
        {
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

size_t FlatMap::size() const
{
    return sizeArray;
}
bool FlatMap::empty() const 
{
    if (sizeArray == 0) return 1;
    return 0;
}

void FlatMap::ReallocArray(size_t newSize)
{
    if (newSize < capacity) return;
    Key* tempKey = new Key[newSize];
    Value* tempValue = new Value[newSize];

    for (size_t i = 0; i < sizeArray; ++i)
    {
        tempKey[i] = key[i];
        tempValue[i] = value[i];
    }

    delete[] key;
    delete[] value;

    key = tempKey;
    value = tempValue;

    capacity = newSize;
}