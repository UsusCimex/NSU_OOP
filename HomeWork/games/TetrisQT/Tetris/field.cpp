#include "field.h"
#include <algorithm>

Field::Field(size_t width_, size_t height_) : width_(width_), height_(height_)
{
    std::vector<int> hght;
    for (size_t j = 0; j < height_; ++j)
    {
        hght.push_back(0);
    }
    for (size_t i = 0; i < width_; ++i)
    {
        field.push_back(hght);
    }
}

size_t Field::width()
{
    return width_;
}

size_t Field::height()
{
    return  height_;
}

Field::~Field() = default;

int Field::checkLines()
{
    size_t posLine = height_ - 1;
    int counter = 0;
    for (size_t i = height_ - 1; i < height_; --i)
    {
        unsigned int count = 0;
        for (size_t j = 0; j < width_; ++j)
        {
            if (field[j][i]) count++;
            field[j][posLine] = field[j][i];
        }

        if (count < width_)
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
    for (size_t i = 0; i < width_; ++i)
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
    return std::make_pair(width_, height_);
}
