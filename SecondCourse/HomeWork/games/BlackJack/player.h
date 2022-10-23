#ifndef PLAYER_H
#define PLAYER_H

#include "blackjack.h"
#include "deck.h"

class Player
{
public:
    Player(std::string name) : name(name) {}
    void reset();
    void addTournamentScore(int score);
    int getTournamentScore();
    virtual std::string makeAction(Player * enemy);
    Card getCard(Deck & deck);
    bool checkScore(); // If Score > 21, start method tryEditAce. If score is good return 1
    bool tryEditAce(); //Power Ace = 1, return 0 if don't searched
    int getScore();
    Card seeCard(); //See the first(opened) card.
    std::string name;
protected:
    int score = 0;
    int tournamentScore = 0;
    std::vector<Card> card;
};

#endif