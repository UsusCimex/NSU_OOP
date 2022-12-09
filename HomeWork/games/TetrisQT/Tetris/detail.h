#ifndef DETAIL_H
#define DETAIL_H

#include <QPoint>

class Detail
{
public:
    enum movement
    {
        LEFT,
        RIGHT,
        DOWN
    };

    Detail(int** field, int field_width, int field_height);

    //Rotate detail
    void rotate();

    //Move detail
    //return false, if stopped
    //return true, else (and if smash into the wall)
    bool move(movement arg = DOWN);

    //Create random detail
    //return false, if don't search place
    bool create();

    int size();
    int getColor();

    Detail* operator=(Detail detail2);
    QPoint& operator[](int index);
private:
    static constexpr int DETAIL_SIZE = 4;
    int** field;
    int field_width;
    int field_height;

    QPoint detail[DETAIL_SIZE];
    QPoint movedDetail[DETAIL_SIZE];

    int color;

    //Check movedDetail position
    bool check();
    //Check stop position
    bool checkMove();

    int figures[7][4] =
    {
        1,3,5,7, //I
        2,4,5,7, //Z
        3,5,4,6, // S
        3,5,4,7, // T
        2,3,5,7, // L
        3,5,7,6, // J
        2,3,4,5, // O
    };
};

#endif // DETAIL_H
