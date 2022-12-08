#include "tetris.h"
#include "startwindow.h"
#include <QApplication>
#include <QtCore>
#include <QWidget>
#include <QPushButton>
#include <time.h>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);

    StartWindow w;
    w.show();

    return a.exec();
}
