#ifndef STRATEGIES_H
#define STRATEGIES_H

#include "deck.h"
#include "fstream"
#include "factory.h"
#include "blackjack.h"

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

class Human : public Strategies
{
    Action makeAction() override;
};

class TrivialBot1 : public Strategies 
{
public:
    Action makeAction() override;
};

class TrivialBot2 : public Strategies 
{
public:
    Action makeAction() override;
};

class TrivialBot3 : public Strategies 
{
public:
    Action makeAction() override;
};

class Bot1 : public Strategies 
{
public:
    void generateStrategyTable();
    Action makeAction() override;
};

class Bot2 : public Strategies
{
public:
    void generateStrategyTable();
    Action makeAction() override;
};

class MetaBot : public Strategies
{
public:
    void generateStrategyTable();
    Action makeAction() override;
private:
    std::vector <std::vector<std::string>> riskStrategy;
    std::vector <std::vector<std::string>> normStrategy;
};

Strategies* CreateHuman();
Strategies* CreateTrivialBot1();
Strategies* CreateTrivialBot2();
Strategies* CreateTrivialBot3();
Strategies* CreateBot1();
Strategies* CreateBot2();
Strategies* CreateMetaBot();

#endif