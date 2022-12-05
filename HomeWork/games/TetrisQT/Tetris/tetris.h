#ifndef TETRIS_H
#define TETRIS_H

#include <QWidget>

class Tetris : public QWidget
{
    Q_OBJECT

public:
    Tetris(QWidget *parent = nullptr);
    ~Tetris();

private:
    static constexpr int M = 20;
    static constexpr int N = 10;
    int field[M][N] = {0};
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

    bool check();
    void draw();
    void checkLines();
    void rotate();
    void move();

protected slots:

};

#endif // TETRIS_H
