#ifndef FACTORY_H
#define FACTORY_H

#include "blackjack.h"
#include "strategies.h"

template <typename key, typename value>
class Factory
{
public:
    Factory()
    {
        RegisterStrategy("-trivialBot1", CreateTrivialBot1);
        RegisterStrategy("-trivialbot1", CreateTrivialBot1);
        RegisterStrategy("-trivialBot2", CreateTrivialBot2);
        RegisterStrategy("-trivialbot2", CreateTrivialBot2);
        RegisterStrategy("-trivialBot3", CreateTrivialBot3);
        RegisterStrategy("-trivialbot3", CreateTrivialBot3);
        RegisterStrategy("-bot1", CreateBot1);
        RegisterStrategy("-bot2", CreateBot2);
        RegisterStrategy("-metabot", CreateMetaBot);
    }
    Player* CreateBot(const key& name)
    {
        if (Strategies_.count(name) == 0) return nullptr;
        return Strategies_[name]();
    }
    void RegisterStrategy(const key& name, value * (*creator)())
    {
        Strategies_[name] = creator;
    }
private:
    std::map <key, value * (*)()> Strategies_;
};

#endif