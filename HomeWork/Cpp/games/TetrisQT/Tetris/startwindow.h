#ifndef STARTWINDOW_H
#define STARTWINDOW_H

#include "tetrisqt.h"
#include "scoretable.h"

#include <QWidget>
#include <QPushButton>
#include <QBrush>
#include <QLineEdit>
#include <QPalette>

class StartWindow : public QWidget
{
public:
    StartWindow(QWidget* parent = nullptr);
    ~StartWindow() override;
private:
    TetrisQT* game;
    ScoreTable* scoreBoard;

    QLineEdit* nameEnter;
    QPushButton* startButton;
    QPushButton* quitButton;
    QPushButton* leaderButton;
    QBrush* brush;
    QPalette* palette;
signals:
    void sMenu();
private slots:
    void onPushButton();
    void onPushButton2();
};

#endif // STARTWINDOW_H
