#ifndef PLAYER_H
#define PLAYER_H

#include "blackjack.h"
#include "deck.h"

struct Person
{
public:
    Person(std::string name) : name(name) {}
    bool GetCard(Deck & deck);
    int GetScore();
    std::vector<Card> SeeCards();
    Card SeeLastCard();
    std::string name;
private:
    int score = 0;
    std::vector<Card> card;
};

#endif