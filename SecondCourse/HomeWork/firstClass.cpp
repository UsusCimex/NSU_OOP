#include <iostream>
#include <cstdlib>
#include "myClasses.h"

using namespace std;
using namespace myCls;

void PrintArray(Array arr) {
    for (int i = 0; i < arr.size(); ++i) {
        cout << arr.at(i) << " ";
    }
    cout << endl;
}

int main() {
    Array myArr(5);

    myArr.push_back(32);
    myArr.push_back(22);
    myArr.push_back(22);
    myArr.push_back(21);
    myArr.push_back(40);

    myArr.sort();
    myArr.push_back(25);
    myArr.sort();

    cout << myArr.pop_back() << endl;
    cout << myArr.pop_back() << endl;

    myArr.push_back(39);
    myArr.sort();

    PrintArray(myArr);
    auto el = myArr.find(21);
    myArr.pop(el);
    PrintArray(myArr);
    myArr.pop(22);
    PrintArray(myArr);

    myArr.push_back(204);
    myArr.sort();

    PrintArray(myArr);
    return 0;
}