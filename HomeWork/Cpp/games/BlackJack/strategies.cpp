#include "strategies.h"

void Strategies::setConfigFile(std::string patch)
{
    configFile = patch;
}

void Strategies::setScore(int newScore)
{
    score = newScore;
}

void Strategies::setEnemyCard(Card card)
{
    enemyCard = card;
}

Card Strategies::getEnemyCard()
{
    return enemyCard;
}

void Strategies::setHand(std::vector<Card> newHand)
{
    hand = newHand;
}

Action Strategies::makeAction()
{
    return Action::NOACTION;
}

std::vector <std::vector<std::string>> GetStrategy(std::string name, std::string & configFile)
{
    std::string patch = configFile;
    if (patch.back() != '/') patch += '/'; //In linux / , in Windows '\\'
    patch += name + ".csv";
    std::ifstream config;
    config.open(patch);
    if (!config.is_open())
    {
        std::cerr << patch << " isn't available!" << std::endl;
        throw std::runtime_error("Strategies file isn't available...");
    }

    //config file file must be of type: 17 rows, 10 columns.
    //columns : enemy card(2 - 11), rows : your score (4 - 20)
    std::vector <std::vector<std::string>> strategyConfig(21, std::vector<std::string>(12)); 

    char value;
    for (int i = 4; i <= 20; ++i)
    {
        for (int j = 2; j <= 11; j++)
        {
            config >> value;
            if (value == ',') config >> value;
            strategyConfig[i][j] = value;
        }
    }
    
    config.close();

    return strategyConfig;
}