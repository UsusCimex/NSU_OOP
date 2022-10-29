#ifndef INTERPRETATOR_H
#define INTERPRETATOR_H

#include <iostream>
#include <map>
#include <vector>
#include <string>
#include <stack>
#include <algorithm>

std::string GetCommand();

class Forth
{
public:
    void start();

    void executeCommands();
    void executeCommand(std::string);
    bool checkUserCommand(std::string);
    bool checkUserVariable(std::string);
    bool checkForthCommand(std::string);
    void getUserCommands();
    void registerUserCommand(std::string, std::vector<std::string>);

    std::map<std::string, int> userVariables;
private:
    void defCommands();
    void registerCommand(std::string, void (*)());

    std::map<std::string, std::vector<std::string>> userCommands;
    std::map<std::string, void (*)()> forthCommands;
};

extern Forth forth;
extern std::stack<int> stack;
extern std::vector<std::string> commander;
#endif