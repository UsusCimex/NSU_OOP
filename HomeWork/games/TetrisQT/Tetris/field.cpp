#include "field.h"

Field::Field(int width, int height) : width(width), height(height)
{
    field = new int*[width];
    for (int i = 0; i < width; ++i)
    {
        field[i] = new int[height];
    }
}

Field::~Field()
{
    for (int i = 0; i < width; ++i)
    {
        delete[] field[i];
    }
    delete[] field;
}

int *Field::operator[](int width)
{
    return field[width];
}

double Field::checkLines()
{
    int posLine = height - 1;
    bool flag = false;
    double multiply = 0;
    int counter = 0;
    for (int i = height - 1; i > 0; --i)
    {
        int count = 0;
        for (int j = 0; j < width; ++j)
        {
            if (field[j][i]) count++;
            field[j][posLine] = field[j][i];
        }

        if (count < width)
        {
            posLine--;
        }
        else
        {
            counter++;
            if (flag)
            {
                multiply += 0.25;
            }
            else
            {
                flag = true;
                multiply = 1;
            }
        }
    }
    return multiply;
}
