#ifndef FACTORY_H
#define FACTORY_H

#include "blackjack.h"
#include "player.h"
#include "strategies.h"

template <typename key, typename value>
class Factory {
public:
    Factory();
    Player* CreateBot(const key&);
    void RegisterStrategy(const key&, value * (*)());
protected:
    std::map <key, value * (*)()> Strategies_;
};

#endif