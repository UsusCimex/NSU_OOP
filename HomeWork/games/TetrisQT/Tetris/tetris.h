#ifndef TETRIS_H
#define TETRIS_H

#include <QWidget>
#include <QPoint>
#include <QKeyEvent>
#include <QPixmap>
#include <QFont>
#include <QPen>
#include <QBrush>
#include <QPainter>
#include <QPalette>
#include <QMediaPlayer>

#include "detail.h"
#include "field.h"

class Tetris : public QWidget
{
    Q_OBJECT

public:
    Tetris(QString name, QWidget *parent = nullptr);
    ~Tetris() override;

    void setName(std::string name);
    void initGame();

signals:
    void sTetris();

protected slots:
    void timerEvent(QTimerEvent *) override;
    void keyPressEvent(QKeyEvent *) override;
    void paintEvent(QPaintEvent *) override;

protected:
    static constexpr int FIELD_WIDTH = 10;
    static constexpr int FIELD_HEIGHT = 20;

    static constexpr int DOT_WIDTH = 40;
    static constexpr int DOT_HEIGHT = 40;

    static constexpr int SHIFT_X = 45;
    static constexpr int SHIFT_Y = 45;

    static constexpr int SHIFT_X_NEXT = 400;
    static constexpr int SHIFT_Y_NEXT = 110;

    static constexpr double MOVE_SPEED = 0.95;

    int level = 1;

    QMediaPlayer* music;

    std::string name;
    Field* field = new Field(FIELD_WIDTH, FIELD_HEIGHT);

    Detail* detail;
    Detail* nextDetail;

    QBrush* brush;
    QPalette* palette;
    QPixmap* tiles;

    int _delay;
    int timerID;

    bool _inGame;

    int score;

    //If search line, delete him, and return true;
    void drawField();
    void drawDetail();
    void drawNextDetail();
    void drawScore();

    void stopGame();
};

#endif // TETRIS_H
