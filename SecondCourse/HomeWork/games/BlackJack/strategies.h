#ifndef STRATEGIES_H
#define STRATEGIES_H

#include "deck.h"
#include "fstream"
#include "factory.h"
#include "blackjack.h"

//Load CSV file from patch
std::vector<std::vector<std::string>> GetStrategy(std::string && name, std::string & configFile);

class Player
{
public:
    //Choice of action
    virtual Action makeAction();
    Card enemyCard;
    std::string configFile;

    //There is no point in changing the following variables, because they are updated in the game.
    //And only needed for execution makeAction.
    std::vector<Card> hand;
    int score = 0;
protected:
    std::vector <std::vector<std::string>> strategyTable;
};

class TrivialBot1 : public Player 
{
public:
    Action makeAction() override;
};

class TrivialBot2 : public Player 
{
public:
    Action makeAction() override;
};

class TrivialBot3 : public Player 
{
public:
    Action makeAction() override;
};

class Bot1 : public Player 
{
public:
    void generateStrategyTable();
    Action makeAction() override;
};

class Bot2 : public Player
{
public:
    void generateStrategyTable();
    Action makeAction() override;
};

class MetaBot : public Player
{
public:
    void generateStrategyTable();
    Action makeAction() override;
private:
    std::vector <std::vector<std::string>> riskStrategy;
    std::vector <std::vector<std::string>> normStrategy;
};

Player* CreateTrivialBot1();
Player* CreateTrivialBot2();
Player* CreateTrivialBot3();
Player* CreateBot1();
Player* CreateBot2();
Player* CreateMetaBot();

#endif