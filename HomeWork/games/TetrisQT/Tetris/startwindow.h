#ifndef STARTWINDOW_H
#define STARTWINDOW_H

#include "tetris.h"
#include "scoretable.h"

#include <QWidget>
#include <QPushButton>
#include <QBrush>
#include <QPalette>

class StartWindow : public QWidget
{
public:
    StartWindow(QWidget* parent = nullptr);
    ~StartWindow() override;
private:
    QPushButton* startButton;
    QPushButton* quitButton;
    QPushButton* leaderButton;
    QBrush* brush;
    QPalette* palette;
private slots:
    void onPushButton();
    void onPushButton2();
};

#endif // STARTWINDOW_H
