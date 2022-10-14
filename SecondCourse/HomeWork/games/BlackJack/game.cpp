#include "game.h"

void Game::start(Rules rules)
{
    std::vector<Person> players;
    for (size_t i = 0; i < rules.playerCount; ++i)
    {
        std::string name;
        std::cout << "Enter " << i + 1 << " player name: ";
        std::cin >> name;
        players.push_back(Person(name));
    }

    std::cout << "Game started!" << std::endl;
    std::cout << "Control: g - get card, s - stop get card, q - quite, r - see all your cards" << std::endl;

    Deck deck;
    deck.GenerateDeck();

    std::vector<Person> pr; //

    bool game = 1;
    std::string read;
    
    while (game)
    {
        int pl;
        for (pl = 0; pl < players.size(); ++pl)
        {
            while (true)
            {
                std::cout << players[pl].name << ": ";
                std::cin >> read;
                
                if (read == "g")
                {
                    if (players[pl].GetCard(deck))
                    {
                        std::cout << players[pl].name << ": You took " << players[pl].SeeLastCard().card << " and your score: " << players[pl].GetScore() << std::endl;
                        std::cout << "System: Player " << players[pl].name << " lost, his score is " << players[pl].GetScore() <<  std::endl;
                        players.erase(players.begin() + pl);
                        pl--;
                        if (players.size() <= 1)
                        {
                            pr.push_back(players.at(0));
                            game = 0;
                            break;
                        }
                        continue;
                    }
                    std::cout << players[pl].name << ": You took " << players[pl].SeeLastCard().card << " and your score: " << players[pl].GetScore() << std::endl;
                    if (players[pl].GetScore() == 21)
                    {
                        std::cout << "Player " << players[pl].name << " WIN!!!" << std::endl;
                        return;
                    }
                }
                else if (read == "s")
                {
                    pr.push_back(players[pl]);
                    players.erase(players.begin() + pl);
                    pl--;
                    if (players.size() == 0)
                    {
                        game = 0;
                        break;
                    }
                }
                else if (read == "q")
                {
                    std::cout << "System: Player " << players[pl].name << " lost" << std::endl;
                    players.erase(players.begin() + pl);
                    pl--;
                    if (players.size() <= 1)
                    {
                        pr.push_back(players.at(0));
                        game = 0;
                        break;
                    }
                }
                else if (read == "r")
                {
                    for (auto el : players[pl].SeeCards()) std::cout << el.card << ", ";
                    std::cout << "Score: " << players[pl].GetScore() << std::endl;
                    continue;
                }
                else
                {
                    continue;
                }
                break;
            }
        }
    }

    if (pr.size() == 0) std::cout << "System: Tie" << std::endl;
    else
    {
        for (int i = 0; i < pr.size(); ++i)
        {
            std::cout << pr[i].name << " scored " << pr[i].GetScore() << " points" << std::endl; 
        }
    }
}