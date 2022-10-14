#ifndef PLAYER_H
#define PLAYER_H

#include "blackjack.h"
#include "deck.h"

class Player
{
public:
    Player(std::string name) : name(name) {}
    virtual std::string makeAction();
    Card GetCard(Deck & deck);
    bool GoodScore();
    bool TryEditAce(); //Power Ace = 1, return 0 if don't searched
    int GetScore();
    std::vector<Card> SeeCards();
    std::string name;
private:
    int score = 0;
    std::vector<Card> card;
};

class Bot : public Player
{
public:
    Bot(std::string name) : Player(name) {}
    std::string makeAction();
};

#endif