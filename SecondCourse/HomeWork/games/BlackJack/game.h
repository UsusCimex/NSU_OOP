#ifndef GAME_H
#define GAME_H

#include "blackjack.h"
#include "player.h"
#include "deck.h"
#include "factory.h"

class Game
{
public:
    Game(Rules & rules, std::vector<Player*> & players) : rules(rules), players(players) {}
    void start();
    std::vector<Player*> detailedGame(std::vector<Player*>);
    std::vector<Player*> fastGame(std::vector<Player*>); //only bots
    void tournamentGame();
    void tournamentfastGame(); //only bots
private:
    Rules rules;
    std::vector<Player*> players;
};

#endif