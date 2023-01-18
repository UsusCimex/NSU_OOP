#ifndef TETRISCORE_H
#define TETRISCORE_H

#include <memory>
#include <QKeyEvent>

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

    Detail getDetail(bool isNext);
    Field *getField() const;

    void init();

private:
    static constexpr int FIELD_WIDTH = 10;
    static constexpr int FIELD_HEIGHT = 20;

    static constexpr double MOVE_SPEED = 0.95;
    int speed;

    std::unique_ptr<Field> field;
    int level = 1;
    std::unique_ptr<Detail> detail;
    std::unique_ptr<Detail> nextDetail;

    int score;
};

#endif // TETRISCORE_H
