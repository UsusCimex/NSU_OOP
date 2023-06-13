#ifndef GUI_H
#define GUI_H

#include "player.h"

#include <iostream>
#include <vector>
#include <iomanip>
#include <algorithm>

namespace {
//Selects the winner from two players, by points
void PrintMatchWinner(std::vector<Player> & vec)
{
    unsigned alignment = std::max(vec[0].getName().length(), vec[1].getName().length());
    std::string printStr;
    printStr = vec[0].getName() + " scored " + "\033[32m" + std::to_string(vec[0].getScore()) + "\033[0m";
    std::cout << "/" << std::setfill('=') << std::setw(alignment / 2) << "=" << std::setw(12) << "\033[1m\033[35mMATCH RESULT\033[0m" << std::setfill('=') << std::setw(alignment / 2 + 1) << "=\\" << std::setfill(' ') << std::endl;
    std::cout << std::setw(printStr.length() + 9 + (alignment + 14 - printStr.length() - 9) / 2) << printStr << std::endl;
    printStr = vec[1].getName() + " scored " + "\033[32m" + std::to_string(vec[1].getScore()) + "\033[0m";
    std::cout << std::setw(printStr.length() + 9 + (alignment + 14 - printStr.length() - 9) / 2) << printStr << std::endl;
    
    if ((vec[0].getScore() > 21 && vec[1].getScore() <= 21) || (vec[0].getScore() <= 21 && vec[1].getScore() <= 21 && vec[1].getScore() > vec[0].getScore()) || (vec[0].getScore() > 21 && vec[1].getScore() > 21 && vec[1].getScore() < vec[0].getScore()))
    {
        std::cout << std::setw(vec[1].getName().length() + (alignment + 14 - vec[1].getName().length() - 4) / 2) << vec[1].getName() << " \033[34mWIN!\033[0m" << std::endl;
    }
    else if ((vec[0].getScore() <= 21 && vec[1].getScore() > 21) || (vec[0].getScore() <= 21 && vec[1].getScore() <= 21 && vec[1].getScore() < vec[0].getScore()) || (vec[0].getScore() > 21 && vec[1].getScore() > 21 && vec[1].getScore() > vec[0].getScore()))
    {
        std::cout << std::setw(vec[0].getName().length() + (alignment + 14 - vec[0].getName().length() - 4) / 2) << vec[0].getName() << " \033[34mWIN!\033[0m" << std::endl;
    }
    else
    {
        std::cout << std::setw(4 + 13 + (alignment + 14 - 13) / 2) << "\033[36mTie!\033[0m" << std::endl;
    }

    std::cout << "\\" << std::setfill('=') << std::setw(alignment + 13 + alignment % 2) << "=/" << std::setfill(' ') << std::endl;
}

void PrintTournamentWinner(std::vector<Player> & vec)
{
    std::cout << std::endl;

    std::sort(vec.begin(), vec.end(), [](auto a, auto b){ return a.getTournamentScore() > b.getTournamentScore(); });
    size_t countWinner = 0;
    unsigned maxLength = 0;
    for (size_t i = 0; i < vec.size(); ++i)
    {
        if (vec[i].getTournamentScore() == vec[0].getTournamentScore())
        {
            countWinner++;
        }
        if (vec[i].getName().length() > maxLength) maxLength = vec[i].getName().length();
    }
    maxLength += 11;
    unsigned alignmentWinner = 0;
    for (size_t i = 0; i < countWinner; ++i) alignmentWinner += vec[i].getName().length();
    alignmentWinner += countWinner + 4;
    unsigned alignment = std::max(maxLength, alignmentWinner);

    std::cout << "\033[1m" << "/" << std::setfill('=') << std::setw(alignment / 2) << "=" << std::setw(11) << "\033[35m!!RESULTS!!\033[0m" << std::setfill('=') << std::setw(alignment / 2 + 1) << "\\" << std::setfill(' ') << std::endl;

    for (size_t i = 0; i < vec.size(); ++i)
    {
        std::cout << "\033[1m" << "\033[1m" << std::setw(vec[i].getName().length() + (alignment + 13 - vec[i].getName().length() - 9) / 2) << vec[i].getName() << " scored " << "\033[32m" << std::to_string(vec[i].getTournamentScore()) << "\033[0m" << std::endl;
    }
    std::cout << std::setw(vec[0].getName().length() + (alignment + 13 - alignmentWinner) / 2);
    for (size_t i = 0; i < countWinner; ++i) std::cout << vec[i].getName() << " ";
    std::cout << "\033[1m" << "\033[34mWIN!\033[0m" << std::endl;

    std::cout << "\033[1m" << "\\" << std::setfill('=') << std::setw(alignment + 12 + 1 - alignment % 2) << "/" << std::setfill(' ') << std::endl;
}

void PrintInitialDistr(std::vector<Player> & players)
{
    for (auto &pl : players)
    {
        if (pl.getScore() > 21) pl.checkScore();
        std::cout << pl.getName() << " has " << pl.getScore() << " score, his openned card: " << pl.getHand().back().card << "(" << pl.getHand().back().power << ")" << std::endl;
    }
}
}

#endif