#include <gtest/gtest.h>
#include <string>
#include "FlatMap.h"

class FlatMapTest : public testing::Test
{
	protected: FlatMap<std::string, int> a, b;	
};

TEST_F(FlatMapTest, Insert_Erase_Size_Empty_At)
{
	ASSERT_TRUE(a.insert("fValue", 23));
	ASSERT_TRUE(a.insert("sValue", 91));
	ASSERT_TRUE(a.insert("tValue", 13));
	ASSERT_TRUE(a.insert("foValue", 32));

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
	ASSERT_ANY_THROW(a.at("newValue"));

	ASSERT_TRUE(a.erase("sValue"));
	ASSERT_TRUE(a.erase("foValue"));
	ASSERT_FALSE(a.erase("rnValue"));

	ASSERT_TRUE(a.contains("fValue"));
	ASSERT_FALSE(a.contains("sValue"));
	ASSERT_TRUE(a.contains("tValue"));
	ASSERT_FALSE(a.contains("foValue"));

	ASSERT_EQ(a.size(), 2);

	ASSERT_TRUE(a.erase("fValue"));
	ASSERT_TRUE(a.erase("tValue"));
	ASSERT_FALSE(a.erase("fValue"));

	ASSERT_TRUE(a.empty());
	ASSERT_EQ(a.size(), 0);
}

TEST_F(FlatMapTest, EqualMaps)
{
	ASSERT_TRUE(a.insert("key1", 1));
	ASSERT_TRUE(a.insert("key2", 2));
	ASSERT_TRUE(a.insert("key3", 3));

	b = a;
	ASSERT_TRUE(a == b);
	
	ASSERT_TRUE(b.erase("key2"));
	ASSERT_FALSE(a == b);
	ASSERT_TRUE(a != b);

	ASSERT_TRUE(b.insert("key2", 4));
	ASSERT_FALSE(a == b);
}

TEST_F(FlatMapTest, SwapMaps) //BAD...
{
	ASSERT_TRUE(a.insert("aKey1", 1));
	ASSERT_TRUE(a.insert("aKey2", 2));
	ASSERT_TRUE(a.insert("aKey3", 3));
	ASSERT_TRUE(b.insert("bKey1", 4));
	ASSERT_TRUE(b.insert("bKey2", 5));

	ASSERT_FALSE(a == b);
	a.swap(b);
	ASSERT_FALSE(a == b);
	ASSERT_TRUE(a.contains("bKey2"));
	ASSERT_FALSE(b.contains("bKey1"));
	ASSERT_TRUE(b.contains("aKey1"));
	ASSERT_EQ(b.at("aKey3"), 3);

	b.swap(a);
	ASSERT_TRUE(a.contains("aKey1"));
}

TEST_F(FlatMapTest, ClearMap)
{
	std::string str = ".";
	for (int i = 0; i < 100; ++i)
	{
		ASSERT_TRUE(a.insert(str, i));
		str.push_back('.');
		ASSERT_EQ(i + 1, a.size());
	}

	a.clear();
	ASSERT_EQ(0, a.size());
	ASSERT_ANY_THROW(a.at("..."));
	ASSERT_ANY_THROW(a.at(""));
}

TEST_F(FlatMapTest, CheckResize)
{
	std::string str = ".";
	for (int i = 0; i < 10000; ++i)
	{
		ASSERT_TRUE(a.insert(str, i));
		str += '.';
		ASSERT_EQ(i + 1, a.size());
	}

	a.clear();
	ASSERT_EQ(0, a.size());
}

int main(int argc, char *argv[])
{
	::testing::InitGoogleTest(&argc, argv);
	return RUN_ALL_TESTS();
}