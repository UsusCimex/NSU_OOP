#ifndef GAME_H
#define GAME_H

#include "player.h"
#include "deck.h"
#include "factory.h"
#include "blackjack.h"

#include <iomanip>

class Game
{
public:
    //Default function, start the desired game
    void start(std::vector<PlayerCharacters> & playerList);

    Mode gameMode = Mode::NONE;
    std::string configFile = "";
    int decksCount = 1;

    int playerCount = 0;
private:
    //A game with a full description of the actions
    std::vector<PlayerCharacters> detailedGame(std::vector<PlayerCharacters> &);
    //A game with a deduced result (Only bots)
    std::vector<PlayerCharacters> fastGame(std::vector<PlayerCharacters> &);
    //Start detailed games with combinations of players
    std::vector<PlayerCharacters> tournamentGame(std::vector<PlayerCharacters> &);
    //Start fast games with combinations of players
    std::vector<PlayerCharacters> tournamentfastGame(std::vector<PlayerCharacters> &);

    void UpgradeTournamentScore(std::vector<PlayerCharacters> &, std::vector<PlayerCharacters> &, size_t &, size_t &);
};

#endif