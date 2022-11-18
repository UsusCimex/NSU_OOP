#ifndef METABOT_H
#define METABOT_H

#include "../strategies.h"

class MetaBot : public Strategies
{
public:
    void generateStrategyTable();
    Action makeAction() override;
private:
    std::vector <std::vector<std::string>> riskStrategy;
    std::vector <std::vector<std::string>> normStrategy;
};

Strategies* CreateMetaBot();

#endif