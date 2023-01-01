#ifndef DETAIL_H
#define DETAIL_H

#include <QPoint>
#include <QPixmap>

#include "field.h"

class Detail
{
public:
    enum movement
    {
        LEFT,
        RIGHT,
        DOWN
    };

    Detail(Field* field, unsigned int field_width, unsigned int field_height);
    Detail(Detail& detail2);
    ~Detail();

    //Rotate detail
    void rotate(size_t center);

    //Move detail
    //return false, if stopped
    //return true, else (and if smash into the wall)
    bool move(movement arg = DOWN);

    //Create random detail
    //return false, if don't search place
    bool create();

    unsigned int size() const;
    int getColor() const;
    QPoint& getCube(size_t index);

    Detail& operator=(const Detail& detail2);
private:
    static constexpr int DETAIL_SIZE = 4;
    Field* field;
    unsigned int field_width;
    unsigned int field_height;

    QPoint detail[DETAIL_SIZE];
    QPoint movedDetail[DETAIL_SIZE];

    int color;

    //Check movedDetail position
    bool check() const;
    //Check stop position
    bool checkMove() const;

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
