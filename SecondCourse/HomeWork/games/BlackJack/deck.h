#ifndef DECK_H
#define DECK_H

#include "blackjack.h"

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
    void generateDeck();
    Card PopCard();
private:
    std::array<Card, DECKSIZE> deck;
    size_t deckPointer = 0ull;
};

#endif