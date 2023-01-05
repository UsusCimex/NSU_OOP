#include "detail.h"
#include <QDebug>

Detail::Detail(Field* field, unsigned int field_width, unsigned int field_height) : field(field), field_width(field_width), field_height(field_height) {}

Detail::Detail(Detail &detail2)
{
    for (int i = 0; i < DETAIL_SIZE; ++i)
    {
        this->detail[i] = detail2.detail[i];
    }
    color = detail2.color;
    figuresNum = detail2.figuresNum;
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
    figuresNum = qrand() % 7;
    color = qrand() % 7 + 1;

    for (int i = 0; i < DETAIL_SIZE; ++i)
    {
        movedDetail[i].rx() = figures[figuresNum][i] % 2 + field_width / 2 - 1;
        movedDetail[i].ry() = figures[figuresNum][i] / 2;
    }

    if (figuresNum == 0)
    {
        for (int i = 0; i < DETAIL_SIZE; ++i)
        {
            movedDetail[i].ry() += 1;
        }
    }

    for (int i = 0; i < DETAIL_SIZE; ++i)
    {
        detail[i] = movedDetail[i];
    }

    return check();
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
    figuresNum = detail2.figuresNum;
    return *this;
}

void Detail::rotate()
{
    if (figuresNum == 6) return; //O

    QPoint p = detail[1]; //center of rotation
    int moveInd = 0;

    do
    {
        for (int i = 0; i < DETAIL_SIZE; ++i)
        {
            //clockwise
            if (p.x() < (field->width() / 2))
            {
                movedDetail[i].rx() = p.x() + p.y() - detail[i].y() + moveInd;
            }
            else
            {
                movedDetail[i].rx() = p.x() + p.y() - detail[i].y() - moveInd;
            }
            movedDetail[i].ry() = detail[i].x() + p.y() - p.x();

            //counterclockwise
            //movedDetail[i].rx() = detail[i].y() + p.x() - p.y();
            //movedDetail[i].ry() = p.x() + p.y() - detail[i].x();
        }
        moveInd++;
    } while (!(check() || moveInd == DETAIL_SIZE));

    if (moveInd == DETAIL_SIZE) return;
    for (int i = 0; i < DETAIL_SIZE; ++i)
    {
        detail[i] = movedDetail[i];
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
