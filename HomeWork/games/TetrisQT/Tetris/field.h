#ifndef FIELD_H
#define FIELD_H

#include <vector>

class Field
{
public:
    Field(size_t width, size_t height);
    Field(Field& fld) = delete;
    ~Field();

    //If search full lines, return count lines
    int checkLines();
    void resetField();
    int getColor(size_t x, size_t y) const;
    void setColor(size_t x, size_t y, int color);
    std::pair<size_t, size_t> sizeField() const;

private:
    std::vector<std::vector<int>> field;
    size_t width;
    size_t height;
};

#endif // FIELD_H
