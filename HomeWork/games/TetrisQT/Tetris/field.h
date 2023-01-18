#ifndef FIELD_H
#define FIELD_H

#include <vector>

class Field
{
public:
    Field(size_t width, size_t height);
    Field(Field& fld) = delete;
    Field operator=(Field) = delete;
    ~Field();

    //If search full lines, return count lines
    size_t width();
    size_t height();
    int checkLines();
    void resetField();
    size_t getColor(size_t x, size_t y) const;
    bool isFree(size_t x, size_t y) const;
    void setColor(size_t x, size_t y, size_t color);
    std::pair<size_t, size_t> sizeField() const;

private:
    std::vector<std::vector<size_t>> field;
    size_t width_;
    size_t height_;
};

#endif // FIELD_H
