#include "deck.h"

Card::Card(std::string card, int power) : card(card), power(power) {}
Deck::Deck(int countDecks) : countDecks(countDecks) {}

Card Deck::PopCard()
{
    if (deckPointer >= (size_t)(kDeckSize * countDecks)) throw std::runtime_error("deck is empty");
    return deck[deckPointer++];
}

void Deck::generateDeck()
{
    for (size_t i = 0; i < (size_t)(kDeckSize * countDecks); ++i)
    {
        std::string card;

        if ((i % kDeckSize) / 4 == 0) card += "2 ";
        else if ((i % kDeckSize) / 4 == 1) card += "3 ";
        else if ((i % kDeckSize) / 4 == 2) card += "4 ";
        else if ((i % kDeckSize) / 4 == 3) card += "5 ";
        else if ((i % kDeckSize) / 4 == 4) card += "6 ";
        else if ((i % kDeckSize) / 4 == 5) card += "7 ";
        else if ((i % kDeckSize) / 4 == 6) card += "8 ";
        else if ((i % kDeckSize) / 4 == 7) card += "9 ";
        else if ((i % kDeckSize) / 4 == 8) card += "10 ";
        else if ((i % kDeckSize) / 4 == 9) card += "Queen ";
        else if ((i % kDeckSize) / 4 == 10) card += "Jack ";
        else if ((i % kDeckSize) / 4 == 11) card += "King ";
        else if ((i % kDeckSize) / 4 == 12) card += "Ace ";

        if ((i % kDeckSize) % 4 == 0) card += "Clubs";
        else if ((i % kDeckSize) % 4 == 1) card += "Spades";
        else if ((i % kDeckSize) % 4 == 2) card += "Hearts";
        else if ((i % kDeckSize) % 4 == 3) card += "Diamonds";

        Card c(card, std::min(((i % kDeckSize) / 4) + 2, (size_t)10));
        if ((i % kDeckSize) / 4 == 12) c.power = 11;

        deck.push_back(c);
    }

    for (size_t i = 0; i < (size_t)(kDeckSize * countDecks); ++i)
    {
        int rm = rand() % (kDeckSize * countDecks);
        std::swap(deck[i % (kDeckSize * countDecks)], deck[rm]);
    }

    deckPointer = 0ull;
}