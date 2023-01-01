#include "field.h"
#include <algorithm>

Field::Field(size_t width, size_t height) : width(width), height(height)
{
    std::vector<int> hght;
    for (size_t j = 0; j < height; ++j)
    {
        hght.push_back(0);
    }
    for (size_t i = 0; i < width; ++i)
    {
        field.push_back(hght);
    }
}

Field::~Field() = default;

int Field::checkLines()
{
    size_t posLine = height - 1;
    int counter = 0;
    for (size_t i = height - 1; i < height; --i)
    {
        unsigned int count = 0;
        for (size_t j = 0; j < width; ++j)
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
        }
    }
    return counter;
}

void Field::resetField()
{
    for (size_t i = 0; i < width; ++i)
    {
        std::fill(field[i].begin(), field[i].end(), 0);
    }
}

int Field::getColor(size_t x, size_t y) const
{
    return field[x][y];
}

void Field::setColor(size_t x, size_t y, int color)
{
    field[x][y] = color;
}

std::pair<size_t, size_t> Field::sizeField() const
{
    return std::make_pair(width, height);
}
