#ifndef DECK_H
#define DECK_H

#include "blackjack.h"

#include <vector>
#include <string>
#include <iostream>

struct Card
{
    Card() = default;
    Card(std::string, int);

    std::string card;
    int power;
};

class Deck
{
public:
    Deck(int);
    void generateDeck();
    Card PopCard();
private:
    std::vector<Card> deck;
    int countDecks;
    size_t deckPointer = 0ull;
};

#endif