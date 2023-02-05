#include <gtest/gtest.h>
#include <string>
#include "FlatMap.h"

class FlatMapTest : public testing::Test
{
	protected: FlatMap<std::string, int> a, b;	
};

TEST_F(FlatMapTest, Insert_Erase_Size_Empty_At)
{
	EXPECT_TRUE(a.insert("fValue", 23));
	EXPECT_TRUE(a.insert("sValue", 91));
	EXPECT_TRUE(a.insert("tValue", 13));
	EXPECT_TRUE(a.insert("foValue", 32));

	EXPECT_FALSE(a.empty());
	EXPECT_EQ(a.size(), 4);
	
	EXPECT_TRUE(a.contains("sValue"));
	EXPECT_TRUE(a.contains("tValue"));
	EXPECT_FALSE(a.contains("randomValue"));
	EXPECT_FALSE(a.contains("mValue"));

	EXPECT_EQ(a["fValue"], 23);
	EXPECT_EQ(a["sValue"], 91);
	EXPECT_EQ(a["tValue"], 13);
	EXPECT_EQ(a["foValue"], 32);

	EXPECT_EQ(a.at("fValue"), 23);
	EXPECT_EQ(a.at("sValue"), 91);
	EXPECT_EQ(a.at("tValue"), 13);
	EXPECT_EQ(a.at("foValue"), 32);
	EXPECT_ANY_THROW(a.at("newValue"));

	EXPECT_TRUE(a.erase("sValue"));
	EXPECT_TRUE(a.erase("foValue"));
	EXPECT_FALSE(a.erase("rnValue"));

	EXPECT_TRUE(a.contains("fValue"));
	EXPECT_FALSE(a.contains("sValue"));
	EXPECT_TRUE(a.contains("tValue"));
	EXPECT_FALSE(a.contains("foValue"));

	EXPECT_EQ(a.size(), 2);

	EXPECT_TRUE(a.erase("fValue"));
	EXPECT_TRUE(a.erase("tValue"));
	EXPECT_FALSE(a.erase("fValue"));

	EXPECT_TRUE(a.empty());
	EXPECT_EQ(a.size(), 0);

	EXPECT_TRUE(a.insert("newValue", 0));
	EXPECT_EQ(a.at("newValue"), 0);
	EXPECT_ANY_THROW(a.at("lastValue"));

	EXPECT_EQ(a.size(), 1);
}

TEST_F(FlatMapTest, EqualMaps) 
{
	EXPECT_TRUE(a.insert("key1", 1));
	EXPECT_TRUE(a.insert("key2", 2));
	EXPECT_TRUE(a.insert("key3", 3));

	b = a;
	EXPECT_TRUE(a == b);
	
	EXPECT_TRUE(b.erase("key2"));
	EXPECT_FALSE(a == b);
	EXPECT_TRUE(a != b);

	EXPECT_TRUE(b.insert("key2", 4));
	EXPECT_FALSE(a == b);

	a=a;
	EXPECT_TRUE(a == a);
}

TEST_F(FlatMapTest, SwapMaps)
{
	EXPECT_TRUE(a.insert("aKey1", 1));
	EXPECT_TRUE(a.insert("aKey2", 2));
	EXPECT_TRUE(a.insert("aKey3", 3));
	EXPECT_TRUE(b.insert("bKey1", 4));
	EXPECT_TRUE(b.insert("bKey2", 5));

	EXPECT_FALSE(a == b);
	a.swap(b);
	EXPECT_FALSE(a == b);
	EXPECT_TRUE(a.contains("bKey2"));
	EXPECT_FALSE(b.contains("bKey1"));
	EXPECT_TRUE(b.contains("aKey1"));
	EXPECT_EQ(b.at("aKey3"), 3);

	b.swap(a);
	EXPECT_TRUE(a.contains("aKey1"));
}

TEST_F(FlatMapTest, ClearMap)
{
	std::string str = ".";
	for (int i = 0; i < 100; ++i)
	{
		EXPECT_TRUE(a.insert(str, i));
		str.push_back('.');
		EXPECT_EQ(i + 1, a.size());
	}

	a.clear();
	EXPECT_EQ(0, a.size());
	EXPECT_ANY_THROW(a.at("..."));
	EXPECT_ANY_THROW(a.at(""));
}

TEST_F(FlatMapTest, CheckResize)
{
	std::string str = ".";
	for (int i = 0; i < 10000; ++i)
	{
		EXPECT_TRUE(a.insert(str, i));
		str += '.';
		EXPECT_EQ(i + 1, a.size());
	}

	a.clear();
	EXPECT_EQ(0, a.size());
}

TEST_F(FlatMapTest, CopyConstructor)
{
	EXPECT_TRUE(a.insert("keyA", 65));
	EXPECT_TRUE(a.insert("keyB", 66));
	EXPECT_TRUE(a.insert("keyC", 13));

	FlatMap<std::string, int> c = a;
	EXPECT_TRUE(a == c);
	EXPECT_EQ(c.at("keyB"), 66);
}

TEST_F(FlatMapTest, CheckOperatorIndex)
{
	EXPECT_EQ(a["key1"], 0);
	EXPECT_EQ(a["key2"], 0);
	EXPECT_EQ(a["key3"], 0);

	std::string str = ".";
	for (int i = 0; i < 50; ++i)
	{
		EXPECT_EQ(a[str], 0);
		str += '.';
		EXPECT_EQ(i + 3 + 1, a.size());
	}

	a["key2"] = 23;
	EXPECT_EQ(a["key2"], 23);
	a.clear();
	EXPECT_EQ(a.size(), 0);
}

TEST_F(FlatMapTest, checkConstMap)
{
	EXPECT_TRUE(a.insert("test1", 1));
	EXPECT_TRUE(a.insert("test2", 2));
	const FlatMap<std::string, int> fMap = a;
	EXPECT_EQ(fMap.at("test1"), 1);
	EXPECT_EQ(fMap.at("test2"), 2);
}

int main(int argc, char *argv[])
{
	::testing::InitGoogleTest(&argc, argv);
	return RUN_ALL_TESTS();
}