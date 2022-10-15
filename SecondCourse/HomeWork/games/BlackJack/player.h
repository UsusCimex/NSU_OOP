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
    virtual std::string makeAction();
    Card getCard(Deck & deck);
    bool goodScore();
    bool tryEditAce(); //Power Ace = 1, return 0 if don't searched
    int getScore();
    std::vector<Card> seeCards();
    std::string name;
private:
    int score = 0;
    int tournamentScore = 0;
    std::vector<Card> card;
};

class Bot : public Player
{
public:
    Bot(std::string name) : Player(name) {}
    std::string makeAction() override;
};

#endif