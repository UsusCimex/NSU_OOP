#ifndef SCORE_H
#define SCORE_H

#include <QVector>
#include <fstream>

struct PlayerStats
{
    std::string name;
    int score;
};

class Score
{
public:
    Score();
    QVector<PlayerStats> getPlayers();
    void UpdateScore(std::string name, int score);
private:
    QVector<PlayerStats> players;

    std::ifstream readScore;
    std::ofstream writeScore;
};

#endif // SCORE_H
