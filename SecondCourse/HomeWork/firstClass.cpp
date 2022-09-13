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
    Array arr;
    arr.push_back(2);
    arr.push_back(5);
    arr.push_back(3);
    arr.push_back(14);

    PrintArray(arr);

    Array b = arr;
    PrintArray(b);
    b = b;
    PrintArray(b);

    return 0;
}