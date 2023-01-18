#ifndef DETAIL_H
#define DETAIL_H

#include <array>

class Field;

class Detail
{
public:
    enum class movement
    {
        LEFT,
        RIGHT,
        DOWN
    };

    Detail();
    Detail(Field* field);
    Detail(Detail& detail2);
    ~Detail();

    //Rotate detail
    void rotate();

    //Move detail
    //return false, if stopped
    //return true, else (and if smash into the wall)
    bool move(movement arg = movement::DOWN);

    //Create random detail
    //return false, if don't search place
    bool transformation();

    size_t size() const;
    size_t getColor() const;
    std::pair<size_t, size_t>& getCube(size_t index);

    Detail& operator=(const Detail& detail2);
private:
    static constexpr int DETAIL_SIZE = 4;
    Field* field = nullptr;
    unsigned int field_width;
    unsigned int field_height;

    std::array<std::pair<size_t, size_t>, DETAIL_SIZE> detail;
    std::array<std::pair<size_t, size_t>, DETAIL_SIZE> movedDetail;

    size_t color = 0;

    //Check movedDetail position
    bool check() const;
    //Check stop position
    bool checkMove() const;

    size_t figuresNum = 0;
    size_t figures[7][4] =
    {
        {1,3,5,7}, //I
        {2,4,5,7}, //Z
        {3,5,4,6}, // S
        {3,5,4,7}, // T
        {2,3,5,7}, // L
        {3,5,7,6}, // J
        {2,3,4,5}, // O
    };
};

#endif // DETAIL_H
