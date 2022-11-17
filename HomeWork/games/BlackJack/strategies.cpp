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

Action Human::makeAction()
{
    std::cout << "Enter something(h - hit, s - stand): ";
    std::string status;
    std::cin >> status;
    if (status == "hit" || status == "h" || status == "g") return Action::HIT;
    if (status == "stand" || status == "s") return Action::STAND;
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

void Bot1::generateStrategyTable()
{
    strategyTable = GetStrategy("bot1", configFile);
}

void Bot2::generateStrategyTable() 
{
    strategyTable = GetStrategy("bot2", configFile);
}

void MetaBot::generateStrategyTable()
{
    riskStrategy = GetStrategy("bot1", configFile);
    normStrategy = GetStrategy("bot2", configFile);
}

Action TrivialBot1::makeAction()
{
    if (score < 16) return Action::HIT;
    return Action::STAND;
}

Action TrivialBot2::makeAction()
{
    if (score >= 21) return Action::STAND;
    if ((rand() % 100) < (score * 100 / 24)) return Action::STAND;
    return Action::HIT;
}

Action TrivialBot3::makeAction()
{
    if (score >= 21) return Action::STAND;
    if (rand() % 2) return Action::HIT;
    return Action::STAND;
}

Action Bot1::makeAction()
{
    if (strategyTable.empty()) generateStrategyTable();
    if (score >= 21) return Action::STAND;
    std::string a = strategyTable[score][enemyCard.power];
    if (a == "g") return Action::HIT;
    else if (a == "s") return Action::STAND;
    return Action::NOACTION;
}

Action Bot2::makeAction()
{
    if (strategyTable.empty()) generateStrategyTable();
    if (score >= 21) return Action::STAND;
    std::string a = strategyTable[score][enemyCard.power];
    if (a == "g") return Action::HIT;
    else if (a == "s") return Action::STAND;
    return Action::NOACTION;
}

Action MetaBot::makeAction()
{
    if (riskStrategy.empty() || normStrategy.empty()) generateStrategyTable();
    if (score >= 21) return Action::STAND;
    
    std::string a;
    if (enemyCard.power >= 8) a = riskStrategy[score][enemyCard.power];
    else a = normStrategy[score][enemyCard.power];

    if (a == "g") return Action::HIT;
    else if (a == "s") return Action::STAND;
    return Action::NOACTION;
}

Strategies* CreateHuman()
{
    return new Human;
}

Strategies* CreateTrivialBot1()
{
    return new TrivialBot1;
}

Strategies* CreateTrivialBot2()
{
    return new TrivialBot2;
}

Strategies* CreateTrivialBot3()
{
    return new TrivialBot3;
}

Strategies* CreateBot1()
{
    return new Bot1;
}

Strategies* CreateBot2()
{
    return new Bot2;
}

Strategies* CreateMetaBot()
{
    return new MetaBot;
}

auto human = Factory<Strategies, std::string, Strategies *(*)()>::getInstance()->Register("human", CreateHuman);
auto tBot1 = Factory<Strategies, std::string, Strategies *(*)()>::getInstance()->Register("-trivialBot1", CreateTrivialBot1);
auto tBot11 = Factory<Strategies, std::string, Strategies *(*)()>::getInstance()->Register("-trivialbot1", CreateTrivialBot1);
auto tBot2 = Factory<Strategies, std::string, Strategies *(*)()>::getInstance()->Register("-trivialBot2", CreateTrivialBot2);
auto tBot21 = Factory<Strategies, std::string, Strategies *(*)()>::getInstance()->Register("-trivialbot2", CreateTrivialBot2);
auto tBot3 = Factory<Strategies, std::string, Strategies *(*)()>::getInstance()->Register("-trivialBot3", CreateTrivialBot3);
auto tBot31 = Factory<Strategies, std::string, Strategies *(*)()>::getInstance()->Register("-trivialbot3", CreateTrivialBot3);
auto bot1 = Factory<Strategies, std::string, Strategies *(*)()>::getInstance()->Register("-bot1", CreateBot1);
auto bot2 = Factory<Strategies, std::string, Strategies *(*)()>::getInstance()->Register("-bot2", CreateBot2);
auto bot3 = Factory<Strategies, std::string, Strategies *(*)()>::getInstance()->Register("-metabot", CreateMetaBot);