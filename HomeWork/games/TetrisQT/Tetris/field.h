#ifndef FIELD_H
#define FIELD_H


class Field
{
public:
    Field(int width, int height);
    ~Field();

    int *operator[](int);
    //If search full lines, return multiply booster
    double checkLines();
private:
    int** field;
    int width;
    int height;
};

#endif // FIELD_H
