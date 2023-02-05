#ifndef GAME_H
#define GAME_H

#include "player.h"
#include "deck.h"
#include "factory.h"
#include "blackjack.h"
#include "gui.h"

class Game
{
public:
    //Default function, start the desired game
    void start(std::vector<Player> & playerList);

    Mode gameMode = Mode::NONE;
    std::string configFile = "";
    int decksCount = 1;

    int playerCount = 0;
private:
    //A game with a full description of the actions
    std::vector<Player> detailedGame(std::vector<Player> &);
    //A game with a deduced result (Only bots)
    std::vector<Player> fastGame(std::vector<Player> &);
    //Start detailed games with combinations of players
    std::vector<Player> tournamentGame(std::vector<Player> &);
    //Start fast games with combinations of players
    std::vector<Player> tournamentfastGame(std::vector<Player> &);

    void UpgradeTournamentScore(std::vector<Player> &, std::vector<Player> &, size_t &, size_t &);
};

#endif