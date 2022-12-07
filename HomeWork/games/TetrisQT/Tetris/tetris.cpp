#include "tetris.h"

#include <QDebug>
#include <QPixmap>
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
    if (_inGame)
    {
        if (!checkMoveDetail())
        {
            for (int i = 0; i < DETAIL_SIZE; ++i)
            {
                field[detail[i].rx()][detail[i].ry()] = 1;
            }
            checkLines();
            killTimer(timerID);
            createDetail();
        }
        else
        {
            for (int i = 0; i < DETAIL_SIZE; ++i)
            {
                detail[i] = movedDetail[i];
            }
        }
    }
    else
    {
        killTimer(timerID);
    }

    this->repaint();
}

void Tetris::keyPressEvent(QKeyEvent * event)
{
    int key = event->key();
    if (_inGame)
    {
        switch (key)
        {
        case Qt::Key_Left:
            for (int i = 0; i < DETAIL_SIZE; ++i)
            {
                movedDetail[i].rx() = detail[i].rx() - 1;
                movedDetail[i].ry() = detail[i].ry();
            }
            if (checkDetail())
                for (int i = 0; i < DETAIL_SIZE; ++i)
                    detail[i] = movedDetail[i];
            break;
        case Qt::Key_Right:
            for (int i = 0; i < DETAIL_SIZE; ++i)
            {
                movedDetail[i].rx() = detail[i].rx() + 1;
                movedDetail[i].ry() = detail[i].ry();
            }
            if (checkDetail())
                for (int i = 0; i < DETAIL_SIZE; ++i)
                    detail[i] = movedDetail[i];
            break;
        case Qt::Key_Up:
            rotate();
            break;
        case Qt::Key_Down:
            killTimer(timerID);
            timerID = startTimer(_delay / 10);
            break;
        }
        this->repaint();
    }
    else
    {
        if (key == Qt::Key_Space)
        {
            initGame();
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
}

bool Tetris::checkDetail()
{
    for (int i = 0; i < DETAIL_SIZE; ++i)
    {
        if (movedDetail[i].rx() < 0 || movedDetail[i].rx() >= FIELD_WIDTH || movedDetail[i].ry() >= FIELD_HEIGHT || field[movedDetail[i].rx()][movedDetail[i].ry()] != 0)
        {
            return false;
        }
    }
    return true;
}

void Tetris::checkLines()
{
    int posLine = FIELD_HEIGHT - 1;
    for (int i = FIELD_HEIGHT - 1; i > 0; --i)
    {
        int count = 0;
        for (int j = 0; j < FIELD_WIDTH; ++j)
        {
            if (field[j][i]) count++;
            field[j][posLine] = field[j][i];
        }
        if (count < FIELD_WIDTH)
        {
            posLine--;
        }
        else
        {
            _delay *= 0.95;
            score++;
        }
    }
}

void Tetris::rotate()
{
    QPoint p = detail[1]; //center of rotation
    for (int i = 0; i < DETAIL_SIZE; ++i)
    {
        int x = detail[i].ry() - p.ry();
        int y = detail[i].rx() - p.rx();
        movedDetail[i].rx() = p.rx() - x;
        movedDetail[i].ry() = p.ry() + y;
    }
    if (checkDetail())
    {
        for (int i = 0; i < DETAIL_SIZE; ++i)
            detail[i] = movedDetail[i];
        this->repaint();
    }
}

bool Tetris::checkMoveDetail()
{
    for (int i = 0; i < DETAIL_SIZE; ++i)
    {
        movedDetail[i].rx() = detail[i].rx();
        movedDetail[i].ry() = detail[i].ry() + 1;
    }
    if (!checkDetail())
    {
        for (int i = 0; i < DETAIL_SIZE; ++i)
        {
            if (movedDetail[i].ry() >= FIELD_HEIGHT || field[movedDetail[i].rx()][movedDetail[i].ry()])
            {
                return false;
            }
        }
    }
    return true;
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
    _delay = 4000 / FIELD_HEIGHT;
    score = 0;

    createDetail();
}

void Tetris::createDetail()
{
    timerID = startTimer(_delay);
    int randDetail = qrand() % 7;

    for (int i = 0; i < DETAIL_SIZE; ++i)
    {
        if (field[figures[randDetail][i] % 2 + FIELD_WIDTH / 2 - 1][figures[randDetail][i] / 2])
        {
            _inGame = false;
            break;
        }
        detail[i].rx() = figures[randDetail][i] % 2 + FIELD_WIDTH / 2 - 1;
        detail[i].ry() = figures[randDetail][i] / 2;
    }
    this->repaint();
}
