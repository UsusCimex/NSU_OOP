#include "detail.h"
#include <QDebug>

#include "field.h"

Detail::Detail() = default;

Detail::Detail(Field* field) : field(field), field_width(field->width()), field_height(field->height()) {}

Detail::Detail(Detail &detail2)
{
    for (size_t i = 0; i < DETAIL_SIZE; ++i)
    {
        this->detail[i] = detail2.detail[i];
    }
    color = detail2.color;
    figuresNum = detail2.figuresNum;
}

Detail::~Detail() = default;

bool Detail::move(Detail::movement arg)
{
    if (arg == movement::LEFT)
    {
        for (size_t i = 0; i < DETAIL_SIZE; ++i)
        {
            movedDetail[i].first = detail[i].first - 1;
            movedDetail[i].second = detail[i].second;
        }
        if (check())
            std::copy(movedDetail.begin(), movedDetail.end(), detail.begin());
    }
    else if (arg == movement::RIGHT)
    {
        for (size_t i = 0; i < DETAIL_SIZE; ++i)
        {
            movedDetail[i].first = detail[i].first + 1;
            movedDetail[i].second = detail[i].second;
        }
        if (check())
            std::copy(movedDetail.begin(), movedDetail.end(), detail.begin());
    }
    else if (arg == movement::DOWN)
    {
        for (size_t i = 0; i < DETAIL_SIZE; ++i)
        {
            movedDetail[i].first = detail[i].first;
            movedDetail[i].second = detail[i].second + 1;
        }
        if (checkMove())
        {
            std::copy(movedDetail.begin(), movedDetail.end(), detail.begin());
        }
        else
        {
            return false;
        }
    }
    return true;
}

bool Detail::transformation()
{
    figuresNum = qrand() % 7;
    color = qrand() % 7 + 1;

    for (size_t i = 0; i < DETAIL_SIZE; ++i)
    {
        movedDetail[i].first = figures[figuresNum][i] % 2 + field_width / 2 - 1;
        movedDetail[i].second = figures[figuresNum][i] / 2;
    }

    if (figuresNum == 0)
    {
        for (size_t i = 0; i < DETAIL_SIZE; ++i)
        {
            movedDetail[i].second += 1;
        }
    }

    std::copy(movedDetail.begin(), movedDetail.end(), detail.begin());
    return check();
}

size_t Detail::size() const
{
    return DETAIL_SIZE;
}

size_t Detail::getColor() const
{
    return color;
}

std::pair<size_t, size_t> &Detail::getCube(size_t index)
{
    if (index > DETAIL_SIZE) throw std::exception();

    return detail[index];
}

Detail& Detail::operator=(const Detail& detail2)
{
    std::copy(detail2.detail.begin(), detail2.detail.end(), detail.begin());

    color = detail2.color;
    figuresNum = detail2.figuresNum;
    return *this;
}

void Detail::rotate()
{
    if (figuresNum == 6) return; //O

    std::pair<size_t, size_t> p = detail[1]; //center of rotation
    size_t moveInd = 0;

    do
    {
        for (size_t i = 0; i < DETAIL_SIZE; ++i)
        {
            //clockwise
            if (p.first < (field->width() / 2))
            {
                movedDetail[i].first = p.first + p.second - detail[i].second + moveInd;
            }
            else
            {
                movedDetail[i].first = p.first + p.second - detail[i].second - moveInd;
            }
            movedDetail[i].second = detail[i].first + p.second - p.first;

            //counterclockwise
            //movedDetail[i].first = detail[i].second + p.first - p.second;
            //movedDetail[i].second = p.first + p.second - detail[i].first;
        }
        moveInd++;
    } while (!(check() || moveInd == DETAIL_SIZE));

    if (moveInd == DETAIL_SIZE) return;
    std::copy(movedDetail.begin(), movedDetail.end(), detail.begin());
}

bool Detail::checkMove() const
{
    if (!check())
    {
        for (size_t i = 0; i < DETAIL_SIZE; ++i)
        {
            if (movedDetail[i].second >= field_height || !field->isFree(movedDetail[i].first, movedDetail[i].second))
            {
                return false;
            }
        }
    }
    return true;
}

bool Detail::check() const
{
    for (size_t i = 0; i < DETAIL_SIZE; ++i)
    {
        if (movedDetail[i].first >= field_width || movedDetail[i].second >= field_height || !field->isFree(movedDetail[i].first, movedDetail[i].second))
        {
            return false;
        }
    }
    return true;
}
