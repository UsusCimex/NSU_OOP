#include "trivialBot3.h"

Action TrivialBot3::makeAction()
{
    if (score >= 21) return Action::STAND;
    if (rand() % 2) return Action::HIT;
    return Action::STAND;
}

Strategies* CreateTrivialBot3()
{
    return new TrivialBot3;
}

auto tBot3 = Factory<Strategies, std::string, Strategies *(*)()>::getInstance()->Register("-trivialBot3", CreateTrivialBot3);
auto tBot31 = Factory<Strategies, std::string, Strategies *(*)()>::getInstance()->Register("-trivialbot3", CreateTrivialBot3);