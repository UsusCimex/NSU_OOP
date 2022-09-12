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
    PrintArray(myArr);
    myArr.insert(2, 104);
    PrintArray(myArr);
    myArr.pop_value(22, 1);
    PrintArray(myArr);

    return 0;
}