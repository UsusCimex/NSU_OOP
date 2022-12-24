#include "startwindow.h"

#include <QHBoxLayout>
#include <QApplication>
#include <QDebug>
#include "score.h"

StartWindow::StartWindow(QWidget* parent)
    : QWidget(parent)
{
    this->setWindowTitle("Tetris");
    this->resize(500,500);
    this->setFixedSize(500,500);

    brush = new QBrush;
    palette = new QPalette;
    brush->setTextureImage(QImage(":/img/menuBackground.png"));
    palette->setBrush(QPalette::Window, *brush);
    this->setPalette(*palette);

    startButton = new QPushButton(this);
    startButton->setFixedSize(322, 104);
    startButton->move(221, 295);
    startButton->setStyleSheet("QPushButton{background: transparent;}");
    connect(startButton, &QPushButton::clicked, this, &StartWindow::onPushButton);

    leaderButton = new QPushButton(this);
    leaderButton->setFixedSize(645, 98);
    leaderButton->move(58, 409);
    leaderButton->setStyleSheet("QPushButton{background: transparent;}");
    connect(leaderButton, &QPushButton::clicked, this, &StartWindow::onPushButton2);


    quitButton = new QPushButton(this);
    quitButton->setFixedSize(256, 109);
    quitButton->move(250, 516);
    quitButton->setStyleSheet("QPushButton{background: transparent;}");
    connect(quitButton, &QPushButton::clicked, this, &QApplication::quit);
}

StartWindow::~StartWindow()
{
    delete brush;
    delete palette;
    delete startButton;
    delete quitButton;
}

void StartWindow::onPushButton()
{
    close();
    Tetris* game = new Tetris();
    game->show();
}

void StartWindow::onPushButton2()
{
    close();
    ScoreTable newWindow;
    newWindow.show();
}
