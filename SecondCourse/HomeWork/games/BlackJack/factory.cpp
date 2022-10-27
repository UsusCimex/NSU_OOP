#include "factory.h"

Factory::Factory()
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

Player* Factory::CreateBot(const std::string& name) 
{
    if (Strategies_.count(name) == 0) return nullptr;
    return Strategies_[name]();
}

void Factory::RegisterStrategy(const std::string& name, Player * (*creator)()) 
{
    Strategies_[name] = creator;
}