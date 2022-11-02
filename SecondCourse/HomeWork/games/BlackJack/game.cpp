#include "game.h"

#define CHANGEPLAYER( A ) ( ( A ) ^= 1 )

Game::Game(std::vector<Player*> & playerList)
{
    players.resize(playerList.size());
    for (int i = 0; i < playerList.size(); ++i) players[i].player = playerList[i];
}

//Selects the winner from two players, by points
void PrintWinner(std::vector<playerCharacters> & vec)
{
    if (vec[0].score > 21 || vec[1].score == 21)
    {
        std::cout << vec[1].player->name << " WIN!" << std::endl;
    }
    else if (vec[1].score > 21 || vec[0].score == 21)
    {
        std::cout << vec[0].player->name << " WIN!" << std::endl;
    }
    else if (vec[0].score > vec[1].score)
    {
        std::cout << vec[0].player->name << " scored " << vec[0].score << std::endl;
        std::cout << vec[1].player->name << " scored " << vec[1].score << std::endl;
        std::cout << vec[0].player->name << " WIN!" << std::endl;
    }
    else if (vec[0].player->score < vec[1].player->score)
    {
        std::cout << vec[1].player->name << " scored " << vec[1].score << std::endl;
        std::cout << vec[0].player->name << " scored " << vec[0].score << std::endl;
        std::cout << vec[1].player->name << " WIN!" << std::endl;
    }
    else
    {
        std::cout << "Both players scored " << vec[0].score << std::endl;
        std::cout << "Tie!" << std::endl;
    }
}

void Game::start()
{
    // std::cout << "### BLACKJACK ###" << std::endl;
    // std::cout << "Control:\ng - get card\ns - stop get card\nq - quit" << std::endl;
    if (rules.mode == DETAILED)
    {
        std::vector<playerCharacters> result = detailedGame(players);
        PrintWinner(result);
    }
    else if (rules.mode == FAST) 
    {
        std::vector<playerCharacters> result = fastGame(players);
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

//Initial deal of cards
//If one of players get 21, return 1
bool InitialDistribution(std::vector<playerCharacters> & players, Deck & deck, bool printScore)
{
    for (size_t i = 0; i < 2; ++i) //defaul 2 cards in start game
    {
        for (auto &pl : players)
        {
            pl.getCard(deck);
        }
    }

    if (printScore)
    {
        for (auto &pl : players)
        {
            pl.checkScore();
            std::cout << pl.player->name << " has " << pl.score << " score, his openned card: " << pl.hand.back().card << "(" << pl.hand.back().power << ")" << std::endl;
        }
    }

    for (auto &pl : players)
    {
        if (pl.score == 21) return 1;
    }

    players[0].player->enemyCard = players[1].hand.front();
    players[1].player->enemyCard = players[0].hand.front();

    return 0;
}

std::vector<playerCharacters> Game::detailedGame(std::vector<playerCharacters> & players)
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
            std::string status = players[curPlayer].player->makeAction();

            if (status == "g" || status == "get")
            {
                Card curCard = players[curPlayer].getCard(deck);
                players[curPlayer].checkScore();
                std::cout << players[curPlayer].player->name << " took " << curCard.card << "(" << curCard.power << ")" << ". His score: " << players[curPlayer].score << std::endl;
                if (!players[curPlayer].checkScore())
                {
                    std::cout << players[curPlayer].player->name << " oops... enumeration!" << std::endl;
                    return players;
                }

                if (players[curPlayer].score == 21)
                {
                    std::cout << players[curPlayer].player->name << " WOW! It's 21!" << std::endl;
                    return players;
                }

                if (inactivePlayersCount == 0) CHANGEPLAYER(curPlayer);
            }
            else if (status == "s" || status == "stop") 
            {
                std::cout << players[curPlayer].player->name << " stop getting card! His score: " << players[curPlayer].score << std::endl;
                inactivePlayersCount++;
                CHANGEPLAYER(curPlayer);
            }
            else if (status == "q" || status == "quit")
            {
                std::cout << players[curPlayer].player->name << " left the game!" << std::endl;
                players[curPlayer].resetHand();
                return players;
            }
            else continue;

            break;
        }
    }

    return players;
}

std::vector<playerCharacters> Game::fastGame(std::vector<playerCharacters> & players)
{
    Deck deck(rules.decksCount);
    deck.generateDeck();
    // std::cerr << rules.decksCount << std::endl;
    if (InitialDistribution(players, deck, false)) return players;

    size_t inactivePlayersCount = 0;

    size_t curPlayer = 0;
    while (inactivePlayersCount != 2)
    {
        std::string status = players[curPlayer].player->makeAction();

        if (status == "g")
        {
            players[curPlayer].getCard(deck);
            if (!players[curPlayer].checkScore())
            {
                return players;
            }

            if (players[curPlayer].score == 21)
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

//Selects the winner from two players, by points
void Game::UpgradeScore(std::vector<playerCharacters> & vec)
{
    if (vec[0].score > 21 || vec[1].score == 21) vec[1].tournamentScore += 2;
    else if (vec[1].score > 21 || vec[0].score == 21) vec[0].tournamentScore += 2;
    else if (vec[0].score > vec[1].score) vec[0].tournamentScore += 2;
    else if (vec[0].score < vec[1].score) vec[1].tournamentScore += 2;
    else { vec[0].tournamentScore += 1; vec[1].tournamentScore += 1; }

    vec[0].resetHand();
    vec[1].resetHand();
}

void Game::tournamentGame()
{
    for (size_t playerA = 0; playerA < players.size() - 1; ++playerA)
    {
        for (size_t playerB = playerA + 1; playerB < players.size(); ++playerB)
        {
            std::cout << std::endl << players[playerA].player->name << " VS " << players[playerB].player->name << std::endl;
            std::vector<playerCharacters> match = {players[playerA], players[playerB]};
            std::vector<playerCharacters> res = detailedGame(match);
            PrintWinner(res);
            UpgradeScore(res);
        }
    }

    std::sort(players.begin(), players.end(), [this](auto a, auto b){ return a.tournamentScore > b.tournamentScore; });
    std::cout << "\nRESULTS!" << std::endl;
    for (size_t i = 0; i < players.size(); ++i)
    {
        std::cout << players[i].player->name << " scored " << players[i].tournamentScore << std::endl;
    }
    for (size_t i = 0; i < players.size(); ++i)
    {
        if (players[i].tournamentScore == players[0].tournamentScore)
        {
            std::cout << players[i].player->name << " ";
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
            std::vector<playerCharacters> match = {players[playerA], players[playerB]};
            std::vector<playerCharacters> res = fastGame(match);
            UpgradeScore(res);
        }
    }

    std::sort(players.begin(), players.end(), [this](auto a, auto b){ return a.tournamentScore > b.tournamentScore; });
    for (size_t i = 0; i < players.size(); ++i)
    {
        if (players[i].tournamentScore == players[0].tournamentScore)
        {
            std::cout << players[i].player->name << " ";
        }
        else
        {
            break;
        }
    }
    std::cout << "WIN!" << std::endl;
}