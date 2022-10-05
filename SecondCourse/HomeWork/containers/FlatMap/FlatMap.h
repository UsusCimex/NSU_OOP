#ifndef FLATMAP_H
#define FLATMAP_H

template <class Key, class Value>
class FlatMap
{
public:
    FlatMap() : capacity(kDefaultSize)
    {
        key = new Key[capacity];
        try { value = new Value[capacity]; }
        catch (...) { delete[] key; }
    }
    ~FlatMap()
    {
        delete[] key;
        delete[] value;
    }
    
    FlatMap(const FlatMap& b) : capacity(b.capacity), sizeArray(b.sizeArray)
    {
        key = new Key[capacity];
        try { value = new Value[capacity]; }
        catch (...) { delete[] key; }
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
    // Swaps the values of two flatmaps.
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
        try { value = new Value[capacity]; }
        catch (...) { delete[] key; }
        
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
    // Clears the container.
    void clear()
    {
        if (sizeArray == 0) return;
    
        ReallocArray(kDefaultSize);
        sizeArray = 0;
    }
    // Removes an element with the given key.
    // Used Binary search and std::move
    bool erase(const Key& k)
    {
        if (sizeArray == 0) return false;
        const size_t index = BinarySearch(k);
        if (key[index] != k) return false;

        std::move(key + index + 1, key + sizeArray, key + index);
        std::move(value + index + 1, value + sizeArray, value + index);
        sizeArray--;

        if (sizeArray < capacity / kDefaultMultiply) //optimise memory
        {
            ReallocArray(capacity / kDefaultMultiply);
        }

        return true;
    }
    // Insert into container. The return value is the success of the insert.
    // Used Binary search and std::move_backward
    bool insert(const Key& k, const Value& v)
    {
        const size_t index = BinarySearch(k);
        if (key[index] == k) return false; //mb old value needs to be replaced with a new one

        if (sizeArray == capacity)
        {
            ReallocArray(capacity * kDefaultMultiply);
        }

        std::move_backward(key + index, key + sizeArray, key + sizeArray + 1);
        key[index] = k;

        std::move_backward(value + index, value + sizeArray, value + sizeArray + 1);
        value[index] = v;
        
        sizeArray++;
        return true;
    }

    // Checking if a value exists for a given key.
    // Used Binary search
    bool contains(const Key& k) const
    {
        const size_t index = BinarySearch(k);
        if (key[index] == k) return true;
        return false;
    }

    // Returns a value by key. Unsafe method.
    // If the key is not in the container, it should be inserted into the container
    // the value created by the default constructor and return a reference to it.
    //Works like std::move_backward and BinarySearch
    Value& operator[](const Key& k)
    {
        const size_t index = BinarySearch(k);
        if (key[index] == k) return value[index];

        if (sizeArray == capacity)
        {
            ReallocArray(capacity * kDefaultMultiply);
        }
        
        std::move_backward(key + index, key + sizeArray, key + sizeArray + 1);
        key[index] = k;

        std::move_backward(value + index, value + sizeArray, value + sizeArray + 1);
        value[index] = kDefaultValue;
        
        sizeArray++;
        return value[index];
    }

    // Returns a value by key. Throws an exception on failure.
    Value& at(const Key& k)
    {
        const size_t index = BinarySearch(k);
        if (key[index] == k) return value[index];
        else throw std::out_of_range("Key isn't available");
    }
    const Value& at(const Key& k) const
    {
        const size_t index = BinarySearch(k);
        if (key[index] == k) return value[index];
        else throw std::out_of_range("Key isn't available");
    }

    size_t size() const
    {
        return sizeArray;
    }
    bool empty() const
    {
        return (sizeArray == 0);
    }
    //Works like std::equal
    friend bool operator==(const FlatMap& a, const FlatMap& b)
    {
        if (a.sizeArray != b.sizeArray) 
        {
            return false;
        }

        if (std::equal(a.key, a.key + a.sizeArray, b.key) && std::equal(a.value, a.value + a.sizeArray, b.value)) return true;
        return false;
    }
    friend bool operator!=(const FlatMap& a, const FlatMap& b)
    {
        return !(a == b);
    }

private:
    static constexpr size_t kDefaultSize = 1;
    static constexpr size_t kDefaultMultiply = 2;
    static constexpr size_t kDefaultValue = 0;

    size_t capacity = 0ull;
    size_t sizeArray = 0ull;
    Key* key = nullptr;
    Value* value = nullptr;

    //Changes array size. Allocates new memory or delete old memory
    //Works just like std::copy
    void ReallocArray(size_t newSize)
    {
        if (newSize == capacity) return;

        Key* tempKey = new Key[newSize];
        Value* tempValue = nullptr;
        try { tempValue = new Value[newSize]; }
        catch (...) { delete[] tempKey; }

        if (newSize > capacity)
        {
            std::copy(key, key + sizeArray, tempKey);
            std::copy(value, value + sizeArray, tempValue);
        }
        else //lose some value
        {
            sizeArray = std::min(sizeArray, newSize);
            std::copy(key, key + newSize, tempKey);
        }

        delete[] key;
        delete[] value;

        key = tempKey;
        value = tempValue;

        capacity = newSize;
    }
    //Searches key index in array and returns next position if key index is not found
    //Works in O(logN)
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