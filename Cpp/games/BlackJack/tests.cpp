#include <gtest/gtest.h>

#include "settings.h"

TEST(InputData, BadInput)
{
    const char * input[] = {"bad_input"};
    EXPECT_ANY_THROW(StartSetting(1, input));
    const char * input2[] = {"bad", "input"};
    EXPECT_ANY_THROW(StartSetting(2, input2));
    const char * input3[] = {"null", "-bot1", "-bot2"}; //need config file
    EXPECT_ANY_THROW(StartSetting(3, input3));
    const char * input4[] = {"null", "-bot1", "-bot2", "--config=badpatch"}; //Bad config file
    EXPECT_ANY_THROW(StartSetting(4, input4));
    const char * input5[] = {"bad", "nobot", "-bot1", "--config=config", "--mode=fast"};
    EXPECT_ANY_THROW(StartSetting(5, input5));
}
TEST(BlackJack, TestTrivialBot)
{
    const char * input[] = {"null", "-trivialbot1", "-trivialbot2", "--mode=detailed", "--config=config"};
    EXPECT_NO_THROW(StartSetting(5, input));
    const char * input2[] = {"null", "-trivialbot1", "-trivialbot3", "--mode=fast"};
    EXPECT_NO_THROW(StartSetting(4, input2));
    const char * input3[] = {"null", "-trivialbot2", "-trivialbot3"};
    EXPECT_NO_THROW(StartSetting(3, input3));
}
TEST(BlackJack, TestBot)
{
    const char * input[] = {"null", "-bot1", "-bot2", "--config=config"};
    EXPECT_NO_THROW(StartSetting(4, input));
    const char * input2[] = {"null", "-bot2", "-bot1", "--config=config", "--mode=fast"};
    EXPECT_NO_THROW(StartSetting(5, input2));
    const char * input3[] = {"null", "-metabot", "-bot1", "--config=config", "--mode=detailed"};
    EXPECT_NO_THROW(StartSetting(5, input3));
}
TEST(BlackJack, TestTournament)
{
    const char * input[] = {"null", "-trivialbot1", "-trivialbot2", "-trivialbot3"};
    EXPECT_NO_THROW(StartSetting(4, input));
    const char * input2[] = {"null", "-trivialbot3", "-trivialbot2", "-trivialbot1", "--mode=tournament"};
    EXPECT_NO_THROW(StartSetting(5, input2));
    const char * input3[] = {"null", "-trivialbot1", "-bot1", "-bot2", "--mode=fasttournament", "--config=config"};
    EXPECT_NO_THROW(StartSetting(6, input3));
    const char * input4[] = {"null", "-bot1", "-trivialbot1", "-trivialbot2", "--config=config", "--mode=fast"};
    EXPECT_NO_THROW(StartSetting(5, input4));
}

TEST(BlackJack, TestMetaBots)
{
    const char * input[] = {"null", "-metabot", "-trivialbot1", "--config=config", "--mode=fast"};
    EXPECT_NO_THROW(StartSetting(5, input));
    const char * input2[] = {"null", "-metabot", "-trivialbot2", "--config=config", "--mode=fast"};
    EXPECT_NO_THROW(StartSetting(5, input2));
    const char * input3[] = {"null", "-metabot", "-trivialbot3", "--config=config", "--mode=fast"};
    EXPECT_NO_THROW(StartSetting(5, input3));
    const char * input4[] = {"null", "-metabot", "-bot1", "--config=config", "--mode=fast"};
    EXPECT_NO_THROW(StartSetting(5, input4));
    const char * input5[] = {"null", "-metabot", "-bot2", "--config=config", "--mode=fast"};
    EXPECT_NO_THROW(StartSetting(5, input5));
}

int main(int argc, char * argv[])
{
    // ::testing::InitGoogleTest(&argc, argv);
	// return RUN_ALL_TESTS();

    StartSetting(argc, (const char **)argv);
}