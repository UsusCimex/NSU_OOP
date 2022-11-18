#ifndef STRATEGIES_H
#define STRATEGIES_H

#include "deck.h"
#include "fstream"
#include "factory.h"
#include "blackjack.h"

#include <vector>

//Load CSV file from patch
std::vector<std::vector<std::string>> GetStrategy(std::string name, std::string & configFile);

class Strategies
{
public:
    //Choice of action
    virtual Action makeAction();
    void setConfigFile(std::string);
    void setScore(int);
    void setEnemyCard(Card);
    Card getEnemyCard();
    void setHand(std::vector<Card>);
protected:
    Card enemyCard;
    std::string configFile;
    
    //There is no point in changing the following variables, because they are updated in the game.
    //And only needed for execution makeAction.
    std::vector<Card> hand;
    int score = 0;
    std::vector <std::vector<std::string>> strategyTable;
};

#endif