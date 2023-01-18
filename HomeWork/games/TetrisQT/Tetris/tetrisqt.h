#ifndef TETRISQT_H
#define TETRISQT_H

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

#include "tetriscore.h"

class TetrisQT : public QWidget
{
    Q_OBJECT

public:
    TetrisQT(QString name, QWidget *parent = nullptr);
    ~TetrisQT() override;

    void setName(std::string name);
    void initGame();

signals:
    void sTetris();

protected slots:
    void timerEvent(QTimerEvent *) override;
    void keyPressEvent(QKeyEvent *) override;
    void paintEvent(QPaintEvent *) override;

protected:
    static constexpr size_t SHIFT_X = 45;
    static constexpr size_t SHIFT_Y = 45;

    static constexpr size_t SHIFT_X_NEXT = 400;
    static constexpr size_t SHIFT_Y_NEXT = 110;

    static constexpr size_t DOT_WIDTH = 40;
    static constexpr size_t DOT_HEIGHT = 40;

    TetrisCore* tetris;

    QMediaPlayer* music;

    std::string name;

    QBrush* brush;
    QPalette* palette;
    QPixmap* tiles;

    int timerID;
    bool _inGame;

    //If search line, delete him, and return true;
    void drawField();
    void drawDetail();
    void drawNextDetail();
    void drawScore();

    void reloadTimer();
    void stopGame();
};

#endif // TETRISQT_H
