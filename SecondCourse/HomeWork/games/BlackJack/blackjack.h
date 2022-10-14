#ifndef BLACKJACK_H
#define BLACKJACK_H

#include <iostream>
#include <string>
#include <array>
#include <vector>
#include <map>

#include <time.h>

#define DECKSIZE 52

enum Mode
{
    NONE,
    DETAILED,
    FAST,
    TOURNAMENT,
    TOURNAMENTFAST
};

struct Rules
{
    std::vector<std::string> players;
    Mode mode = NONE;
    std::string configFile;

    size_t playerCount = 0ull;
};

#endif