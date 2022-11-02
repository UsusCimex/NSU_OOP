#ifndef GAME_H
#define GAME_H

#include "blackjack.h"
#include "player.h"
#include "deck.h"
#include "factory.h"

class Game
{
public:
    Game(std::vector<Player*> & playerList);
    //Default function, start the desired game
    void start();
private:
    //A game with a full description of the actions
    std::vector<playerCharacters> detailedGame(std::vector<playerCharacters> &);
    //A game with a deduced result (Only bots)
    std::vector<playerCharacters> fastGame(std::vector<playerCharacters> &);
    //Start detailed games with combinations of players
    void tournamentGame();
    //Start fast games with combinations of players
    void tournamentfastGame();

    void UpgradeScore(std::vector<playerCharacters> &);

    std::vector<playerCharacters> players;
};

#endif