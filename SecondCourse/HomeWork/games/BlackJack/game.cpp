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
        tournamentGame();
    }
    else if (rules.mode == TOURNAMENTFAST)
    {
        tournamentfastGame();
    }
    else
    {
        throw std::runtime_error("mode isn't available...");
    }
}

//If one of players get 21, return 1
bool InitialDistribution(std::vector<Player*> players, Deck & deck, bool printScore)
{
    for (size_t i = 0; i < 2; ++i) //defaul 2 cards in start game
    {
        for (auto pl : players)
        {
            pl->getCard(deck);
        }
    }

    if (printScore)
    {
        for (auto pl : players)
        {
            pl->checkScore();
            std::cout << pl->name << " has " << pl->getScore() << " score, his openned card: " << pl->seeCard().card << "(" << pl->seeCard().power << ")" << std::endl;
        }
    }

    for (auto pl : players)
    {
        if (pl->getScore() == 21) return 1;
    }
    return 0;
}

std::vector<Player*> Game::detailedGame(std::vector<Player*> players)
{
    Deck deck(rules.decksCount);
    deck.generateDeck();
    if (InitialDistribution(players, deck, true)) return players;
    size_t inactivePlayersCount = 0;

    size_t curPlayer = 0;
    while (inactivePlayersCount != 2)
    {
        while (true) //successful command enter
        {
            std::string status = players[curPlayer]->makeAction(players[!curPlayer]);

            if (status == "g" || status == "get")
            {
                Card curCard = players[curPlayer]->getCard(deck);
                players[curPlayer]->checkScore();
                std::cout << players[curPlayer]->name << " took " << curCard.card << "(" << curCard.power << ")" << ". His score: " << players[curPlayer]->getScore() << std::endl;
                if (!players[curPlayer]->checkScore())
                {
                    std::cout << players[curPlayer]->name << " oops... enumeration!" << std::endl;
                    return players;
                }

                if (players[curPlayer]->getScore() == 21)
                {
                    std::cout << players[curPlayer]->name << " WOW! It's 21!" << std::endl;
                    return players;
                }

                if (inactivePlayersCount == 0) CHANGEPLAYER(curPlayer);
            }
            else if (status == "s" || status == "stop") 
            {
                std::cout << players[curPlayer]->name << " stop getting card! His score: " << players[curPlayer]->getScore() << std::endl;
                inactivePlayersCount++;
                CHANGEPLAYER(curPlayer);
            }
            else if (status == "q" || status == "quit")
            {
                std::cout << players[curPlayer]->name << " left the game!" << std::endl;
                players[curPlayer]->reset();
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
    Deck deck(rules.decksCount);
    deck.generateDeck();
    // std::cerr << rules.decksCount << std::endl;
    if (InitialDistribution(players, deck, false)) return players;

    size_t inactivePlayersCount = 0;

    size_t curPlayer = 0;
    while (inactivePlayersCount != 2)
    {
        std::string status = players[curPlayer]->makeAction(players[!curPlayer]);

        if (status == "g")
        {
            players[curPlayer]->getCard(deck);
            if (!players[curPlayer]->checkScore())
            {
                return players;
            }

            if (players[curPlayer]->getScore() == 21)
            {
                return players;
            }

            if (!inactivePlayersCount) CHANGEPLAYER(curPlayer);
        }
        else if (status == "s")
        {
            inactivePlayersCount++;
            CHANGEPLAYER(curPlayer);
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

void Game::tournamentGame()
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
            break;
        }
    }
    std::cout << "WIN!" << std::endl;
}

void Game::tournamentfastGame()
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
            break;
        }
    }
    std::cout << "WIN!" << std::endl;
}