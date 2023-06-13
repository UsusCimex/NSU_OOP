#ifndef TBOT3_H
#define TBOT3_H

#include "../strategies.h"

class TrivialBot3 : public Strategies 
{
public:
    Action makeAction() override;
};

Strategies* CreateTrivialBot3();

#endif