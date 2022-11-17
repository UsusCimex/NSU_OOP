#ifndef PLAYER_H
#define PLAYER_H

#include <memory>

#include "strategies.h"
#include "deck.h"
#include "blackjack.h"


class Player
{
public:
    std::shared_ptr<Strategies> player;
    void syncWithPlayer();
    std::string getName();
    int getScore();
    int getTournamentScore();
    void changeTournamentScore(int);
    std::vector<Card> getHand();

    //Get a card from the deck
    Card getCard(Deck &);
    //If Score > 21, start method tryEditAce. If score is good return 1
    bool checkScore();
private:
    std::string name;
    std::vector<Card> hand;
    int score = 0;
    int tournamentScore = 0;

    //Power Ace = 1, return 0 if don't searched
    bool tryEditAce();    
};

#endif