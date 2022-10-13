#include <iostream>
#include <string>
#include <map>
#include <array>
#include <vector>

#define DECKSIZE 52

enum Status
{
    DEF, //default
    WIN,
    LOSE
};

struct Card
{
    Card() = default;
    Card(std::string card, int power) : card(card), power(power) {}
    std::string card;
    size_t power;
};

struct Deck
{
public:
    void GenerateDeck();
    Card PopCard();
    void PrintDeck();
private:
    std::array<Card, DECKSIZE> deck;
    size_t deckPointer = 0ull;
};

Card Deck::PopCard()
{
    if (deckPointer >= DECKSIZE) throw std::runtime_error("deck is empty");
    return deck[deckPointer++];
}

void Deck::GenerateDeck()
{
    for (size_t i = 0; i < DECKSIZE; ++i) //Know... It isn't normal :d
    {
        std::string card;

        if (i / 4 == 0) card += "2 ";
        else if (i / 4 == 1) card += "3 ";
        else if (i / 4 == 2) card += "4 ";
        else if (i / 4 == 3) card += "5 ";
        else if (i / 4 == 4) card += "6 ";
        else if (i / 4 == 5) card += "7 ";
        else if (i / 4 == 6) card += "8 ";
        else if (i / 4 == 7) card += "9 ";
        else if (i / 4 == 8) card += "10 ";
        else if (i / 4 == 9) card += "Queen ";
        else if (i / 4 == 10) card += "Jack ";
        else if (i / 4 == 11) card += "King ";
        else if (i / 4 == 12) card += "Ace ";

        if (i % 4 == 0) card += "Clubs";
        else if (i % 4 == 1) card += "Spades";
        else if (i % 4 == 2) card += "Hearts";
        else if (i % 4 == 3) card += "Diamonds";

        Card c(card, std::min((i / 4) + 2, (size_t)10));
        if (i / 4 == 12) c.power = 11;

        deck[i] = c;
    }

    srand(time(0));

    for (size_t i = 0; i < 3 * DECKSIZE; ++i) //3 times, why not)
    {
        int rm = rand() % DECKSIZE;
        std::swap(deck[i % DECKSIZE], deck[rm]);
    }

    deckPointer = 0ull;
}

void Deck::PrintDeck()
{
    for (int i = 0; i < DECKSIZE; ++i)
    {
        std::cout << deck[i].card << std::endl;
    }
}

struct Person
{
public:
    Person(std::string name) : name(name) {}
    Status GetCard(Deck & deck);
    int GetScore();
    std::vector<Card> SeeCards();
    Card SeeLastCard();
    std::string name;
private:
    int score = 0;
    std::vector<Card> card;
};

std::vector<Card> Person::SeeCards()
{
    return card;
}

Card Person::SeeLastCard()
{
    return card.at(card.size() - 1);
}

int Person::GetScore()
{
    return score;
}

Status Person::GetCard(Deck & deck)
{
    Card curCard = deck.PopCard();
    card.push_back(curCard);

    score += curCard.power;
    if (score > 21)
    {
        for (size_t i = 0; i < card.size(); ++i)
        {
            if (card[i].power == 11) 
            {
                card[i].power = 1;
                score -= 10;
                break;
            }
        }

        if (score > 21) return LOSE;
    }
    if (score == 21) return WIN;
    return DEF;
}

class Game
{
public:
    void start(size_t countPlayer);
};

void Game::start(size_t countPlayer)
{
    std::vector<Person> players;
    for (size_t i = 0; i < countPlayer; ++i)
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
                    if (players[pl].GetCard(deck) == LOSE)
                    {
                        std::cout << players[pl].name << ": You took " << players[pl].SeeLastCard().card << " and your score: " << players[pl].GetScore() << std::endl;
                        std::cout << "System: Player " << players[pl].name << " lost, his score is " << players[pl].GetScore() <<  std::endl;
                        players.erase(players.begin() + pl);
                        pl--;
                        if (players.size() == 1)
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
                    if (players.size() == 1)
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

struct Rules
{
    size_t playerCount = 2;
};

int main(int argc, char ** argv)
{
    if (argc < 2) throw std::invalid_argument("Enter something...");
    Rules rules;
    for (size_t i = 1; i < argc; ++i)
    {
        std::string gt = argv[i];
        if (gt.compare("-playerCount=") > 0)
        {
            std::string n = gt.substr(gt.find('=') + 1);
            rules.playerCount = atoi(n.c_str());
        }
    }
    
    Game game;
    game.start(rules.playerCount);
    return 0;
}