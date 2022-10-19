#ifndef BLACKJACK_H
#define BLACKJACK_H

#include <iostream>
#include <string>
#include <vector>
#include <map>
#include <algorithm>

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
    int decksCount = 1;

    int playerCount = 0;
};

#endif