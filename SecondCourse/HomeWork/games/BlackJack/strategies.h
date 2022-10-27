#ifndef STRATEGIES_H
#define STRATEGIES_H

#include "blackjack.h"
#include "player.h"
#include "fstream"

//Load CSV file from patch
char ** GetStrategy(std::string name);

class TrivialBot1 : public Player 
{
public:
    TrivialBot1() : Player("TrivialBot1") {}
    std::string makeAction(Player * enemy) override;
};

class TrivialBot2 : public Player 
{
public:
    TrivialBot2() : Player("TrivialBot2") {}
    std::string makeAction(Player * enemy) override;
};

class TrivialBot3 : public Player 
{
public:
    TrivialBot3() : Player("TrivialBot3") {}
    std::string makeAction(Player * enemy) override;
};

class Bot1 : public Player 
{
public:
    Bot1();
    ~Bot1();
    std::string makeAction(Player * enemy) override;
private:
    char ** strategyTable = nullptr;
};

class Bot2 : public Player
{
public:
    Bot2();
    ~Bot2();
    std::string makeAction(Player * enemy) override;
private:
   char ** strategyTable = nullptr;
};

class MetaBot : public Player
{
public:
    MetaBot();
    ~MetaBot();
    std::string makeAction(Player * enemy) override;
private:
    char ** riskStrategy = nullptr;
    char ** normStrategy = nullptr;
};

Player* CreateTrivialBot1();
Player* CreateTrivialBot2();
Player* CreateTrivialBot3();
Player* CreateBot1();
Player* CreateBot2();
Player* CreateMetaBot();

#endif