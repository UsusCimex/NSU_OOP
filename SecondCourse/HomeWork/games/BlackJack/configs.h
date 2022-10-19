#ifndef CONFIGS_H
#define CONFIGS_H

#include "blackjack.h"

#include "player.h"

class TestBot1 : public Player 
{
public:
    TestBot1() : Player("TestBot1") {}
    std::string makeAction() override;
};

Player* createTestBot1();

#endif