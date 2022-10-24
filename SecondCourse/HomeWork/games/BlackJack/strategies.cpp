#include "strategies.h"

char ** GetStrategy(std::string name)
{
    std::string patch = rules.configFile;
    if (patch.back() != '\\') patch += '\\';
    patch += name + ".csv";
    std::ifstream config;
    config.open(patch);
    if (!config.is_open()) throw std::runtime_error("Strategies file isn't available...");

    //config file file must be of type: 17 rows, 10 columns.
    //columns : enemy card(2 - 11), rows : your score (4 - 20)
    char ** strategyConfig = new char* [21];
    for (int i = 0; i <21; ++i) strategyConfig[i] = new char[12];

    std::cerr << "[ PARSING ] " << patch << std::endl; /////
    for (int i = 4; i <= 20; ++i)
    {
        for (int j = 2; j <= 11; j++)
        {
            static char value;
            value = config.get();
            config >> value;
            if (value == ',') config >> value;
            // std::cerr << value << " ";
            strategyConfig[i][j] = value;
        }
        // std::cerr << std::endl;
    }
    // std::cerr << std::endl;

    for (int i = 4; i <= 20; ++i)
    {
        for (int j = 2; j <= 11; j++)
        {
            std::cerr << strategyConfig[i][j] << " ";
        }
        std::cerr << std::endl;
    }
    std::cerr << std::endl;

    config.close();

    return strategyConfig;
}

std::string TrivialBot1::makeAction(Player * enemy)
{
    if (getScore() < 16) return "g";
    return "s";
}

std::string TrivialBot2::makeAction(Player * enemy)
{
    if ((rand() % 100) < (getScore() * 100 / 24)) return "s";
    return "g";
}

std::string TrivialBot3::makeAction(Player * enemy)
{
    if (rand() % 2) return "g";
    return "s";
}

std::string Bot1::makeAction(Player * enemy)
{
    std::string a = "";
    a += strategyTable[getScore()][enemy->seeCard().power];
    std::cerr << "[ \033[33mDEBUG\033[0m ]  " << a << " " << getScore() << " " << enemy->seeCard().power << std::endl;
    return a;
}

std::string Bot2::makeAction(Player * enemy)
{
    std::string a = "";
    a += strategyTable[getScore()][enemy->seeCard().power];
    std::cerr << "[ \033[34mDEBUG\033[0m ]  " << a << " " << getScore() << " " << enemy->seeCard().power << std::endl;
    return a;
}

std::string MetaBot::makeAction(Player * enemy)
{
    if (enemy->seeCard().power >= 8) return "g";
    else return "s";
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
    return new MetaBot;
}