#include "game.h"

//Selects the winner from two players, by points
void PrintMatchWinner(std::vector<PlayerCharacters> & vec)
{
    unsigned alignment = std::max(vec[0].name.length(), vec[1].name.length());
    std::string printStr;
    printStr = vec[0].name + " scored " + "\033[32m" + std::to_string(vec[0].score) + "\033[0m";
    std::cout << "/" << std::setfill('=') << std::setw(alignment / 2) << "=" << std::setw(12) << "\033[1m\033[35mMATCH RESULT\033[0m" << std::setfill('=') << std::setw(alignment / 2 + 1) << "=\\" << std::setfill(' ') << std::endl;
    std::cout << std::setw(printStr.length() + 9 + (alignment + 14 - printStr.length() - 9) / 2) << printStr << std::endl;
    printStr = vec[1].name + " scored " + "\033[32m" + std::to_string(vec[1].score) + "\033[0m";
    std::cout << std::setw(printStr.length() + 9 + (alignment + 14 - printStr.length() - 9) / 2) << printStr << std::endl;
    
    if ((vec[0].score > 21 && vec[1].score <= 21) || (vec[0].score <= 21 && vec[1].score <= 21 && vec[1].score > vec[0].score) || (vec[0].score > 21 && vec[1].score > 21 && vec[1].score < vec[0].score))
    {
        std::cout << std::setw(vec[1].name.length() + (alignment + 14 - vec[1].name.length() - 4) / 2) << vec[1].name << " \033[34mWIN!\033[0m" << std::endl;
    }
    else if ((vec[0].score <= 21 && vec[1].score > 21) || (vec[0].score <= 21 && vec[1].score <= 21 && vec[1].score < vec[0].score) || (vec[0].score > 21 && vec[1].score > 21 && vec[1].score > vec[0].score))
    {
        std::cout << std::setw(vec[0].name.length() + (alignment + 14 - vec[0].name.length() - 4) / 2) << vec[0].name << " \033[34mWIN!\033[0m" << std::endl;
    }
    else
    {
        std::cout << std::setw(4 + 13 + (alignment + 14 - 13) / 2) << "\033[36mTie!\033[0m" << std::endl;
    }

    std::cout << "\\" << std::setfill('=') << std::setw(alignment + 13 + alignment % 2) << "=/" << std::setfill(' ') << std::endl;
}

void PrintTournamentWinner(std::vector<PlayerCharacters> & vec)
{
    std::cout << std::endl;

    std::sort(vec.begin(), vec.end(), [](auto a, auto b){ return a.tournamentScore > b.tournamentScore; });
    size_t countWinner = 0;
    unsigned maxLength = 0;
    for (size_t i = 0; i < vec.size(); ++i)
    {
        if (vec[i].tournamentScore == vec[0].tournamentScore)
        {
            countWinner++;
        }
        if (vec[i].name.length() > maxLength) maxLength = vec[i].name.length();
    }
    maxLength += 11;
    unsigned alignmentWinner = 0;
    for (size_t i = 0; i < countWinner; ++i) alignmentWinner += vec[i].name.length();
    alignmentWinner += countWinner + 4;
    unsigned alignment = std::max(maxLength, alignmentWinner);

    std::cout << "\033[1m" << "/" << std::setfill('=') << std::setw(alignment / 2) << "=" << std::setw(11) << "\033[35m!!RESULTS!!\033[0m" << std::setfill('=') << std::setw(alignment / 2 + 1) << "\\" << std::setfill(' ') << std::endl;

    for (size_t i = 0; i < vec.size(); ++i)
    {
        std::cout << "\033[1m" << "\033[1m" << std::setw(vec[i].name.length() + (alignment + 13 - vec[i].name.length() - 9) / 2) << vec[i].name << " scored " << "\033[32m" << std::to_string(vec[i].tournamentScore) << "\033[0m" << std::endl;
    }
    std::cout << std::setw(vec[0].name.length() + (alignment + 13 - alignmentWinner) / 2);
    for (size_t i = 0; i < countWinner; ++i) std::cout << vec[i].name << " ";
    std::cout << "\033[1m" << "\033[34mWIN!\033[0m" << std::endl;

    std::cout << "\033[1m" << "\\" << std::setfill('=') << std::setw(alignment + 12 + 1 - alignment % 2) << "/" << std::setfill(' ') << std::endl;
}

void Game::start(std::vector<PlayerCharacters> & players)
{
    // std::cout << "### BLACKJACK ###" << std::endl;
    // std::cout << "Control:\ng - get card\ns - stop get card\nq - quit" << std::endl;
    if (gameMode == Mode::DETAILED)
    {
        std::vector<PlayerCharacters> result = detailedGame(players);
        PrintMatchWinner(result);
    }
    else if (gameMode == Mode::FAST)
    {
        std::vector<PlayerCharacters> result = fastGame(players);
        PrintMatchWinner(result);
    }
    else if (gameMode == Mode::TOURNAMENT) 
    {
        std::vector<PlayerCharacters> result = tournamentGame(players);
        PrintTournamentWinner(result);
    }
    else if (gameMode == Mode::TOURNAMENTFAST)
    {
        std::vector<PlayerCharacters> result = tournamentfastGame(players);
        PrintTournamentWinner(result);
    }
    else
    {
        throw std::runtime_error("gameMode isn't available...");
    }
}

//Initial deal of cards
void InitialDistribution(std::vector<PlayerCharacters> & players, Deck & deck)
{
    for (size_t i = 0; i < 2; ++i) //defaul 2 cards in start game
    {
        for (auto &pl : players)
        {
            pl.getCard(deck);
        }
    }

    players[0].player->enemyCard = players[1].hand.front();
    players[1].player->enemyCard = players[0].hand.front();
}

void PrintInitialDistr(std::vector<PlayerCharacters> & players)
{
    for (auto &pl : players)
    {
        if (pl.score > 21) pl.checkScore();
        std::cout << pl.name << " has " << pl.score << " score, his openned card: " << pl.hand.back().card << "(" << pl.hand.back().power << ")" << std::endl;
    }
}

std::vector<PlayerCharacters> Game::detailedGame(std::vector<PlayerCharacters> & players)
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
            std::cout << players[playerPtr].name << " took " << curCard.card << "(" << curCard.power << ")" << ". His score: " << players[playerPtr].score << std::endl;
            if (!playerStatus)
            {
                std::cout << "\033[31m" << players[playerPtr].name << " oops... enumeration!\033[0m" << std::endl;
                playerPtr++;
            }
        }
        else if (action == Action::STAND) 
        {
            std::cout << players[playerPtr].name << " stop getting card! His score: " << players[playerPtr].score << std::endl;
            playerPtr++;
        }
    }

    return players;
}

std::vector<PlayerCharacters> Game::fastGame(std::vector<PlayerCharacters> & players)
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
void Game::UpgradeTournamentScore(std::vector<PlayerCharacters> & vec, std::vector<PlayerCharacters> & players,size_t & a, size_t & b)
{

    if ((vec[0].score > 21 && vec[1].score <= 21) || (vec[0].score <= 21 && vec[1].score <= 21 && vec[1].score > vec[0].score) || (vec[0].score > 21 && vec[1].score > 21 && vec[1].score < vec[0].score))
    {
        players[b].tournamentScore += 2;
    }
    else if ((vec[0].score <= 21 && vec[1].score > 21) || (vec[0].score <= 21 && vec[1].score <= 21 && vec[1].score < vec[0].score) || (vec[0].score > 21 && vec[1].score > 21 && vec[1].score > vec[0].score))
    {
        players[a].tournamentScore += 2;
    }
    else
    {
        players[a].tournamentScore += 1;
        players[b].tournamentScore += 1;
    }
}

std::vector<PlayerCharacters> Game::tournamentGame(std::vector<PlayerCharacters> & players)
{
    for (size_t playerA = 0; playerA < players.size() - 1; ++playerA)
    {
        for (size_t playerB = playerA + 1; playerB < players.size(); ++playerB)
        {
            std::cout << std::endl << "   \033[1m" << players[playerA].name << " \033[31mVS\033[0m \033[1m" << players[playerB].name << "\033[0m" << std::endl;
            std::vector<PlayerCharacters> match = {players[playerA], players[playerB]};
            std::vector<PlayerCharacters> res = detailedGame(match);
            PrintMatchWinner(res);
            UpgradeTournamentScore(res, players, playerA, playerB);
        }
    }

    return players;
}

std::vector<PlayerCharacters> Game::tournamentfastGame(std::vector<PlayerCharacters> & players)
{
    for (size_t playerA = 0; playerA < players.size() - 1; ++playerA)
    {
        for (size_t playerB = playerA + 1; playerB < players.size(); ++playerB)
        {
            std::vector<PlayerCharacters> match = {players[playerA], players[playerB]};
            std::vector<PlayerCharacters> res = fastGame(match);
            UpgradeTournamentScore(res, players, playerA, playerB);
        }
    }

    return players;
}