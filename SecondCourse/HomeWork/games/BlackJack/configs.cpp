#include "configs.h"

std::string TrivialBot1::makeAction()
{
    if (getScore() < 16) return "g";
    return "s";
}

std::string TrivialBot2::makeAction()
{
    if ((rand() % 100) < (getScore() * 100 / 24)) return "s";
    return "g";
}

std::string TrivialBot3::makeAction()
{
    if (rand() % 2) return "g";
    return "s";
}

std::string Bot1::makeAction()
{
    return "g";
}

std::string Bot2::makeAction()
{
    return "g";
}

std::string Bot3::makeAction()
{
    return "g";
}

Player* createTrivialBot1()
{
    return new TrivialBot1;
}

Player* createTrivialBot2()
{
    return new TrivialBot2;
}

Player* createTrivialBot3()
{
    return new TrivialBot3;
}

Player* createBot1()
{
    return new Bot1;
}

Player* createBot2()
{
    return new Bot2;
}

Player* createBot3()
{
    return new Bot3;
}