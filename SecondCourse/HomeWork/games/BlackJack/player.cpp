#include "player.h"

std::vector<Card> Person::SeeCards()
{
    return card;
}

Card Person::SeeLastCard()
{
    return card.at(card.size() - 1);
}

int Person::GetScore()
{
    return score;
}

bool Person::GetCard(Deck & deck)
{
    Card curCard = deck.PopCard();
    card.push_back(curCard);

    score += curCard.power;
    if (score > 21)
    {
        for (size_t i = 0; i < card.size(); ++i)
        {
            if (card[i].power == 11) 
            {
                card[i].power = 1;
                score -= 10;
                break;
            }
        }

        if (score > 21) return 1;
    }
    return 0;
}