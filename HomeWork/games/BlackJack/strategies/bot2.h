#ifndef BOT2_H
#define BOT2_H

#include "../strategies.h"

class Bot2 : public Strategies
{
public:
    void generateStrategyTable();
    Action makeAction() override;
};

Strategies* CreateBot2();

#endif