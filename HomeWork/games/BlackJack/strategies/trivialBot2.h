#ifndef TBOT2_H
#define TBOT2_H

#include "../strategies.h"

class TrivialBot2 : public Strategies 
{
public:
    Action makeAction() override;
};

Strategies* CreateTrivialBot2();
#endif