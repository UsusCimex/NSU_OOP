#include "tetris.h"
#include <QApplication>
#include <time.h>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);

    srand(time(0));

    Tetris w;
    w.show();

    return a.exec();
}
