#include "human.h"

Action Human::makeAction()
{
    std::cout << "Enter something(h - hit, s - stand): ";
    std::string status;
    std::cin >> status;
    if (status == "hit" || status == "h" || status == "g") return Action::HIT;
    if (status == "stand" || status == "s") return Action::STAND;
    return Action::NOACTION;
}

Strategies* CreateHuman()
{
    return new Human;
}

auto human = Factory<Strategies, std::string, Strategies *(*)()>::getInstance()->Register("human", CreateHuman);