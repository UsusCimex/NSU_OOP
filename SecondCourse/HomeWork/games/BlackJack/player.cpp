#include "player.h"

std::vector<Card> Player::seeCards()
{
    return card;
}

int Player::getScore()
{
    return score;
}

void Player::addTournamentScore(int score)
{
    tournamentScore += score;
}

int Player::getTournamentScore()
{
    return tournamentScore;
}

Card Player::getCard(Deck & deck)
{
    Card curCard = deck.PopCard();
    card.push_back(curCard);

    score += curCard.power;
    return curCard;
}

bool Player::tryEditAce()
{
    for (size_t i = 0; i < card.size(); ++i)
    {
        if (card[i].power == 11) 
        {
            card[i].power = 1;
            score -= 10;
            return 1;
        }
    }
    return 0;
}

bool Player::goodScore()
{
    if (score > 21) return tryEditAce();
    return 1;
}

void Player::reset()
{
    score = 0;
    card.clear();
}

std::string Player::makeAction()
{
    std::cout << name << ": ";
    std::string status;
    std::cin >> status;

    return status;
}

std::string Bot::makeAction()
{
    return "g"; //temp
}