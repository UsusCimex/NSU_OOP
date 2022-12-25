#ifndef SCORETABLE_H
#define SCORETABLE_H

#include "score.h"

#include <QWidget>
#include <QApplication>
#include <QMainWindow>
#include <QBoxLayout>

class ScoreTable : public QWidget
{
    Q_OBJECT
public:
    ScoreTable(QWidget* parent = nullptr);
    ~ScoreTable() override;
private:
    QVBoxLayout* layout;
    Score score;
};

#endif // SCORETABLE_H
