#include "tetris.h"

#include "score.h"

#include <QDebug>
#include <QPixmap>
#include <time.h>
#include <QMessageBox>
#include <string>

Tetris::Tetris(QString name, QWidget *parent)
    : QWidget(parent)
{
    this->name = name.toStdString();
    music = new QMediaPlayer(this);
    _inGame = false;
    QImage background(":/img/level1.png");

    this->resize(background.width(), background.height());
    this->setFixedSize(background.width(), background.height());

    tiles = new QPixmap(":/img/tiles.png");
    brush = new QBrush;
    palette = new QPalette;
    brush->setTextureImage(background);
    palette->setBrush(QPalette::Window, *brush);
    this->setPalette(*palette);
    this->setWindowTitle("Tetris game");    

    detail = new Detail(field, FIELD_WIDTH, FIELD_HEIGHT);
    nextDetail = new Detail(field, FIELD_WIDTH, FIELD_HEIGHT);

    qsrand((uint)time(0));
}

Tetris::~Tetris()
{
    delete field;

    delete detail;
    delete nextDetail;

    delete brush;
    delete palette;
    delete music;
}

void Tetris::setName(std::string name)
{
    this->name = name;
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
                (*field)[(*detail)[i].rx()][(*detail)[i].ry()] = detail->getColor();
            }
            double multiply = field->checkLines();
            if (multiply > 0)
            {
                if (multiply > 1.2) score += 400 * multiply * (multiply - 1);
                else score += 100;

                if (level == 1 && score >= 500)
                {
                    level = 2;
                    QImage background(":/img/level2.png");
                    brush->setTextureImage(background);
                    palette->setBrush(QPalette::Window, *brush);
                    this->setPalette(*palette);

                    music->setMedia(QUrl("qrc:/music/level2.mp3"));
                    music->play();
                }
                else if (level == 2 && score >= 1000)
                {
                    level = 3;
                    QImage background(":/img/level3.png");
                    brush->setTextureImage(background);
                    palette->setBrush(QPalette::Window, *brush);
                    this->setPalette(*palette);

                    music->setMedia(QUrl("qrc:/music/level3.mp3"));
                    music->play();
                }

                _delay *= MOVE_SPEED;
            }
            (*detail) = (*nextDetail);
            killTimer(timerID);
            if (!nextDetail->create()) stopGame();
            else timerID = startTimer(_delay);
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
            detail->move(Detail::DOWN);
            break;
        case Qt::Key_Space:
            killTimer(timerID);
            timerID = startTimer(1);
            break;
        }
        this->repaint();
    }
    else if (key == Qt::Key_Escape)
    {
        this->close();
        emit sTetris();
    }
    else if (key == Qt::Key_Enter || key == Qt::Key_Space)
    {
        initGame();
    }
}

void Tetris::paintEvent(QPaintEvent * event)
{
    Q_UNUSED(event);

    if (_inGame)
    {
        drawField();
        drawDetail();
        drawNextDetail();
    }

    drawScore();
}

void Tetris::initGame()
{
    _inGame = true;
    score = 0;
    if (level != 1)
    {
        level = 1;
        QImage background(":/img/level1.png");
        brush->setTextureImage(background);
        palette->setBrush(QPalette::Window, *brush);
        this->setPalette(*palette);
    }

    music->setMedia(QUrl("qrc:/music/level1.mp3"));
    music->play();

    for (int i = 0; i < FIELD_WIDTH; ++i)
    {
        for (int j = 0; j < FIELD_HEIGHT; ++j)
        {
            (*field)[i][j] = 0;
        }
    }

    detail->create();
    nextDetail->create();
    _delay = 750;
    timerID = startTimer(_delay);
    this->repaint();
}

void Tetris::drawField()
{
    QPainter qp(this);
    for (int i = 0; i < FIELD_WIDTH; ++i)
    {
        for(int j = 0; j < FIELD_HEIGHT; ++j)
        {
            if ((*field)[i][j] == 0) continue;
            qp.drawPixmap(SHIFT_X + i * DOT_WIDTH, SHIFT_Y + j * DOT_HEIGHT, *tiles, (*field)[i][j] * DOT_HEIGHT, 0, DOT_HEIGHT, DOT_WIDTH);
        }
    }
}

void Tetris::drawDetail()
{
    QPainter qp(this);
    for (int i = 0; i < detail->size(); ++i)
    {
        qp.drawPixmap(SHIFT_X + (*detail)[i].rx() * DOT_WIDTH, SHIFT_Y + (*detail)[i].ry() * DOT_HEIGHT, *tiles, detail->getColor() * DOT_HEIGHT, 0, DOT_HEIGHT, DOT_WIDTH);
    }
}

void Tetris::drawNextDetail()
{
    QPainter qp(this);
    for (int i = 0; i < nextDetail->size(); ++i)
    {
        qp.drawPixmap(SHIFT_X_NEXT + (*nextDetail)[i].rx() * DOT_WIDTH, SHIFT_Y_NEXT + (*nextDetail)[i].ry() * DOT_HEIGHT, *tiles, nextDetail->getColor() * DOT_HEIGHT, 0, DOT_HEIGHT, DOT_WIDTH);
    }
}

void Tetris::drawScore()
{
    std::string sScore(std::to_string(score));
    QPainter qp(this);
    QFont font;
    QPen pen;
    font.setBold(true);
    font.setPixelSize(50);
    pen.setColor(Qt::white);
    qp.setFont(font);
    qp.setPen(pen);
    qp.drawText(525, 555, sScore.c_str());
}

void Tetris::stopGame()
{
    music->stop();
    _inGame = 0;
    std::string sScore("Your score: ");
    sScore += std::to_string(score);
    QMessageBox::information(this, "WOW!", sScore.c_str());

    Score bestScore;
    bestScore.newScore(name, score);
}
