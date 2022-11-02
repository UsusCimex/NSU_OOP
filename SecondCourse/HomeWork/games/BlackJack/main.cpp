#include "blackjack.h"
#include "deck.h"
#include "strategies.h"
#include "game.h"
#include "factory.h"

Rules rules;

//Parameter handler
void SettingRules(std::string arg)
{
    if (arg.compare("--mode=detailed") == 0) rules.mode = DETAILED;
    else if (arg.compare("--mode=fast") == 0) rules.mode = FAST;
    else if (arg.compare("--mode=tournament") == 0) rules.mode = TOURNAMENT;
    else if (arg.compare("--mode=tournamentfast") == 0 || arg.compare("--mode=fasttournament") == 0) rules.mode = TOURNAMENTFAST; 
    else if (arg.compare(0, 9, "--config=") == 0 || arg.compare(0, 13, "--configfile=") == 0) rules.configFile = arg.substr(arg.find('=') + 1);
    else if (arg.compare(0, 13, "--countdecks=") == 0 || arg.compare(0, 13, "--deckscount=") == 0) rules.decksCount = stoi(arg.substr(arg.find('=') + 1));
    else { rules.players.push_back(arg); rules.playerCount++; }
}

int main(int argc, char ** argv)
{
    srand(time(0));

    if (argc < 2) throw std::invalid_argument("Enter something...");
    
    for (int i = 1; i < argc; ++i) SettingRules(std::string(argv[i]));
    
    if (rules.playerCount < 2) throw std::invalid_argument("Enter some players...");
    if (rules.mode == NONE)
    {
        if (rules.playerCount == 2)
        {
            rules.mode = DETAILED;
        }
        else
        {
            rules.mode = TOURNAMENT;
        }
    }
    if ((rules.mode == DETAILED || rules.mode == FAST) && rules.playerCount > 2)
    {
        std::cerr << "mode changed to tournament" << std::endl;
        rules.mode = TOURNAMENT;
    }

    if (rules.mode == FAST || rules.mode == TOURNAMENTFAST)
        for (auto pl : rules.players)
            if (pl[0] != '-')
                throw std::invalid_argument("Only bots can participate in Fast and TournamentFast modes!");
    
    if (rules.decksCount < 1) throw std::invalid_argument("Minimal decks count is 1!");

    Factory<std::string, Player> fac;
    std::vector<Player*> players;

    for (auto pl : rules.players)
    {
        auto rm = fac.CreateBot(pl);
        if (rm == nullptr && pl[0] == '-') throw std::invalid_argument("Strategy not found...");
        if (rm == nullptr) rm = new Player(pl);
        players.push_back(rm);
    }

    Game game(players);
    game.start();
    return 0;
}