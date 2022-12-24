#include "score.h"

Score::Score()
{
    readScore.open("bestScore.dll");
    if (!readScore.is_open())
    {
        writeScore.open("bestScore.dll");
        for (int i = 0; i < 10; ++i)
        {
            writeScore << "NO_RECORD ";
            writeScore << "0\n";
        }
        writeScore.close();
        readScore.open("bestScore.dll");
    }

    for (int i = 0; i < 10; ++i)
    {
        PlayerStats pl;
        readScore >> pl.name;
        readScore >> pl.score;
        players.push_back(pl);
    }
    readScore.close();
}

QVector<PlayerStats> Score::getPlayers()
{
    return players;
}

void Score::UpdateScore(std::string name, int score)
{
    if (players.back().score < score)
    {
        players.pop_back();
        PlayerStats pl;
        pl.name = name;
        pl.score = score;
        players.push_back(pl);

        for (int i = 0; i < 10; ++i)
        {
            if (players.back().score > players[i].score)
            {
                std::swap(players[i], players.back());
            }
        }
    }

    writeScore.open("bestScore.dll");
    for (int i = 0; i < 10; ++i)
    {
        writeScore << players[i].name << " ";
        writeScore << players[i].score << "\n";
    }

    writeScore.close();
}
