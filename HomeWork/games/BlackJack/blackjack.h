#ifndef BLACKJACK_H
#define BLACKJACK_H

constexpr int kDeckSize = 52;

enum Mode
{
    NONE,
    DETAILED,
    FAST,
    TOURNAMENT,
    TOURNAMENTFAST
};

enum Action
{
    NOACTION,
    HIT,
    STAND
};

#endif