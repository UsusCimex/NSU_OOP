#include "scoretable.h"

#include <fstream>
#include <QLabel>
#include <QVector>

ScoreTable::ScoreTable(QWidget* parent)
{
    Q_UNUSED(parent);
    QVector<PlayerStats> players = score.getPlayers();
    for (int i = 0; i < players.size(); ++i)
    {
        QLabel label(players[i].name.c_str());
        layout->addWidget(&label);
    }
    setLayout(layout);
}
