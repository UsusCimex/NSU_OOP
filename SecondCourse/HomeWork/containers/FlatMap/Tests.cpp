#include <gtest/gtest.h>
#include <string>
#include "FlatMap.h"

class FlatMapTest : public testing::Test
{
	protected: FlatMap<std::string, int> a, b;	
};

TEST_F(FlatMapTest, checkInsertAndErase)
{
	a.insert("fValue", 23);
	a.insert("sValue", 91);
	a.insert("tValue", 13);
	a.insert("foValue", 32);

	ASSERT_FALSE(a.empty());
	ASSERT_EQ(a.size(), 4);
	
	ASSERT_TRUE(a.contains("sValue"));
	ASSERT_TRUE(a.contains("tValue"));
	ASSERT_FALSE(a.contains("randomValue"));
	ASSERT_FALSE(a.contains("mValue"));

	ASSERT_EQ(a["fValue"], 23);
	ASSERT_EQ(a["sValue"], 91);
	ASSERT_EQ(a["tValue"], 13);
	ASSERT_EQ(a["foValue"], 32);

	ASSERT_EQ(a.at("fValue"), 23);
	ASSERT_EQ(a.at("sValue"), 91);
	ASSERT_EQ(a.at("tValue"), 13);
	ASSERT_EQ(a.at("foValue"), 32);

	a.erase("sValue");
	a.erase("foValue");

	ASSERT_TRUE(a.contains("fValue"));
	ASSERT_FALSE(a.contains("sValue"));
	ASSERT_TRUE(a.contains("tValue"));
	ASSERT_FALSE(a.contains("foValue"));

	ASSERT_EQ(a.size(), 2);

	a.erase("fValue");
	a.erase("tValue");

	ASSERT_FALSE(a.empty());
	ASSERT_EQ(a.size(), 0);	
}

// TEST_F(FlatMapTest, EqualMaps)
// {
// 	a.insert("fValue", 15);
// }

int main(int argc, char *argv[])
{
	::testing::InitGoogleTest(&argc, argv);
	return RUN_ALL_TESTS();
}