#include "startwindow.h"

#include <QHBoxLayout>
#include <QApplication>
#include <QDebug>

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
    startButton->setFixedSize(250, 75);
    startButton->move(115, 240);
    startButton->setStyleSheet("QPushButton{background: transparent;}");
    connect(startButton, &QPushButton::clicked, this, &StartWindow::onPushButton);

    quitButton = new QPushButton(this);
    quitButton->setFixedSize(200, 75);
    quitButton->move(150, 345);
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
