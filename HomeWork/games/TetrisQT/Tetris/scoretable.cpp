#include "scoretable.h"

#include <fstream>
#include <QLabel>
#include <string>
#include <QVector>
#include <QDebug>

ScoreTable::ScoreTable(QWidget* parent)
    : QWidget(parent)
{
    this->resize(500,500);
    this->setFixedSize(500,500);
    layout = new QVBoxLayout;
    QVector<PlayerStats> players = score.getPlayers();
    for (int i = 0; i < players.size(); ++i)
    {
        std::string res = players[i].name + " " + std::to_string(players[i].score);
        QLabel* label = new QLabel(res.c_str());
        layout->addWidget(label);
    }
    layout->setAlignment(Qt::AlignCenter);
    setLayout(layout);
}

ScoreTable::~ScoreTable()
{
    delete layout;
}
