#include "deck.h"

Card Deck::PopCard()
{
    if (deckPointer >= (size_t)(DECKSIZE * countDecks)) throw std::runtime_error("deck is empty");
    return deck[deckPointer++];
}

void Deck::generateDeck()
{
    for (size_t i = 0; i < (size_t)(DECKSIZE * countDecks); ++i)
    {
        std::string card;

        if ((i % DECKSIZE) / 4 == 0) card += "2 ";
        else if ((i % DECKSIZE) / 4 == 1) card += "3 ";
        else if ((i % DECKSIZE) / 4 == 2) card += "4 ";
        else if ((i % DECKSIZE) / 4 == 3) card += "5 ";
        else if ((i % DECKSIZE) / 4 == 4) card += "6 ";
        else if ((i % DECKSIZE) / 4 == 5) card += "7 ";
        else if ((i % DECKSIZE) / 4 == 6) card += "8 ";
        else if ((i % DECKSIZE) / 4 == 7) card += "9 ";
        else if ((i % DECKSIZE) / 4 == 8) card += "10 ";
        else if ((i % DECKSIZE) / 4 == 9) card += "Queen ";
        else if ((i % DECKSIZE) / 4 == 10) card += "Jack ";
        else if ((i % DECKSIZE) / 4 == 11) card += "King ";
        else if ((i % DECKSIZE) / 4 == 12) card += "Ace ";

        if ((i % DECKSIZE) % 4 == 0) card += "Clubs";
        else if ((i % DECKSIZE) % 4 == 1) card += "Spades";
        else if ((i % DECKSIZE) % 4 == 2) card += "Hearts";
        else if ((i % DECKSIZE) % 4 == 3) card += "Diamonds";

        Card c(card, std::min(((i % DECKSIZE) / 4) + 2, (size_t)10));
        if ((i % DECKSIZE) / 4 == 12) c.power = 11;

        deck.push_back(c);
    }

    for (size_t i = 0; i < (size_t)(DECKSIZE * countDecks); ++i)
    {
        int rm = rand() % (DECKSIZE * countDecks);
        std::swap(deck[i % (DECKSIZE * countDecks)], deck[rm]);
    }

    deckPointer = 0ull;
}