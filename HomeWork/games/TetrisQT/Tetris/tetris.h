#ifndef TETRIS_H
#define TETRIS_H

#include <QWidget>
#include <QPoint>
#include <QKeyEvent>
#include <QPixmap>
#include <QBrush>
#include <QPalette>

#include "detail.h"

class Tetris : public QWidget
{
    Q_OBJECT

public:
    Tetris(QWidget *parent = nullptr);
    ~Tetris() override;

protected:
    void timerEvent(QTimerEvent *) override;
    void keyPressEvent(QKeyEvent *) override;
    void paintEvent(QPaintEvent *) override;

private:
    static constexpr int FIELD_WIDTH = 10;
    static constexpr int FIELD_HEIGHT = 20;

    static constexpr int DOT_WIDTH = 40;
    static constexpr int DOT_HEIGHT = 40;

    static constexpr int SHIFT_X = 44;
    static constexpr int SHIFT_Y = 43;

    static constexpr int SHIFT_X_NEXT = 300;
    static constexpr int SHIFT_Y_NEXT = 110;

    static constexpr double MOVE_SPEED = 0.96;

    int** field;

    Detail* detail;
    Detail* nextDetail;

    QBrush* brush;
    QPalette* palette;

    int _delay;
    int timerID;

    bool _inGame;

    int score;

    void checkLines();
    void initGame();
    void drawField(QPainter& qp);
    void drawDetail(QPainter& qp);
    void drawNextDetail(QPainter& qp);
};

#endif // TETRIS_H
