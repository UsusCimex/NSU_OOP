#ifndef GAME_H
#define GAME_H

#include "blackjack.h"
#include "player.h"
#include "deck.h"
#include "factory.h"

class Game
{
public:
    Game(std::vector<Player*> & players) : players(players) {}
    //Default function, start the desired game
    void start();
    //A game with a full description of the actions
    std::vector<Player*> detailedGame(std::vector<Player*>);
    //A game with a deduced result (Only bots)
    std::vector<Player*> fastGame(std::vector<Player*>);
    //Start detailed games with combinations of players
    void tournamentGame();
    //Start fast games with combinations of players
    void tournamentfastGame();
private:
    std::vector<Player*> players;
};

#endif