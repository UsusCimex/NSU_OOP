#include "player.h"

std::vector<Card> Player::SeeCards()
{
    return card;
}

int Player::GetScore()
{
    return score;
}

Card Player::GetCard(Deck & deck)
{
    Card curCard = deck.PopCard();
    card.push_back(curCard);

    score += curCard.power;
    return curCard;
}

bool Player::TryEditAce()
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

bool Player::GoodScore()
{
    if (score > 21) return TryEditAce();
    return 1;
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