#ifndef BOT1_H
#define BOT1_H

#include "../strategies.h"
#include <iostream>

class Bot1 : public Strategies 
{
public:
    void generateStrategyTable();
    Action makeAction() override;
};

Strategies* CreateBot1();

#endif