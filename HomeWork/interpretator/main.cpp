#include "interpretator.h"

Forth forth;
std::stack<int> stack;
std::vector<std::string> commander;

int main()
{
    (void)freopen("input.dll", "r", stdin);
    forth.start();
    return 0;
}