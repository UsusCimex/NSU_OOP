#include "bot1.h"

void Bot1::generateStrategyTable()
{
    strategyTable = GetStrategy("bot1", configFile);
}

Action Bot1::makeAction()
{
    if (strategyTable.empty()) generateStrategyTable();
    if (score >= 21) return Action::STAND;
    std::string a = strategyTable[score][enemyCard.power];
    if (a == "g") return Action::HIT;
    else if (a == "s") return Action::STAND;
    return Action::NOACTION;
}

Strategies* CreateBot1()
{
    return new Bot1;
}

auto bot1 = Factory<Strategies, std::string, Strategies *(*)()>::getInstance()->Register("-bot1", CreateBot1);