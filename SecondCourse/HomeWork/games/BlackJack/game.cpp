#include "game.h"

#define CHANGEPLAYER( A ) ( ( A ) ^= 1 )

void PrintWinner(std::vector<Player*> vec)
{
    if (vec[0]->getScore() > 21 || vec[1]->getScore() == 21)
    {
        std::cout << vec[1]->name << " WIN!" << std::endl;
    }
    else if (vec[1]->getScore() > 21 || vec[0]->getScore() == 21)
    {
        std::cout << vec[0]->name << " WIN!" << std::endl;
    }
    else if (vec[0]->getScore() > vec[1]->getScore())
    {
        std::cout << vec[0]->name << " scored " << vec[0]->getScore() << std::endl;
        std::cout << vec[1]->name << " scored " << vec[1]->getScore() << std::endl;
        std::cout << vec[0]->name << " WIN!" << std::endl;
    }
    else if (vec[0]->getScore() < vec[1]->getScore())
    {
        std::cout << vec[1]->name << " scored " << vec[1]->getScore() << std::endl;
        std::cout << vec[0]->name << " scored " << vec[0]->getScore() << std::endl;
        std::cout << vec[1]->name << " WIN!" << std::endl;
    }
    else
    {
        std::cout << "Both players scored " << vec[0]->getScore() << std::endl;
        std::cout << "Tie!" << std::endl;
    }
}

void Game::start()
{
    // std::cout << "### BLACKJACK ###" << std::endl;
    // std::cout << "Control:\ng - get card\ns - stop get card\nq - quit" << std::endl;

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
    else if (rules.mode == TOURNAMENTFAST)
    {
        tournamentfastGame(players);
    }
    else
    {
        throw std::runtime_error("mode isn't available...");
    }
}

std::vector<Player*> Game::detailedGame(std::vector<Player*> players)
{
    Deck deck;
    deck.generateDeck();
    size_t inactivePlayersCount = 0;

    size_t ptr = 0;
    while (inactivePlayersCount != 2)
    {
        while (true) //successful command enter
        {
            std::string status = players[ptr]->makeAction();

            if (status == "g" || status == "get")
            {
                Card curCard = players[ptr]->getCard(deck);
                players[ptr]->goodScore();
                std::cout << players[ptr]->name << " took " << curCard.card << ". His score: " << players[ptr]->getScore() << std::endl;
                if (!players[ptr]->goodScore())
                {
                    std::cout << players[ptr]->name << " oops... enumeration!" << std::endl;
                    return players;
                }

                if (players[ptr]->getScore() == 21)
                {
                    std::cout << players[ptr]->name << " WOW! It's 21!" << std::endl; 
                    return players;
                }

                if (inactivePlayersCount == 0) CHANGEPLAYER(ptr);
            }
            else if (status == "s" || status == "stop") 
            {
                std::cout << players[ptr]->name << " stop getting card! His score: " << players[ptr]->getScore() << std::endl;
                inactivePlayersCount++;
                CHANGEPLAYER(ptr);
            }
            else if (status == "q" || status == "quit")
            {
                std::cout << players[ptr]->name << " left the game!" << std::endl;
                players[ptr]->reset();
                return players;
            }
            else continue;

            break;
        }
    }

    return players;
}

std::vector<Player*> Game::fastGame(std::vector<Player*> players)
{
    Deck deck;
    deck.generateDeck();

    size_t inactivePlayersCount = 0;

    size_t ptr = 0;
    while (inactivePlayersCount != 2)
    {
        std::string status = players[ptr]->makeAction();

        if (status == "g")
        {
            players[ptr]->getCard(deck);
            if (!players[ptr]->goodScore())
            {
                return players;
            }

            if (players[ptr]->getScore() == 21)
            {
                return players;
            }

            if (!inactivePlayersCount) CHANGEPLAYER(ptr);
        }
        else if (status == "s")
        {
            inactivePlayersCount++;
            CHANGEPLAYER(ptr);
        }
        else
        {
            throw std::logic_error("Bot is destroyed...");
        }
    }

    return players;
}

void UpgradeScore(std::vector<Player*> vec)
{
    if (vec[0]->getScore() > 21 || vec[1]->getScore() == 21) vec[1]->addTournamentScore(2);
    else if (vec[1]->getScore() > 21 || vec[0]->getScore() == 21) vec[0]->addTournamentScore(2);
    else if (vec[0]->getScore() > vec[1]->getScore()) vec[0]->addTournamentScore(2);
    else if (vec[0]->getScore() < vec[1]->getScore()) vec[1]->addTournamentScore(2);
    else { vec[0]->addTournamentScore(1); vec[1]->addTournamentScore(1); }

    vec[0]->reset();
    vec[1]->reset();
}

void Game::tournamentGame(std::vector<Player*> players)
{
    for (size_t playerA = 0; playerA < players.size() - 1; ++playerA)
    {
        for (size_t playerB = playerA + 1; playerB < players.size(); ++playerB)
        {
            std::cout << std::endl << players[playerA]->name << " VS " << players[playerB]->name << std::endl;
            std::vector<Player*> match = {players[playerA], players[playerB]};
            std::vector<Player*> res = detailedGame(match);
            PrintWinner(res);
            UpgradeScore(res);
        }
    }

    std::sort(players.begin(), players.end(), [](auto a, auto b){ return a->getTournamentScore() > b->getTournamentScore(); });
    std::cout << "\nRESULTS!" << std::endl;
    for (size_t i = 0; i < players.size(); ++i)
    {
        std::cout << players[i]->name << " scored " << players[i]->getTournamentScore() << std::endl;
    }
    for (size_t i = 0; i < players.size(); ++i)
    {
        if (players[i]->getTournamentScore() == players[0]->getTournamentScore())
        {
            std::cout << players[i]->name << " ";
        }
        else
        {
            std::cout << "WIN!" << std::endl;
            break;
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
            UpgradeScore(res);
        }
    }

    std::sort(players.begin(), players.end(), [](auto a, auto b){ return a->getTournamentScore() > b->getTournamentScore(); });
    for (size_t i = 0; i < players.size(); ++i)
    {
        if (players[i]->getTournamentScore() == players[0]->getTournamentScore())
        {
            std::cout << players[i]->name << " ";
        }
        else
        {
            std::cout << "WIN!" << std::endl;
            break;
        }
    }
}