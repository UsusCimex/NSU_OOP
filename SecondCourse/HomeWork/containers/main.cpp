#include <iostream>
#include <cstdlib>
#include "FlatMap.h"

using namespace std;

int main() {
    FlatMap<string, int> myFM;
    myFM["help"] = 24;
    myFM["hello"] = 12;
    myFM["pirivet"] = 33;

    cout << myFM.at("hello") << endl;
    cout << myFM.at("pirivet") << endl;
    // cout << myFM.at("pirivit") << endl; //throw

    myFM["hello"] = 22;
    cout << myFM.at("hello") << endl;

    if (myFM.erase("hello")) cout << "del" << endl;
    else cout << "error erase" << endl;

    // cout << myFM.at("hello") << endl; //throw

    myFM.insert("foo", 100);
    cout << myFM.at("foo") << endl;

    return 0;
}