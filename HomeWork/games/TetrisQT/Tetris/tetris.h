#ifndef TETRIS_H
#define TETRIS_H

#include <QWidget>
#include <QPoint>
#include <QKeyEvent>
#include <QPixmap>
#include <QBrush>
#include <QPalette>

#include <fstream>

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

    static constexpr int SHIFT_X = 45;
    static constexpr int SHIFT_Y = 45;

    static constexpr int SHIFT_X_NEXT = 400;
    static constexpr int SHIFT_Y_NEXT = 110;

    static constexpr double MOVE_SPEED = 0.94;

    std::ifstream readScore;
    std::ofstream writeScore;

    int** field;

    Detail* detail;
    Detail* nextDetail;

    QBrush* brush;
    QPalette* palette;
    QPixmap* tiles;

    int _delay;
    int timerID;

    bool _inGame;

    int score;
    int bestScore;

    //If search line, delete him, and return true;
    bool checkLines();
    void initGame();
    void drawField(QPainter& qp);
    void drawDetail(QPainter& qp);
    void drawNextDetail(QPainter& qp);
    void drawScore(QPainter& qp);
    void drawBestScore(QPainter& qp);

    void stopGame();
};

#endif // TETRIS_H
