#include "tetrisqt.h"

#include "score.h"

#include <QDebug>
#include <QPixmap>
#include <QTime>
#include <QMessageBox>
#include <string>

TetrisQT::TetrisQT(QString name, QWidget *parent)
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
    this->setWindowTitle("TetrisQT game");

    tetris = new TetrisCore();

    qsrand(static_cast<uint>(QTime::currentTime().msec()));
}

TetrisQT::~TetrisQT() = default;

void TetrisQT::setName(std::string name)
{
    this->name = name;
}

void TetrisQT::timerEvent(QTimerEvent * event)
{
    Q_UNUSED(event)

    if (_inGame)
    {
        switch (tetris->game())
        {
            case 1:
            {
                reloadTimer();
                break;
            }
            case 2:
            {
                QImage background(":/img/level2.png");
                brush->setTextureImage(background);
                palette->setBrush(QPalette::Window, *brush);
                this->setPalette(*palette);

                music->setMedia(QUrl("qrc:/music/level2.mp3"));
                music->play();
                reloadTimer();
                break;
            }
            case 3:
            {
                QImage background(":/img/level3.png");
                brush->setTextureImage(background);
                palette->setBrush(QPalette::Window, *brush);
                this->setPalette(*palette);

                music->setMedia(QUrl("qrc:/music/level3.mp3"));
                music->play();
                reloadTimer();
                break;
            }
            case -1:
            {
                killTimer(timerID);
                stopGame();
                return;
            }
        }
    }
    this->repaint();
}

void TetrisQT::keyPressEvent(QKeyEvent * event)
{
    int key = event->key();
    if (_inGame)
    {
        tetris->detailAction(key);
        if (key == Qt::Key_Space)
        {
            killTimer(timerID);
            timerID = startTimer(1);
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

void TetrisQT::paintEvent(QPaintEvent * event)
{
    Q_UNUSED(event)

    if (_inGame)
    {
        drawField();
        drawDetail();
        drawNextDetail();
    }

    drawScore();
}

void TetrisQT::initGame()
{
    _inGame = true;
    QImage background(":/img/level1.png");
    brush->setTextureImage(background);
    palette->setBrush(QPalette::Window, *brush);
    this->setPalette(*palette);

    music->setMedia(QUrl("qrc:/music/level1.mp3"));
    music->play();

    tetris->init();

    timerID = startTimer(tetris->getSpeed());
    this->repaint();
}

void TetrisQT::drawField()
{
    QPainter qp(this);
    for (size_t i = 0; i < tetris->getField()->sizeField().first; ++i)
    {
        for(size_t j = 0; j < tetris->getField()->sizeField().second; ++j)
        {
            if (tetris->getField()->getColor(i,j) == 0) continue;
            qp.drawPixmap(SHIFT_X + i * DOT_WIDTH, SHIFT_Y + j * DOT_HEIGHT, *tiles, tetris->getField()->getColor(i,j) * DOT_HEIGHT, 0, DOT_HEIGHT, DOT_WIDTH);
        }
    }
}

void TetrisQT::drawDetail()
{
    QPainter qp(this);
    for (size_t i = 0; i < tetris->getDetail(false).size(); ++i)
    {
        qp.drawPixmap(SHIFT_X + tetris->getDetail(false).getCube(i).first * DOT_WIDTH, SHIFT_Y + tetris->getDetail(false).getCube(i).second * DOT_HEIGHT, *tiles, tetris->getDetail(false).getColor() * DOT_HEIGHT, 0, DOT_HEIGHT, DOT_WIDTH);
    }
}

void TetrisQT::drawNextDetail()
{
    QPainter qp(this);
    for (size_t i = 0; i < tetris->getDetail(true).size(); ++i)
    {
        qp.drawPixmap(SHIFT_X_NEXT + tetris->getDetail(true).getCube(i).first * DOT_WIDTH, SHIFT_Y_NEXT + tetris->getDetail(true).getCube(i).second * DOT_HEIGHT, *tiles, tetris->getDetail(true).getColor() * DOT_HEIGHT, 0, DOT_HEIGHT, DOT_WIDTH);
    }
}

void TetrisQT::drawScore()
{
    std::string sScore(std::to_string(tetris->getScore()));
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

void TetrisQT::reloadTimer()
{
    killTimer(timerID);
    timerID = startTimer(tetris->getSpeed());
}

void TetrisQT::stopGame()
{
    music->stop();
    _inGame = 0;
    std::string sScore("Your score: ");
    sScore += std::to_string(tetris->getScore());
    QMessageBox::information(this, "WOW!", sScore.c_str());

    Score bestScore;
    bestScore.newScore(name, tetris->getScore());
}
