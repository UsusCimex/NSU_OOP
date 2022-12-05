#ifndef TETRIS_H
#define TETRIS_H

#include <QWidget>
#include <QPoint>
#include <QKeyEvent>
#include <QPixmap>

class Tetris : public QWidget
{
    Q_OBJECT

public:
    Tetris(QWidget *parent = nullptr);
    ~Tetris();

protected:
    void timerEvent(QTimerEvent *) override;
    void keyPressEvent(QKeyEvent *) override;
    void paintEvent(QPaintEvent *) override;

private:
    static constexpr int DOT_WIDTH = 20;
    static constexpr int DOT_HEIGHT = 20;

    static constexpr int FIELD_WIDTH = 15;
    static constexpr int FIELD_HEIGHT = 20;

    static constexpr int DETAIL_SIZE = 4;

    int _delay;
    int timerID;

    bool _inGame;
    int field[FIELD_WIDTH][FIELD_HEIGHT];

    QPoint detail[DETAIL_SIZE];
    QPoint movedDetail[DETAIL_SIZE];

    int score = 0;

    bool checkDetail();
    void checkLines();

    void rotate();
    bool checkMoveDetail();

    void initGame();
    void createDetail();

    int figures[7][4] =
    {
        1,3,5,7, //I
        2,4,5,7, //Z
        3,5,4,6, // S
        3,5,4,7, // T
        2,3,5,7, // L
        3,5,7,6, // J
        2,3,4,5, // O
    };
};

#endif // TETRIS_H
