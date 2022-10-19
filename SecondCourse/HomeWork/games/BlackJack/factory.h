#ifndef FACTORY_H
#define FACTORY_H

#include "blackjack.h"
#include "player.h"

#include "configs.h" //config file

class Factory {
public:
    Factory()
    {
        RegisterStrategy("-trivialBot1", createTrivialBot1);
        RegisterStrategy("-trivialBot2", createTrivialBot2);
        RegisterStrategy("-trivialBot3", createTrivialBot3);
        RegisterStrategy("-bot1", createBot1);
        RegisterStrategy("-bot2", createBot2);
        RegisterStrategy("-bot3", createBot3);
    }

    Player* CreateBot(const std::string& name) {
        if (Strategies_.count(name) == 0) return nullptr;
        return Strategies_[name]();
    }

    void RegisterStrategy(const std::string& name, Player * (*creator)()) {
        Strategies_[name] = creator;
    }
private:
    std::map <std::string, Player * (*)()> Strategies_;
};

#endif