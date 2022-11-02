#ifndef STRATEGIES_H
#define STRATEGIES_H

#include "blackjack.h"
#include "deck.h"
#include "fstream"

//Load CSV file from patch
char ** GetStrategy(std::string name);

class Player
{
public:
    Player(std::string name);
    std::string name;
    //Choice of action
    virtual std::string makeAction();
    Card enemyCard;

    //There is no point in changing the following variables, because they are updated in the game.
    //And only needed for execution makeAction.
    std::vector<Card> hand;
    int score = 0;
};

class TrivialBot1 : public Player 
{
public:
    TrivialBot1() : Player("TrivialBot1") {}
    std::string makeAction() override;
};

class TrivialBot2 : public Player 
{
public:
    TrivialBot2() : Player("TrivialBot2") {}
    std::string makeAction() override;
};

class TrivialBot3 : public Player 
{
public:
    TrivialBot3() : Player("TrivialBot3") {}
    std::string makeAction() override;
};

class Bot1 : public Player 
{
public:
    Bot1();
    ~Bot1();
    std::string makeAction() override;
private:
    char ** strategyTable = nullptr;
};

class Bot2 : public Player
{
public:
    Bot2();
    ~Bot2();
    std::string makeAction() override;
private:
   char ** strategyTable = nullptr;
};

class MetaBot : public Player
{
public:
    MetaBot();
    ~MetaBot();
    std::string makeAction() override;
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