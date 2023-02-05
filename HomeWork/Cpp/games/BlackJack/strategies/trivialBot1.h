#ifndef TBOT1_H
#define TBOT1_H

#include "../strategies.h"

class TrivialBot1 : public Strategies 
{
public:
    Action makeAction() override;
};

Strategies* CreateTrivialBot1();

#endif