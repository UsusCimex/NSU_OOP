#include "configs.h"

std::string TestBot1::makeAction()
{
    return "g";
}

Player* createTestBot1()
{
    return new TestBot1;
}