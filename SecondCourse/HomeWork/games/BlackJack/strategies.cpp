#include "strategies.h"

Player::Player(std::string name) : name(name) {}

std::string Player::makeAction()
{
    std::cout << name << ": ";
    std::string status;
    std::cin >> status;

    return status;
}

char ** GetStrategy(std::string && name, std::string & configFile)
{
    std::string patch = configFile;
    if (patch.back() != '\\') patch += '\\';
    patch += name + ".csv";
    std::ifstream config;
    config.open(patch);
    if (!config.is_open()) 
    {
        std::cerr << name << " " << patch << std::endl;
        throw std::runtime_error("Strategies file isn't available...");
    }

    //config file file must be of type: 17 rows, 10 columns.
    //columns : enemy card(2 - 11), rows : your score (4 - 20)
    char ** strategyConfig = new char* [21];
    for (int i = 0; i <21; ++i) strategyConfig[i] = new char[12];

    for (int i = 4; i <= 20; ++i)
    {
        for (int j = 2; j <= 11; j++)
        {
            static char value;
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

Bot1::~Bot1()
{
    if (strategyTable != nullptr)
    {
        for (int i = 0; i < 21; ++i) delete[] strategyTable[i];
        delete[] strategyTable;
    }
}

void Bot2::generateStrategyTable() 
{
    strategyTable = GetStrategy("bot2", configFile);
}

Bot2::~Bot2()
{
    if (strategyTable != nullptr)
    {
        for (int i = 0; i < 21; ++i) delete[] strategyTable[i];
        delete[] strategyTable;
    }
}

void MetaBot::generateStrategyTable()
{
    riskStrategy = GetStrategy("bot1", configFile);
    normStrategy = GetStrategy("bot2", configFile);
}

MetaBot::~MetaBot()
{
    if (riskStrategy != nullptr) 
    {
        for (int i = 0; i < 21; ++i) delete[] riskStrategy[i];
        delete[] riskStrategy;
    }

    if (normStrategy != nullptr)
    {
        for (int i = 0; i < 21; ++i) delete[] normStrategy[i];
        delete[] normStrategy;
    }
}

std::string TrivialBot1::makeAction()
{
    if (score < 16) return "g";
    return "s";
}

std::string TrivialBot2::makeAction()
{
    if ((rand() % 100) < (score * 100 / 24)) return "s";
    return "g";
}

std::string TrivialBot3::makeAction()
{
    if (rand() % 2) return "g";
    return "s";
}

std::string Bot1::makeAction()
{
    if (strategyTable == nullptr) generateStrategyTable();
    std::string a = "";
    a += strategyTable[score][enemyCard.power];
    return a;
}

std::string Bot2::makeAction()
{
    if (strategyTable == nullptr) generateStrategyTable();
    std::string a = "";
    a += strategyTable[score][enemyCard.power];
    return a;
}

std::string MetaBot::makeAction()
{
    if (riskStrategy == nullptr || normStrategy == nullptr) generateStrategyTable();
    std::string a = "";
    if (enemyCard.power >= 8) a += riskStrategy[score][enemyCard.power];
    else a += normStrategy[score][enemyCard.power];

    return a;
}

Player* CreateTrivialBot1()
{
    return new TrivialBot1;
}

Player* CreateTrivialBot2()
{
    return new TrivialBot2;
}

Player* CreateTrivialBot3()
{
    return new TrivialBot3;
}

Player* CreateBot1()
{
    return new Bot1();
}

Player* CreateBot2()
{
    return new Bot2();
}

Player* CreateMetaBot()
{
    return new MetaBot();
}