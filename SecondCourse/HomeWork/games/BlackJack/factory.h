#ifndef FACTORY_H
#define FACTORY_H

#include "blackjack.h"
#include "player.h"

#include "configs.h" //config file

class Factory {
public:
    Factory()
    {
        RegisterStrategy("-testBot1", createTestBot1);
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