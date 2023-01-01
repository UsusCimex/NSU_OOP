#include "detail.h"

Detail::Detail(Field* field, unsigned int field_width, unsigned int field_height) : field(field), field_width(field_width), field_height(field_height) {}

Detail::Detail(Detail &detail2)
{
    for (int i = 0; i < DETAIL_SIZE; ++i)
    {
        this->detail[i] = detail2.detail[i];
    }
    color = detail2.color;
}

Detail::~Detail() = default;

bool Detail::move(Detail::movement arg)
{
    if (arg == LEFT)
    {
        for (int i = 0; i < DETAIL_SIZE; ++i)
        {
            movedDetail[i].rx() = detail[i].rx() - 1;
            movedDetail[i].ry() = detail[i].ry();
        }
        if (check())
            for (int i = 0; i < DETAIL_SIZE; ++i)
                detail[i] = movedDetail[i];
    }
    else if (arg == RIGHT)
    {
        for (int i = 0; i < DETAIL_SIZE; ++i)
        {
            movedDetail[i].rx() = detail[i].rx() + 1;
            movedDetail[i].ry() = detail[i].ry();
        }
        if (check())
            for (int i = 0; i < DETAIL_SIZE; ++i)
                detail[i] = movedDetail[i];
    }
    else if (arg == DOWN)
    {
        for (int i = 0; i < DETAIL_SIZE; ++i)
        {
            movedDetail[i].rx() = detail[i].rx();
            movedDetail[i].ry() = detail[i].ry() + 1;
        }
        if (checkMove())
        {
            for (int i = 0; i < DETAIL_SIZE; ++i)
            {
                detail[i] = movedDetail[i];
            }
        }
        else
        {
            return false;
        }
    }
    return true;
}

bool Detail::create()
{
    int randDetail = qrand() % 7;
    color = qrand() % 7 + 1;

    for (int i = 0; i < DETAIL_SIZE; ++i)
    {
        if (field->getColor(figures[randDetail][i] % 2 + field_width / 2 - 1, figures[randDetail][i] / 2) != 0)
        {
            return false;
        }
        detail[i].rx() = figures[randDetail][i] % 2 + field_width / 2 - 1;
        detail[i].ry() = figures[randDetail][i] / 2;
    }

    return true;
}

unsigned int Detail::size() const
{
    return DETAIL_SIZE;
}

int Detail::getColor() const
{
    return color;
}

QPoint &Detail::getCube(size_t index)
{
    if (index > DETAIL_SIZE) throw std::exception();

    return detail[index];
}

Detail& Detail::operator=(const Detail& detail2)
{
    for (int i = 0; i < DETAIL_SIZE; ++i)
    {
        this->detail[i] = detail2.detail[i];
    }
    color = detail2.color;
    return *this;
}

void Detail::rotate(size_t center)
{
    if (center >= DETAIL_SIZE) throw std::runtime_error("center rotation error...");
    QPoint p = detail[center]; //center of rotation
    for (int i = 0; i < DETAIL_SIZE; ++i)
    {
        int x = detail[i].y() - p.y();
        int y = detail[i].x() - p.x();
        movedDetail[i].rx() = p.x() - x;
        movedDetail[i].ry() = p.y() + y;
    }
    if (check())
    {
        for (int i = 0; i < DETAIL_SIZE; ++i)
            detail[i] = movedDetail[i];
    }
    else
    {
        if (center != 3) rotate(center + 1);
    }
}

bool Detail::checkMove() const
{
    if (!check())
    {
        for (int i = 0; i < DETAIL_SIZE; ++i)
        {
            if (movedDetail[i].y() >= field_height || field->getColor(movedDetail[i].x(), movedDetail[i].y()))
            {
                return false;
            }
        }
    }
    return true;
}

bool Detail::check() const
{
    for (int i = 0; i < DETAIL_SIZE; ++i)
    {
        if (movedDetail[i].x() < 0 || movedDetail[i].x() >= field_width || movedDetail[i].y() >= field_height || field->getColor(movedDetail[i].x(), movedDetail[i].y()) != 0)
        {
            return false;
        }
    }
    return true;
}
