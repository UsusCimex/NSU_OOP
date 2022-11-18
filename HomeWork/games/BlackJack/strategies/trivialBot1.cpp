#include "trivialBot1.h"

Action TrivialBot1::makeAction()
{
    if (score < 16) return Action::HIT;
    return Action::STAND;
}

Strategies* CreateTrivialBot1()
{
    return new TrivialBot1;
}

auto tBot1 = Factory<Strategies, std::string, Strategies *(*)()>::getInstance()->Register("-trivialBot1", CreateTrivialBot1);
auto tBot11 = Factory<Strategies, std::string, Strategies *(*)()>::getInstance()->Register("-trivialbot1", CreateTrivialBot1);