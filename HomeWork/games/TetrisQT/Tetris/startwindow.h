#ifndef STARTWINDOW_H
#define STARTWINDOW_H

#include "tetris.h"

#include <QWidget>
#include <QPushButton>
#include <QBrush>
#include <QPalette>

class StartWindow : public QWidget
{
public:
    StartWindow(QWidget* parent = nullptr);
    ~StartWindow();
private:
    QPushButton* startButton;
    QPushButton* quitButton;
    QBrush* brush;
    QPalette* palette;
private slots:
    void onPushButton();
};

#endif // STARTWINDOW_H
