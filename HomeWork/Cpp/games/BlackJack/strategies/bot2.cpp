#include "bot2.h"

void Bot2::generateStrategyTable() 
{
    strategyTable = GetStrategy("bot2", configFile);
}

Action Bot2::makeAction()
{
    if (strategyTable.empty()) generateStrategyTable();
    if (score >= 21) return Action::STAND;
    std::string a = strategyTable[score][enemyCard.power];
    if (a == "g") return Action::HIT;
    else if (a == "s") return Action::STAND;
    return Action::NOACTION;
}

Strategies* CreateBot2()
{
    return new Bot2;
}

auto bot2 = Factory<Strategies, std::string, Strategies *(*)()>::getInstance()->Register("-bot2", CreateBot2);