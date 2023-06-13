#include "metaBot.h"

void MetaBot::generateStrategyTable()
{
    riskStrategy = GetStrategy("bot1", configFile);
    normStrategy = GetStrategy("bot2", configFile);
}

Action MetaBot::makeAction()
{
    if (riskStrategy.empty() || normStrategy.empty()) generateStrategyTable();
    if (score >= 21) return Action::STAND;
    
    std::string a;
    if (enemyCard.power >= 8) a = riskStrategy[score][enemyCard.power];
    else a = normStrategy[score][enemyCard.power];

    if (a == "g") return Action::HIT;
    else if (a == "s") return Action::STAND;
    return Action::NOACTION;
}

Strategies* CreateMetaBot()
{
    return new MetaBot;
}

auto bot3 = Factory<Strategies, std::string, Strategies *(*)()>::getInstance()->Register("-metabot", CreateMetaBot);