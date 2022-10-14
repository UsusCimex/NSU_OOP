#ifndef GAME_H
#define GAME_H

#include "blackjack.h"
#include "player.h"
#include "deck.h"

class Game
{
public:
    Game(Rules rules) : rules(rules) {}
    void start();
    std::vector<Player*> detailedGame(std::vector<Player*>);
    std::vector<Player*> fastGame(std::vector<Player*>); //only bots
    void tournamentGame(std::vector<Player*>);
    void tournamentfastGame(std::vector<Player*>); //only bots
private:
    Rules rules;
};

#endif