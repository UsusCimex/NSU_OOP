#ifndef FACTORY_H
#define FACTORY_H

#include "blackjack.h"
#include "player.h"
#include "strategies.h"

class Factory {
public:
    Factory() {}

    Player* CreateBot(const std::string&) {}

    void RegisterStrategy(const std::string&, Player * (*)()) {}
protected:
    std::map <std::string, Player * (*)()> Strategies_;
};

#endif