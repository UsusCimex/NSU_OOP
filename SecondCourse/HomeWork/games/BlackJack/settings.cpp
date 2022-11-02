#include "blackjack.h"
#include "deck.h"
#include "strategies.h"
#include "game.h"
#include "factory.h"

namespace{
    //Parameter handler
    void SettingRules(std::string arg, Game & game, std::vector<std::string> & playerList)
    {
        if (arg.compare("--mode=detailed") == 0) game.gameMode = DETAILED;
        else if (arg.compare("--mode=fast") == 0) game.gameMode = FAST;
        else if (arg.compare("--mode=tournament") == 0) game.gameMode = TOURNAMENT;
        else if (arg.compare("--mode=tournamentfast") == 0 || arg.compare("--mode=fasttournament") == 0) game.gameMode = TOURNAMENTFAST; 
        else if (arg.compare(0, 9, "--config=") == 0 || arg.compare(0, 13, "--configfile=") == 0) game.configFile = arg.substr(arg.find('=') + 1);
        else if (arg.compare(0, 13, "--countdecks=") == 0 || arg.compare(0, 13, "--deckscount=") == 0) game.decksCount = stoi(arg.substr(arg.find('=') + 1));
        else { playerList.push_back(arg); game.playerCount++; }
    }

    void StartSetting(int argc, char * argv[])
    {
        srand(time(0));
        Game game;
        std::vector<std::string> playerList;
        if (argc < 2) throw std::invalid_argument("Enter something...");
        
        for (int i = 1; i < argc; ++i) SettingRules(std::string(argv[i]), game, playerList);
        
        if (game.playerCount < 2) throw std::invalid_argument("Enter some players...");
        if (game.gameMode == NONE)
        {
            if (game.playerCount == 2)
            {
                game.gameMode = DETAILED;
            }
            else
            {
                game.gameMode = TOURNAMENT;
            }
        }
        if ((game.gameMode == DETAILED || game.gameMode == FAST) && game.playerCount > 2)
        {
            std::cerr << "mode changed to tournament" << std::endl;
            game.gameMode = TOURNAMENT;
        }

        if (game.gameMode == FAST || game.gameMode == TOURNAMENTFAST)
            for (auto pl : playerList)
                if (pl[0] != '-')
                    throw std::invalid_argument("Only bots can participate in Fast and TournamentFast modes!");
        
        if (game.decksCount < 1) throw std::invalid_argument("Minimal decks count is 1!");

        Factory<std::string, Player> fac;
        std::vector<Player*> players;

        for (auto pl : playerList)
        {
            auto newPlayer = fac.CreateBot(pl);
            if (newPlayer == nullptr && pl[0] == '-') throw std::invalid_argument("Strategy not found...");
            if (newPlayer == nullptr) newPlayer = new Player(pl);
            players.push_back(newPlayer);
        }
        game.start(players);
    }
}