#include "player.h"

void PlayerCharacters::syncWithPlayer()
{
    player->score = score;
    player->hand = hand;
}

Card PlayerCharacters::getCard(Deck & deck)
{
    Card curCard = deck.PopCard();
    hand.push_back(curCard);

    score += curCard.power;

    syncWithPlayer();

    return curCard;
}

bool PlayerCharacters::tryEditAce()
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

bool PlayerCharacters::checkScore()
{
    if (score > 21) return tryEditAce();
    return 1;
}