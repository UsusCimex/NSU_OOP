#include "startwindow.h"

#include <QHBoxLayout>
#include <QPalette>
#include <QApplication>
#include <QMessageBox>
#include <QDebug>
#include "score.h"

StartWindow::StartWindow(QWidget* parent)
    : QWidget(parent)
{
    this->setWindowTitle("Tetris");
    QImage backGround(":/img/menuBackground.png");
    this->resize(backGround.width(),backGround.height());
    this->setFixedSize(backGround.width(),backGround.height());

    brush = new QBrush;
    palette = new QPalette;
    brush->setTextureImage(backGround);
    palette->setBrush(QPalette::Window, *brush);
    this->setPalette(*palette);

    nameEnter = new QLineEdit(this);
    nameEnter->setPlaceholderText("Enter name");
    nameEnter->setStyleSheet("QLineEdit{color: white; background: transparent; border: none;}");
    nameEnter->setAlignment(Qt::AlignCenter);
    nameEnter->setMaxLength(15);
    nameEnter->setFixedSize(544,92);
    nameEnter->move(118,194);
    nameEnter->setFont(QFont("Consolas", 30));

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
    delete leaderButton;
    delete quitButton;
}

void StartWindow::onPushButton()
{
    if (nameEnter->text().size() == 0)
    {
        QMessageBox::critical(this, "Warning", "Enter nickname!");
        return;
    }

    for (int i = 0; i < nameEnter->text().size(); ++i)
    {
        QChar sym = nameEnter->text()[i];
        if (sym == ' ' || sym == '.' || sym == ',' || sym == '-' || sym == '/' ||
            sym == '\\' || sym == '\'' || sym == '\"' || sym == '!' || sym == '@' ||
            sym == '#' || sym == '$' || sym == '%' || sym == '^' || sym == '&' ||
            sym == '*' || sym == '(' || sym == ')' || sym == '+' || sym == '=')
        {
            QMessageBox::critical(this, "Warning", "Use only a-zA-z!");
            return;
        }
    }

    this->hide();
    Tetris* game = new Tetris(nameEnter->text());
    game->show();
}

void StartWindow::onPushButton2()
{
    this->hide();
    ScoreTable* newWindow = new ScoreTable();
    newWindow->show();
}
