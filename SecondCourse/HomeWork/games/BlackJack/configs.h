#ifndef CONFIGS_H
#define CONFIGS_H

#include "blackjack.h"

#include "player.h"

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
    Bot1() : Player("Bot1") {}
    std::string makeAction() override;
};

class Bot2 : public Player 
{
public:
    Bot2() : Player("Bot2") {}
    std::string makeAction() override;
};

class Bot3 : public Player 
{
public:
    Bot3() : Player("Bot3") {}
    std::string makeAction() override;
};

Player* createTrivialBot1();
Player* createTrivialBot2();
Player* createTrivialBot3();
Player* createBot1();
Player* createBot2();
Player* createBot3();

#endif