#include "player.h"

void playerCharacters::syncWithPlayer()
{
    player->score = score;
    player->hand = hand;
}

Card playerCharacters::getCard(Deck & deck)
{
    Card curCard = deck.PopCard();
    hand.push_back(curCard);

    score += curCard.power;

    syncWithPlayer();

    return curCard;
}

bool playerCharacters::tryEditAce()
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

bool playerCharacters::checkScore()
{
    if (score > 21) return tryEditAce();
    return 1;
}

void playerCharacters::resetHand()
{
    score = 0;
    hand.clear();

    syncWithPlayer();
}