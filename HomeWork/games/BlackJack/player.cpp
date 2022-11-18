#include "player.h"

void Player::syncWithPlayer()
{
    player->setScore(score);
    player->setHand(hand);
}

std::string Player::getName()
{
    return name;
}

void Player::setName(std::string newName)
{
    name=newName;
}

int Player::getScore()
{
    return score;
}

int Player::getTournamentScore()
{
    return tournamentScore;
}

void Player::changeTournamentScore(int value)
{
    tournamentScore += value;
}

Card Player::getCard(Deck & deck)
{
    Card curCard = deck.PopCard();
    hand.push_back(curCard);

    score += curCard.power;

    syncWithPlayer();

    return curCard;
}

std::vector<Card> Player::getHand()
{
    return hand;
}

bool Player::tryEditAce()
{
    for (size_t i = 0; i < hand.size(); ++i)
    {
        if (hand[i].power == 11)
        {
            hand[i].power = 1;
            score -= 10;

            syncWithPlayer();

            return 1;
        }
    }
    return 0;
}

bool Player::checkScore()
{
    if (score > 21) return tryEditAce();
    return 1;
}