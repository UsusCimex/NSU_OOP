#include "blackjack.h"
#include "deck.h"
#include "player.h"
#include "game.h"

int main(int argc, char ** argv)
{
    if (argc < 2) throw std::invalid_argument("Enter something...");
    Rules rules;
    for (size_t i = 1; i < argc; ++i)
    {
        std::string gt = argv[i];
        if (gt.compare("-playerCount=") > 0)
        {
            std::string n = gt.substr(gt.find('=') + 1);
            rules.playerCount = atoi(n.c_str());
        }
    }
    
    Game game;
    game.start(rules);
    return 0;
}