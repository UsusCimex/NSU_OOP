#include "factory.h"
#include "deck.h"
#include "strategies.h"
#include "game.h"
#include "blackjack.h"

namespace {
    //Parameter handler
    void SettingRules(std::string arg, Game & game, std::vector<std::string> & playerList)
    {
        if (arg.compare("--mode=detailed") == 0) game.gameMode = Mode::DETAILED;
        else if (arg.compare("--mode=fast") == 0) game.gameMode = Mode::FAST;
        else if (arg.compare("--mode=tournament") == 0) game.gameMode = Mode::TOURNAMENT;
        else if (arg.compare("--mode=tournamentfast") == 0 || arg.compare("--mode=fasttournament") == 0) game.gameMode = Mode::TOURNAMENTFAST; 
        else if (arg.compare(0, 9, "--config=") == 0 || arg.compare(0, 13, "--configfile=") == 0) game.configFile = arg.substr(arg.find('=') + 1);
        else if (arg.compare(0, 13, "--countdecks=") == 0 || arg.compare(0, 13, "--deckscount=") == 0) game.decksCount = stoi(arg.substr(arg.find('=') + 1));
        else { playerList.push_back(arg); game.playerCount++; }
    }

    void StartSetting(int argc, const char * argv[])
    {
        srand(time(0));
        Game game;
        std::vector<std::string> playerList;
        if (argc < 2) throw std::invalid_argument("Enter something...");
        
        for (int i = 1; i < argc; ++i) SettingRules(std::string(argv[i]), game, playerList);
        
        if (game.playerCount < 2) throw std::invalid_argument("Enter some players...");
        if (game.gameMode == Mode::NONE)
        {
            if (game.playerCount == 2)
            {
                game.gameMode = Mode::DETAILED;
            }
            else
            {
                game.gameMode = Mode::TOURNAMENT;
            }
        }
        if ((game.gameMode == Mode::DETAILED || game.gameMode == Mode::FAST) && game.playerCount > 2)
        {
            std::cerr << "mode changed to tournament" << std::endl;
            game.gameMode = Mode::TOURNAMENT;
        }

        if (game.gameMode == Mode::FAST || game.gameMode == Mode::TOURNAMENTFAST)
            for (auto pl : playerList)
                if (pl[0] != '-')
                    throw std::invalid_argument("Only bots can participate in Fast and TournamentFast modes!");
        
        if (game.decksCount < 1) throw std::invalid_argument("Minimal decks count is 1!");
        
        std::vector<PlayerCharacters> players;
        auto factory = Factory<Player, std::string, Player *(*)()>::getInstance();
        for (auto& pl : playerList)
        {
            PlayerCharacters newPlayer;
            if (pl.find("=") == std::string::npos) 
            {
                newPlayer.name = pl;
            }
            else 
            {
                newPlayer.name = pl.substr(pl.find('=') + 1);
                pl = pl.substr(0, pl.find('='));
            }

            if (factory->CheckObject(pl))
                newPlayer.player = std::shared_ptr<Player>(factory->CreateObject(pl));
            else
                newPlayer.player = std::shared_ptr<Player>(new Player());
            
            newPlayer.player->configFile = game.configFile;
            players.push_back(newPlayer);
        }

        for (size_t checkerA = 0; checkerA < players.size() - 1; ++checkerA)
        {
            for (size_t checkerB = checkerA + 1; checkerB < players.size(); ++checkerB)
            {
                if (players[checkerA].name == players[checkerB].name)
                {
                    std::cerr << "Nickname: " << players[checkerA].name << ", reapeats" << std::endl;
                    throw std::invalid_argument("You can only play with different nicknames");
                }
            }
        }

        game.start(players);
    }
}