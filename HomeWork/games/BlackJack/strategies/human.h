#ifndef HUMAN_H
#define HUMAN_H

#include "../strategies.h"

class Human : public Strategies
{
    Action makeAction() override;
};

Strategies* CreateHuman();

#endif