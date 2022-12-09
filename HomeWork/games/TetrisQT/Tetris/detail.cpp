#include "detail.h"

Detail::Detail(int** field, int field_width, int field_height)
{
    this->field = field;
    this->field_width = field_width;
    this->field_height = field_height;
}

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
    color = randDetail + 1;

    for (int i = 0; i < DETAIL_SIZE; ++i)
    {
        if (field[figures[randDetail][i] % 2 + field_width / 2 - 1][figures[randDetail][i] / 2])
        {
            return false;
        }
        detail[i].rx() = figures[randDetail][i] % 2 + field_width / 2 - 1;
        detail[i].ry() = figures[randDetail][i] / 2;
    }

    return true;
}

int Detail::size()
{
    return DETAIL_SIZE;
}

int Detail::getColor()
{
    return color;
}

Detail* Detail::operator=(Detail *detail2)
{
    for (int i = 0; i < DETAIL_SIZE; ++i)
    {
        this->detail[i] = detail2->detail[i];
    }
    return this;
}

QPoint& Detail::operator[](int index)
{
    if (index > DETAIL_SIZE) throw std::exception();

    return detail[index];
}

void Detail::rotate()
{
    QPoint p = detail[1]; //center of rotation
    for (int i = 0; i < DETAIL_SIZE; ++i)
    {
        int x = detail[i].ry() - p.ry();
        int y = detail[i].rx() - p.rx();
        movedDetail[i].rx() = p.rx() - x;
        movedDetail[i].ry() = p.ry() + y;
    }
    if (check())
    {
        for (int i = 0; i < DETAIL_SIZE; ++i)
            detail[i] = movedDetail[i];
    }
}

bool Detail::checkMove()
{
    if (!check())
    {
        for (int i = 0; i < DETAIL_SIZE; ++i)
        {
            if (movedDetail[i].ry() >= field_height || field[movedDetail[i].rx()][movedDetail[i].ry()])
            {
                return false;
            }
        }
    }
    return true;
}

bool Detail::check()
{
    for (int i = 0; i < DETAIL_SIZE; ++i)
    {
        if (movedDetail[i].rx() < 0 || movedDetail[i].rx() >= field_width || movedDetail[i].ry() >= field_height || field[movedDetail[i].rx()][movedDetail[i].ry()] != 0)
        {
            return false;
        }
    }
    return true;
}
