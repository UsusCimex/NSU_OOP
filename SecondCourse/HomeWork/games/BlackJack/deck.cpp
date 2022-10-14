#include "deck.h"

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