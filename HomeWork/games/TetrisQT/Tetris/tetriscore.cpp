#include "tetriscore.h"

TetrisCore::TetrisCore()
{
    field = new Field(FIELD_WIDTH, FIELD_HEIGHT);

    detail = new Detail(field, FIELD_WIDTH, FIELD_HEIGHT);
    nextDetail = new Detail(field, FIELD_WIDTH, FIELD_HEIGHT);
    score = 0;
}

TetrisCore::~TetrisCore()
{
    delete field;
    delete detail;
    delete nextDetail;
}

int TetrisCore::game()
{
    int res = 0;
    if (!detail->move())
    {
        res = 1;
        for (size_t i = 0; i < detail->size(); ++i)
        {
            field->setColor(detail->getCube(i).x(),detail->getCube(i).y(), detail->getColor());
        }
        int countLines = field->checkLines();
        if (countLines > 0)
        {
            score += 100 * countLines * (1 + (countLines - 1) * 0.25);

            if (level == 1 && score >= 500) { level = 2; res = 2; }
            else if (level == 2 && score >= 1000) { level = 3; res = 3; }

            speed *= MOVE_SPEED;
        }
        (*detail) = (*nextDetail);
        if (!nextDetail->create()) res = -1;
    }
    return res;
}

void TetrisCore::detailAction(int key)
{
    switch (key)
    {
    case Qt::Key_Left:
        detail->move(Detail::LEFT);
        break;
    case Qt::Key_Right:
        detail->move(Detail::RIGHT);
        break;
    case Qt::Key_Up:
        detail->rotate(1);
        break;
    case Qt::Key_Down:
        detail->move(Detail::DOWN);
        break;
    }
}

int TetrisCore::getSpeed() const
{
    return speed;
}

int TetrisCore::getScore() const
{
    return score;
}

Detail TetrisCore::getDetail(bool isNext) const
{
    if (isNext)
    {
        return *nextDetail;
    }
    else
    {
        return *detail;
    }
}

Field *TetrisCore::getField() const
{
    return field;
}

void TetrisCore::init()
{
    field->resetField();
    score = 0;
    level = 1;

    detail->create();
    nextDetail->create();
    speed = 750;
}
