#include "tetris.h"

#include <QDebug>
#include <QPainter>
#include <time.h>

Tetris::Tetris(QWidget *parent)
    : QWidget(parent)
{
    this->setWindowTitle("Tetris game");

    this->resize(DOT_WIDTH * FIELD_WIDTH, DOT_HEIGHT * FIELD_HEIGHT);
    this->setFixedSize(DOT_WIDTH * FIELD_WIDTH, DOT_HEIGHT * FIELD_HEIGHT);

    qsrand((uint)time(0));
    initGame();
}

Tetris::~Tetris()
{

}

void Tetris::timerEvent(QTimerEvent * event)
{
    Q_UNUSED(event);

    for (int i = 0; i < DETAIL_SIZE; ++i)
    {
        qDebug() << detail[i].rx() << " " << detail[i].ry() << " \\ ";
    }
    qDebug() << "\n";

    if (_inGame)
    {
        if (!moveDetail())
        {
            for (int i = 0; i < DETAIL_SIZE; ++i)
            {
                field[detail[i].rx()][detail[i].ry()] = 1;
            }
            checkLines();
            createDetail();
        }
    }

    this->repaint();
}

void Tetris::keyPressEvent(QKeyEvent * event)
{
    if (_inGame)
    {
        int key = event->key();
        switch (key)
        {
        case Qt::Key_Left:
            for (int i = 0; i < DETAIL_SIZE; ++i) movedDetail[i].rx() -= 1;
            if (!checkDetail()) for (int i = 0; i < DETAIL_SIZE; ++i) detail[i] = movedDetail[i];
            break;
        case Qt::Key_Right:
            for (int i = 0; i < DETAIL_SIZE; ++i) movedDetail[i].rx() += 1;
            if (!checkDetail()) for (int i = 0; i < DETAIL_SIZE; ++i) detail[i] = movedDetail[i];
            break;
        case Qt::Key_Up:
            rotate();
            break;
        case Qt::Key_Down:
            _delay = 50;
            break;
        }
    }
}

void Tetris::paintEvent(QPaintEvent * event)
{
    Q_UNUSED(event);

    QPainter qp(this);

//    QPixmap tiles;
//    tiles.load(":img/tiles.png");

    if (_inGame)
    {
        qp.setBrush(Qt::blue);
        for (int i = 0; i < FIELD_WIDTH; ++i)
        {
            for(int j = 0; j < FIELD_HEIGHT; ++j)
            {
                if (field[i][j] == 0) continue;
                qp.drawRect(i * DOT_WIDTH, j * DOT_HEIGHT, DOT_WIDTH, DOT_HEIGHT);
            }
        }

        qp.setBrush(Qt::red);
        for (int i = 0; i < DETAIL_SIZE; ++i)
        {
            qp.drawRect(detail[i].rx() * DOT_WIDTH, detail[i].ry() * DOT_HEIGHT, DOT_WIDTH, DOT_HEIGHT);
        }
    }
    else
    {
//        gameOver();
    }
}

bool Tetris::checkDetail()
{
    for (int i = 0; i < DETAIL_SIZE; ++i)
    {
        if (movedDetail[i].rx() < 0 || movedDetail[i].rx() > FIELD_WIDTH || movedDetail[i].ry() > FIELD_HEIGHT || field[movedDetail[i].rx()][movedDetail[i].ry()] != 0)
        {
            return true;
        }
    }
    return false;
}

void Tetris::checkLines()
{
    int posLine = FIELD_HEIGHT - 1;
    for (int i = FIELD_HEIGHT - 1; i > 0; --i)
    {
        int count = 0;
        for (int j = 0; j < FIELD_WIDTH; ++j)
        {
            if (field[i][j]) count++;
            field[posLine][j] = field[i][j];
        }
        if (count < FIELD_WIDTH) posLine--;
        else score++;
    }
}

void Tetris::rotate()
{
    QPoint p = detail[1]; //center of rotation
    for (int i = 0; i < DETAIL_SIZE; ++i)
    {
        int x = detail[i].ry() - p.ry();
        int y = detail[i].rx() - p.rx();
        movedDetail[i].rx() = detail[i].rx() - x;
        movedDetail[i].ry() = detail[i].ry() - y;
    }
    if (!checkDetail()) for (int i = 0; i < DETAIL_SIZE; ++i) detail[i] = movedDetail[i];
}

bool Tetris::moveDetail()
{
    for (int i = 0; i < DETAIL_SIZE; ++i)
    {
        movedDetail[i].ry() = detail[i].ry() + 1;
    }
    if (!checkDetail())
    {
        for (int i = 0; i < DETAIL_SIZE; ++i) detail[i] = movedDetail[i];
    }
    else
    {
        for (int i = 0; i < DETAIL_SIZE; ++i)
        {
            if (movedDetail[i].ry() > FIELD_HEIGHT || field[movedDetail[i].rx()][movedDetail[i].ry()])
            {
                return true;
            }
        }
    }
    return false;
}

void Tetris::initGame() {

    for (int i = 0; i < FIELD_WIDTH; ++i)
    {
        for (int j = 0; j < FIELD_HEIGHT; ++j)
        {
            field[i][j] = 0;
        }
    }

    _inGame = true;
    _delay = 500;

    createDetail();

    timerID = startTimer(_delay);
}

void Tetris::createDetail()
{
    int randDetail = qrand() % 7;

    for (int i = 0; i < DETAIL_SIZE; ++i)
    {
        detail[i].rx() = figures[randDetail][i] % 2;
        detail[i].ry() = figures[randDetail][i] / 2;
    }
}
