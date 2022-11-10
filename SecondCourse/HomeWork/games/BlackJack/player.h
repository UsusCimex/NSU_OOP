#ifndef PLAYER_H
#define PLAYER_H

#include "strategies.h"
#include "deck.h"
#include "blackjack.h"

#include <memory>

struct PlayerCharacters
{
    std::shared_ptr<Player> player;
    std::string name;
    std::vector<Card> hand;
    int score = 0;
    int tournamentScore = 0;

    void syncWithPlayer();

    //If Score > 21, start method tryEditAce. If score is good return 1
    bool checkScore();
    //Power Ace = 1, return 0 if don't searched
    bool tryEditAce();
    //Get a card from the deck
    Card getCard(Deck &);
};

#endif