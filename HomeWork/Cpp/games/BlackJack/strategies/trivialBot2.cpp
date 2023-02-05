#include "trivialBot2.h"

Action TrivialBot2::makeAction()
{
    if (score >= 21) return Action::STAND;
    if ((rand() % 100) < (score * 100 / 24)) return Action::STAND;
    return Action::HIT;
}

Strategies* CreateTrivialBot2()
{
    return new TrivialBot2;
}

auto tBot2 = Factory<Strategies, std::string, Strategies *(*)()>::getInstance()->Register("-trivialBot2", CreateTrivialBot2);
auto tBot21 = Factory<Strategies, std::string, Strategies *(*)()>::getInstance()->Register("-trivialbot2", CreateTrivialBot2);