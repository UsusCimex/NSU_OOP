#ifndef PLAYER_H
#define PLAYER_H

#include "blackjack.h"
#include "strategies.h"
#include "deck.h"

struct playerCharacters
{
    Player* player;
    std::vector<Card> hand;
    int score = 0;
    int tournamentScore = 0;

    void syncWithPlayer();

    //Edit tournament score, your score += score
    void addTournamentScore(int score);
    //Remove hand deck. Tournament score remains the same
    void resetHand();
    //If Score > 21, start method tryEditAce. If score is good return 1
    bool checkScore();
    //Power Ace = 1, return 0 if don't searched
    bool tryEditAce();
    //Get a card from the deck
    Card getCard(Deck &);
};

#endif