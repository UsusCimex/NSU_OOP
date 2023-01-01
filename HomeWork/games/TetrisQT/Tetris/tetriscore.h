#ifndef TETRISCORE_H
#define TETRISCORE_H

#include "detail.h"
#include "field.h"

class TetrisCore
{
public:
    TetrisCore();
    TetrisCore(TetrisCore&) = delete;
    ~TetrisCore();

    int game();
    void detailAction(int key);

    int getSpeed() const;
    int getScore() const;

    Detail getDetail(bool isNext) const;
    Field *getField() const;

    void init();

private:
    static constexpr int FIELD_WIDTH = 10;
    static constexpr int FIELD_HEIGHT = 20;

    static constexpr double MOVE_SPEED = 0.95;
    int speed;

    Field* field;
    int level = 1;

    Detail* detail;
    Detail* nextDetail;

    int score;
};

#endif // TETRISCORE_H
