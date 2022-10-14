#ifndef BLACKJACK_H
#define BLACKJACK_H

#include <iostream>
#include <string>
#include <array>
#include <vector>

#include <time.h>

enum Mode
{
    NONE,
    DETAILED,
    FAST,
    TOURNAMENT
};

struct Rules
{
    std::vector<std::string> players;
    Mode mode = NONE;
    std::string configFile;

    size_t playerCount = 0ull;
};

#define DECKSIZE 52

#endif