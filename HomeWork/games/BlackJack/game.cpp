#include "game.h"

void Game::start(std::vector<Player> & players)
{
    // std::cout << "### BLACKJACK ###" << std::endl;
    // std::cout << "Control:\ng - get card\ns - stop get card\nq - quit" << std::endl;
    if (gameMode == Mode::DETAILED)
    {
        std::vector<Player> result = detailedGame(players);
        PrintMatchWinner(result);
    }
    else if (gameMode == Mode::FAST)
    {
        std::vector<Player> result = fastGame(players);
        PrintMatchWinner(result);
    }
    else if (gameMode == Mode::TOURNAMENT) 
    {
        std::vector<Player> result = tournamentGame(players);
        PrintTournamentWinner(result);
    }
    else if (gameMode == Mode::TOURNAMENTFAST)
    {
        std::vector<Player> result = tournamentfastGame(players);
        PrintTournamentWinner(result);
    }
    else
    {
        throw std::runtime_error("gameMode isn't available...");
    }
}

//Initial deal of cards
void InitialDistribution(std::vector<Player> & players, Deck & deck)
{
    for (size_t i = 0; i < 2; ++i) //defaul 2 cards in start game
    {
        for (auto &pl : players)
        {
            pl.getCard(deck);
        }
    }

    players[0].player->getEnemyCard() = players[1].getHand().front();
    players[1].player->getEnemyCard() = players[0].getHand().front();
}

std::vector<Player> Game::detailedGame(std::vector<Player> & players)
{
    if (players.size() != 2) throw std::runtime_error("In detailed game play only 2 players");
    Deck deck(decksCount);
    deck.generateDeck();
    InitialDistribution(players, deck);
    PrintInitialDistr(players);

    size_t playerPtr = 0;
    while (playerPtr != players.size())
    {
        Action action = players[playerPtr].player->makeAction();

        if (action == Action::HIT)
        {
            Card curCard = players[playerPtr].getCard(deck);
            bool playerStatus = players[playerPtr].checkScore();
            std::cout << players[playerPtr].getName() << " took " << curCard.card << "(" << curCard.power << ")" << ". His score: " << players[playerPtr].getScore() << std::endl;
            if (!playerStatus)
            {
                std::cout << "\033[31m" << players[playerPtr].getName() << " oops... enumeration!\033[0m" << std::endl;
                playerPtr++;
            }
        }
        else if (action == Action::STAND) 
        {
            std::cout << players[playerPtr].getName() << " stop getting card! His score: " << players[playerPtr].getScore() << std::endl;
            playerPtr++;
        }
    }

    return players;
}

std::vector<Player> Game::fastGame(std::vector<Player> & players)
{
    if (players.size() != 2) throw std::runtime_error("In fast game play only 2 players");
    Deck deck(decksCount);
    deck.generateDeck();
    InitialDistribution(players, deck);

    size_t playerPtr = 0;
    while (playerPtr != players.size())
    {
        Action action = players[playerPtr].player->makeAction();

        if (action == HIT)
        {
            players[playerPtr].getCard(deck);
            if (!players[playerPtr].checkScore())
            {
                playerPtr++;
            }
        }
        else if (action == STAND)
        {
            playerPtr++;
        }
        else
        {
            throw std::logic_error("Bot is destroyed...");
        }
    }

    return players;
}

//Selects the winner from two players, by points
void Game::UpgradeTournamentScore(std::vector<Player> & vec, std::vector<Player> & players,size_t & a, size_t & b)
{

    if ((vec[0].getScore() > 21 && vec[1].getScore() <= 21) || (vec[0].getScore() <= 21 && vec[1].getScore() <= 21 && vec[1].getScore() > vec[0].getScore()) || (vec[0].getScore() > 21 && vec[1].getScore() > 21 && vec[1].getScore() < vec[0].getScore()))
    {
        players[b].changeTournamentScore(2);
    }
    else if ((vec[0].getScore() <= 21 && vec[1].getScore() > 21) || (vec[0].getScore() <= 21 && vec[1].getScore() <= 21 && vec[1].getScore() < vec[0].getScore()) || (vec[0].getScore() > 21 && vec[1].getScore() > 21 && vec[1].getScore() > vec[0].getScore()))
    {
        players[a].changeTournamentScore(2);
    }
    else
    {
        players[a].changeTournamentScore(1);
        players[b].changeTournamentScore(1);
    }
}

std::vector<Player> Game::tournamentGame(std::vector<Player> & players)
{
    for (size_t playerA = 0; playerA < players.size() - 1; ++playerA)
    {
        for (size_t playerB = playerA + 1; playerB < players.size(); ++playerB)
        {
            std::cout << std::endl << "   \033[1m" << players[playerA].getName() << " \033[31mVS\033[0m \033[1m" << players[playerB].getName() << "\033[0m" << std::endl;
            std::vector<Player> match = {players[playerA], players[playerB]};
            std::vector<Player> res = detailedGame(match);
            PrintMatchWinner(res);
            UpgradeTournamentScore(res, players, playerA, playerB);
        }
    }

    return players;
}

std::vector<Player> Game::tournamentfastGame(std::vector<Player> & players)
{
    for (size_t playerA = 0; playerA < players.size() - 1; ++playerA)
    {
        for (size_t playerB = playerA + 1; playerB < players.size(); ++playerB)
        {
            std::vector<Player> match = {players[playerA], players[playerB]};
            std::vector<Player> res = fastGame(match);
            UpgradeTournamentScore(res, players, playerA, playerB);
        }
    }

    return players;
}