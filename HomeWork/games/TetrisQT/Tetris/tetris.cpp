#include "tetris.h"

#include <QDebug>
#include <QPainter>

Tetris::Tetris(QWidget *parent)
    : QWidget(parent)
{
    this->setWindowTitle("Tetris game");

    this->resize(DOT_WIDTH * FIELD_WIDTH, DOT_HEIGHT * FIELD_HEIGHT);
    this->setFixedSize(DOT_WIDTH * FIELD_WIDTH, DOT_HEIGHT * FIELD_HEIGHT);

    initGame();
}

Tetris::~Tetris()
{

}

void Tetris::timerEvent(QTimerEvent * event)
{
//    qDebug() << 1;
}

void Tetris::keyPressEvent(QKeyEvent * event)
{
    int key = event->key();
    switch (key)
    {
        case Qt::Key_Left:
            qDebug() << "A";
            break;
        case Qt::Key_Right:
            qDebug() << "D";
            break;
    }
}

void Tetris::paintEvent(QPaintEvent * event)
{
    Q_UNUSED(event);
    doDrawing();
}

void Tetris::initGame() {
    _inGame = true;
    _delay = 150;
    timerID = startTimer(_delay);

    createDetail();
}

void Tetris::createDetail()
{

}

void Tetris::doDrawing()
{
    QPainter qp(this);

    if (_inGame)
    {

    }
    else
    {
//        gameOver();
    }
}
