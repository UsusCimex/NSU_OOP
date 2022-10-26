#ifndef PLAYER_H
#define PLAYER_H

#include "blackjack.h"
#include "deck.h"

class Player
{
public:
    Player(std::string name) : name(name) {}
    //Remove all cards and reset the account. Tournament score remains the same
    void reset();
    //Edit tournament score, your score += score
    void addTournamentScore(int score);
    //Getter tournament score
    int getTournamentScore();
    //Choice of action
    virtual std::string makeAction(Player * enemy);
    //Get a card from the deck
    Card getCard(Deck &);
    //If Score > 21, start method tryEditAce. If score is good return 1
    bool checkScore();
    //Power Ace = 1, return 0 if don't searched
    bool tryEditAce();
    int getScore();
    //See the first(opened) card.
    //The method needed for opponents
    Card seeCard();
    std::string name;
protected:
    int score = 0;
    int tournamentScore = 0;
    std::vector<Card> card;
};

#endif