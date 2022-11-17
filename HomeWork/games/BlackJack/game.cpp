#include "game.h"

//Selects the winner from two players, by points
void PrintMatchWinner(std::vector<Player> & vec)
{
    unsigned alignment = std::max(vec[0].getName().length(), vec[1].getName().length());
    std::string printStr;
    printStr = vec[0].getName() + " scored " + "\033[32m" + std::to_string(vec[0].getScore()) + "\033[0m";
    std::cout << "/" << std::setfill('=') << std::setw(alignment / 2) << "=" << std::setw(12) << "\033[1m\033[35mMATCH RESULT\033[0m" << std::setfill('=') << std::setw(alignment / 2 + 1) << "=\\" << std::setfill(' ') << std::endl;
    std::cout << std::setw(printStr.length() + 9 + (alignment + 14 - printStr.length() - 9) / 2) << printStr << std::endl;
    printStr = vec[1].getName() + " scored " + "\033[32m" + std::to_string(vec[1].getScore()) + "\033[0m";
    std::cout << std::setw(printStr.length() + 9 + (alignment + 14 - printStr.length() - 9) / 2) << printStr << std::endl;
    
    if ((vec[0].getScore() > 21 && vec[1].getScore() <= 21) || (vec[0].getScore() <= 21 && vec[1].getScore() <= 21 && vec[1].getScore() > vec[0].getScore()) || (vec[0].getScore() > 21 && vec[1].getScore() > 21 && vec[1].getScore() < vec[0].getScore()))
    {
        std::cout << std::setw(vec[1].getName().length() + (alignment + 14 - vec[1].getName().length() - 4) / 2) << vec[1].getName() << " \033[34mWIN!\033[0m" << std::endl;
    }
    else if ((vec[0].getScore() <= 21 && vec[1].getScore() > 21) || (vec[0].getScore() <= 21 && vec[1].getScore() <= 21 && vec[1].getScore() < vec[0].getScore()) || (vec[0].getScore() > 21 && vec[1].getScore() > 21 && vec[1].getScore() > vec[0].getScore()))
    {
        std::cout << std::setw(vec[0].getName().length() + (alignment + 14 - vec[0].getName().length() - 4) / 2) << vec[0].getName() << " \033[34mWIN!\033[0m" << std::endl;
    }
    else
    {
        std::cout << std::setw(4 + 13 + (alignment + 14 - 13) / 2) << "\033[36mTie!\033[0m" << std::endl;
    }

    std::cout << "\\" << std::setfill('=') << std::setw(alignment + 13 + alignment % 2) << "=/" << std::setfill(' ') << std::endl;
}

void PrintTournamentWinner(std::vector<Player> & vec)
{
    std::cout << std::endl;

    std::sort(vec.begin(), vec.end(), [](auto a, auto b){ return a.getTournamentScore() > b.getTournamentScore(); });
    size_t countWinner = 0;
    unsigned maxLength = 0;
    for (size_t i = 0; i < vec.size(); ++i)
    {
        if (vec[i].getTournamentScore() == vec[0].getTournamentScore())
        {
            countWinner++;
        }
        if (vec[i].getName().length() > maxLength) maxLength = vec[i].getName().length();
    }
    maxLength += 11;
    unsigned alignmentWinner = 0;
    for (size_t i = 0; i < countWinner; ++i) alignmentWinner += vec[i].getName().length();
    alignmentWinner += countWinner + 4;
    unsigned alignment = std::max(maxLength, alignmentWinner);

    std::cout << "\033[1m" << "/" << std::setfill('=') << std::setw(alignment / 2) << "=" << std::setw(11) << "\033[35m!!RESULTS!!\033[0m" << std::setfill('=') << std::setw(alignment / 2 + 1) << "\\" << std::setfill(' ') << std::endl;

    for (size_t i = 0; i < vec.size(); ++i)
    {
        std::cout << "\033[1m" << "\033[1m" << std::setw(vec[i].getName().length() + (alignment + 13 - vec[i].getName().length() - 9) / 2) << vec[i].getName() << " scored " << "\033[32m" << std::to_string(vec[i].getTournamentScore()) << "\033[0m" << std::endl;
    }
    std::cout << std::setw(vec[0].getName().length() + (alignment + 13 - alignmentWinner) / 2);
    for (size_t i = 0; i < countWinner; ++i) std::cout << vec[i].getName() << " ";
    std::cout << "\033[1m" << "\033[34mWIN!\033[0m" << std::endl;

    std::cout << "\033[1m" << "\\" << std::setfill('=') << std::setw(alignment + 12 + 1 - alignment % 2) << "/" << std::setfill(' ') << std::endl;
}

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

void PrintInitialDistr(std::vector<Player> & players)
{
    for (auto &pl : players)
    {
        if (pl.getScore() > 21) pl.checkScore();
        std::cout << pl.getName() << " has " << pl.getScore() << " score, his openned card: " << pl.getHand().back().card << "(" << pl.getHand().back().power << ")" << std::endl;
    }
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
    // std::cerr << decksCount << std::endl;
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