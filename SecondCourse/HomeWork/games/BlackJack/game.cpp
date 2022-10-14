#include "game.h"

void PrintWinner(std::vector<Player> vec)
{
    if (vec[0].GetScore() > 21 || vec[1].GetScore() == 21)
    {
        std::cout << vec[1].name << " WIN!" << std::endl;
    }
    else if (vec[1].GetScore() > 21 || vec[0].GetScore() == 21)
    {
        std::cout << vec[0].name << " WIN!" << std::endl;
    }
    else if (vec[0].GetScore() > vec[1].GetScore())
    {
        std::cout << vec[0].name << " scored " << vec[0].GetScore() << std::endl;
        std::cout << vec[1].name << " scored " << vec[1].GetScore() << std::endl;
        std::cout << vec[0].name << " WIN!" << std::endl;
    }
    else if (vec[0].GetScore() < vec[1].GetScore())
    {
        std::cout << vec[1].name << " scored " << vec[1].GetScore() << std::endl;
        std::cout << vec[0].name << " scored " << vec[0].GetScore() << std::endl;
        std::cout << vec[1].name << " WIN!" << std::endl;
    }
    else
    {
        std::cout << "Both players scored " << vec[0].GetScore() << std::endl;
        std::cout << "Tie!" << std::endl;
    }
}

void Game::start()
{
    std::vector<Player> players;
    for (auto player : rules.players)
    {
        if (player.front() == '-') players.push_back(Bot(player));
        else players.push_back(Player(player));
    }

    if (rules.mode == DETAILED) 
    {
        std::vector<Player> result = detailedGame(players);
        PrintWinner(result);
    }
    else if (rules.mode == FAST) 
    {
        std::vector<Player> result = fastGame(players);
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

std::vector<Player> Game::detailedGame(std::vector<Player> players)
{
    std::cout << "### BLACKJACK ###" << std::endl;
    std::cout << "Control:\ng - get card\ns - stop get card\nq - quite" << std::endl;

    Deck deck;
    deck.GenerateDeck();

    std::vector<Player> inactivePlayers;

    while(players.size() != 0)
    {
        size_t ptr = 0;
        while (ptr < players.size())
        {
            while (true) //successful command enter
            {
                std::string status = players[ptr].makeAction();

                if (status == "g" || status == "get")
                {
                    Card curCard = players[ptr].GetCard(deck);
                    std::cout << players[ptr].name << " took " << curCard.card << ". His score: " << players[ptr].GetScore() << std::endl;
                    if (!players[ptr].GoodScore())
                    {
                        inactivePlayers = std::move(players); // players.size == 0?
                    }
                    else ptr++;

                    if (players[ptr].GetScore() == 21)
                    {
                        inactivePlayers = std::move(players); //up
                    }
                }
                else if (status == "s" || status == "stop") 
                {
                    inactivePlayers.push_back(players[ptr]);
                    players.erase(players.begin() + ptr);
                }
                else if (status == "q" || status == "quit")
                {
                    return;
                }
                else continue;

                break;
            }
        }
    }

    return inactivePlayers;
}

std::vector<Player> Game::fastGame(std::vector<Player> players)
{
    Deck deck;
    deck.GenerateDeck();

    std::vector<Player> inactivePlayers;

    while(players.size() != 0)
    {
        size_t ptr = 0;
        while (ptr < players.size())
        {
            std::string status = players[ptr].makeAction();

            if (status == "g")
            {
                if (!players[ptr].GoodScore())
                {
                    inactivePlayers = std::move(players); // players.size == 0?
                }
                else ptr++;

                if (players[ptr].GetScore() == 21)
                {
                    inactivePlayers = std::move(players); //up
                }
            }
            else if (status == "s") 
            {
                inactivePlayers.push_back(players[ptr]);
                players.erase(players.begin() + ptr);
            }
            else
            {
                throw std::logic_error("Bot is destroyed...");
            }
        }
    }

    return inactivePlayers;
}

void Game::tournamentGame(std::vector<Player> players)
{
    for (size_t playerA = 0; playerA < players.size() - 1; ++playerA)
    {
        for (size_t playerB = playerA + 1; playerB < players.size(); ++playerB)
        {
            std::vector<Player> match = {players[playerA], players[playerB]};
            std::vector<Player> res = detailedGame(match);
        }
    }
}

void Game::tournamentfastGame(std::vector<Player> players)
{
    for (size_t playerA = 0; playerA < players.size() - 1; ++playerA)
    {
        for (size_t playerB = playerA + 1; playerB < players.size(); ++playerB)
        {
            std::vector<Player> match = {players[playerA], players[playerB]};
            std::vector<Player> res = fastGame(match);
        }
    }
}