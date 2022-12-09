#include "tetris.h"

#include <QDebug>
#include <QPixmap>
#include <QPainter>
#include <time.h>

Tetris::Tetris(QWidget *parent)
    : QWidget(parent)
{
    QImage background(":/img/background.png");

    this->resize(background.width(), background.height());
    this->setFixedSize(background.width(), background.height());

    brush = new QBrush;
    palette = new QPalette;
    brush->setTextureImage(background);
    palette->setBrush(QPalette::Window, *brush);
    this->setPalette(*palette);
    this->setWindowTitle("Tetris game");

    field = new int*[FIELD_WIDTH];
    for (int i = 0; i < FIELD_WIDTH; ++i)
    {
        field[i] = new int[FIELD_HEIGHT];
    }

    detail = new Detail(field, FIELD_WIDTH, FIELD_HEIGHT);
    nextDetail = new Detail(field, FIELD_WIDTH, FIELD_HEIGHT);

    qsrand((uint)time(0));
    initGame();
}

Tetris::~Tetris()
{
    for (int i = 0; i < FIELD_WIDTH; ++i)
    {
        delete[] field[i];
    }
    delete[] field;

    delete detail;
    delete nextDetail;

    delete brush;
    delete palette;
}

void Tetris::timerEvent(QTimerEvent * event)
{
    Q_UNUSED(event);
    if (_inGame)
    {
        if (!detail->move())
        {
            for (int i = 0; i < detail->size(); ++i)
            {
                field[(*detail)[i].rx()][(*detail)[i].ry()] = detail->getColor();
            }
            checkLines();
            killTimer(timerID);
            detail = nextDetail;
            nextDetail->create();
            timerID = startTimer(_delay);
        }
        else
        {
            killTimer(timerID);
        }

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
            detail->move(Detail::LEFT);
            break;
        case Qt::Key_Right:
            detail->move(Detail::RIGHT);
            break;
        case Qt::Key_Up:
            detail->rotate();
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
        drawField(qp);
        drawDetail(qp);
        drawNextDetail(qp);
    }
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
            _delay *= MOVE_SPEED;
            score++;
        }
    }
}

void Tetris::initGame()
{
    _inGame = true;
    score = 0;

    for (int i = 0; i < FIELD_WIDTH; ++i)
    {
        for (int j = 0; j < FIELD_HEIGHT; ++j)
        {
            field[i][j] = 0;
        }
    }

    detail->create();
    nextDetail->create();
    _delay = 800;
    timerID = startTimer(_delay);
    this->repaint();
}

void Tetris::drawField(QPainter& qp)
{
    qp.setBrush(Qt::blue);
    for (int i = 0; i < FIELD_WIDTH; ++i)
    {
        for(int j = 0; j < FIELD_HEIGHT; ++j)
        {
            if (field[i][j] == 0) continue;
            qp.drawRect(SHIFT_X + i * DOT_WIDTH, SHIFT_Y + j * DOT_HEIGHT, DOT_WIDTH, DOT_HEIGHT);
        }
    }
}

void Tetris::drawDetail(QPainter& qp)
{
    qp.setBrush(Qt::red);
    for (int i = 0; i < detail->size(); ++i)
    {
        qp.drawRect(SHIFT_X + (*detail)[i].rx() * DOT_WIDTH, SHIFT_Y + (*detail)[i].ry() * DOT_HEIGHT, DOT_WIDTH, DOT_HEIGHT);
    }
}

void Tetris::drawNextDetail(QPainter& qp)
{
    qp.setBrush(Qt::green);
    for (int i = 0; i < nextDetail->size(); ++i)
    {
        qp.drawRect(SHIFT_X_NEXT + (*nextDetail)[i].rx() * DOT_WIDTH * 1.5, SHIFT_Y_NEXT + (*nextDetail)[i].ry() * DOT_HEIGHT * 1.5, DOT_WIDTH * 1.5, DOT_HEIGHT * 1.5);
    }
}
