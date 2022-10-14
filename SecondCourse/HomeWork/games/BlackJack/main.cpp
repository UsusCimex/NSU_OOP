#include "blackjack.h"
#include "deck.h"
#include "player.h"
#include "game.h"

void SettingRules(std::string arg, Rules & rules)
{
    if (arg.compare("--mode=detailed") == 0) rules.mode = DETAILED;
    else if (arg.compare("--mode=fast") == 0) rules.mode = FAST;
    else if (arg.compare("--mode=tournament") == 0) rules.mode = TOURNAMENT;
    else if (arg.compare(0, 10, "--configs=") == 0) rules.configFile = arg.substr(arg.find('=') + 1);
    else { rules.players.push_back(arg); rules.playerCount++; }
}

int main(int argc, char ** argv)
{
    if (argc < 2) throw std::invalid_argument("Enter something...");
    
    Rules rules;
    for (size_t i = 1; i < argc; ++i) SettingRules(std::string(argv[i]), rules);
    
    if (rules.playerCount < 2) throw std::invalid_argument("Enter some players...");
    if (rules.mode == NONE)
        if (rules.playerCount == 2)
            rules.mode = DETAILED;
        else
            rules.mode = TOURNAMENT;

    Game game(rules);
    game.start();
    return 0;
}