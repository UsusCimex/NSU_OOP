#ifndef SCORETABLE_H
#define SCORETABLE_H

#include "score.h"

#include <QWidget>
#include <QApplication>
#include <QMainWindow>
#include <QKeyEvent>
#include <QVector>
#include <QLabel>
#include <QBoxLayout>

class ScoreTable : public QWidget
{
    Q_OBJECT
public:
    ScoreTable(QWidget* parent = nullptr);
    ~ScoreTable() override;
    void updateScore();

signals:
    void sTable();

private slots:
    void keyPressEvent(QKeyEvent *) override;

private:
    QVBoxLayout* layout;
    QVector<QLabel*> topList;

    QBrush* brush;
    QPalette* palette;
};

#endif // SCORETABLE_H
