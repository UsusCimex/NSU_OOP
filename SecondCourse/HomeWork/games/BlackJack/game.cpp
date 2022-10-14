#include "game.h"

void PrintWinner(std::vector<Player*> vec)
{
    if (vec[0]->GetScore() > 21 || vec[1]->GetScore() == 21)
    {
        std::cout << vec[1]->name << " WIN!" << std::endl;
    }
    else if (vec[1]->GetScore() > 21 || vec[0]->GetScore() == 21)
    {
        std::cout << vec[0]->name << " WIN!" << std::endl;
    }
    else if (vec[0]->GetScore() > vec[1]->GetScore())
    {
        std::cout << vec[0]->name << " scored " << vec[0]->GetScore() << std::endl;
        std::cout << vec[1]->name << " scored " << vec[1]->GetScore() << std::endl;
        std::cout << vec[0]->name << " WIN!" << std::endl;
    }
    else if (vec[0]->GetScore() < vec[1]->GetScore())
    {
        std::cout << vec[1]->name << " scored " << vec[1]->GetScore() << std::endl;
        std::cout << vec[0]->name << " scored " << vec[0]->GetScore() << std::endl;
        std::cout << vec[1]->name << " WIN!" << std::endl;
    }
    else
    {
        std::cout << "Both players scored " << vec[0]->GetScore() << std::endl;
        std::cout << "Tie!" << std::endl;
    }
}

void Game::start()
{
    std::vector<Player*> players;
    for (auto player : rules.players)
    {
        if (player.front() == '-') players.push_back(new Bot(player));
        else players.push_back(new Player(player));
    }

    if (rules.mode == DETAILED) 
    {
        std::vector<Player*> result = detailedGame(players);
        PrintWinner(result);
    }
    else if (rules.mode == FAST) 
    {
        std::vector<Player*> result = fastGame(players);
        PrintWinner(result);
    }
    else if (rules.mode == TOURNAMENT) 
    {
        tournamentGame(players);
    }
    else 
    {
        tournamentfastGame(players);
    }
}

std::vector<Player*> Game::detailedGame(std::vector<Player*> players)
{
    std::cout << "### BLACKJACK ###" << std::endl;
    std::cout << "Control:\ng - get card\ns - stop get card\nq - quite" << std::endl;

    Deck deck;
    deck.GenerateDeck();
    size_t readyPlayers = 0;

    while(readyPlayers != 2)
    {
        size_t ptr = 0;
        while (ptr < players.size())
        {
            while (true) //successful command enter
            {
                std::string status = players[ptr]->makeAction();

                if (status == "g" || status == "get")
                {
                    Card curCard = players[ptr]->GetCard(deck);
                    players[ptr]->GoodScore();
                    std::cout << players[ptr]->name << " took " << curCard.card << ". His score: " << players[ptr]->GetScore() << std::endl;
                    if (!players[ptr]->GoodScore())
                    {
                        std::cout << players[ptr]->name << " oops... enumeration!" << std::endl;
                        return players;
                    }

                    if (players[ptr]->GetScore() == 21)
                    {
                        std::cout << players[ptr]->name << " WOW! It's 21!" << std::endl; 
                        return players;
                    }

                    if (!readyPlayers) ptr ^= 1;
                }
                else if (status == "s" || status == "stop") 
                {
                    std::cout << players[ptr]->name << " stop getting card! His score: " << players[ptr]->GetScore() << std::endl;
                    readyPlayers++;
                    ptr ^= 1;
                }
                else if (status == "q" || status == "quit")
                {
                    try
                    {                       
                        throw std::runtime_error("Bye! Bye!");
                    }
                    catch(const std::exception& e)
                    {
                        std::cerr << e.what() << '\n';
                    }
                }
                else continue;

                break;
            }
        }
    }

    return players;
}

std::vector<Player*> Game::fastGame(std::vector<Player*> players)
{
    Deck deck;
    deck.GenerateDeck();

    size_t readyPlayers = 0;

    while(readyPlayers != 2)
    {
        size_t ptr = 0;
        while (ptr < players.size())
        {
            std::string status = players[ptr]->makeAction();

            if (status == "g")
            {
                players[ptr]->GetCard(deck);
                if (!players[ptr]->GoodScore())
                {
                    return players;
                }

                if (players[ptr]->GetScore() == 21)
                {
                    return players;
                }

                if (!readyPlayers) ptr ^= 1;
            }
            else if (status == "s")
            {
                readyPlayers++;
                ptr ^= 1;
            }
            else
            {
                throw std::logic_error("Bot is destroyed...");
            }
        }
    }

    return players;
}

void Game::tournamentGame(std::vector<Player*> players)
{
    for (size_t playerA = 0; playerA < players.size() - 1; ++playerA)
    {
        for (size_t playerB = playerA + 1; playerB < players.size(); ++playerB)
        {
            std::vector<Player*> match = {players[playerA], players[playerB]};
            std::vector<Player*> res = detailedGame(match);
            players[playerA]->reset();
            players[playerB]->reset();
        }
    }
}

void Game::tournamentfastGame(std::vector<Player*> players)
{
    for (size_t playerA = 0; playerA < players.size() - 1; ++playerA)
    {
        for (size_t playerB = playerA + 1; playerB < players.size(); ++playerB)
        {
            std::vector<Player*> match = {players[playerA], players[playerB]};
            std::vector<Player*> res = fastGame(match);
            players[playerA]->reset();
            players[playerB]->reset();
        }
    }
}