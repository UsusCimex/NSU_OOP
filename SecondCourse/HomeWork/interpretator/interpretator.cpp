#include "interpretator.h"
#include "forthCommands/diff.h"
#include "forthCommands/math.h"

std::string GetCommand()
{
    std::string value;
    if (commander.empty()) 
    {
        std::cin >> value;
    }
    else
    {
        value = commander.front();
        commander.erase(commander.begin());
    }
    return value;
}

void Forth::start()
{
    defCommands();
    bool status = true;
    std::string value;
    while (status)
    {
        value = GetCommand();
        commander.push_back(value);
        executeCommands();
        if (std::cin.eof()) status = false;
    }
}

bool Forth::checkUserVariable(std::string value)
{
    return userVariables.count(value) != 0;
}

bool Forth::checkForthCommand(std::string value)
{
    std::transform(value.begin(), value.end(), value.begin(), tolower);
    return forthCommands.count(value) != 0;
}

bool Forth::checkUserCommand(std::string value)
{
    return userCommands.count(value) != 0;
}

bool checkNum(std::string value)
{
    for (auto nm : "0123456789-")
    {
        if (value[0] == nm)
        {
            return 1;
        }
    }
    return 0;
}

bool dotCommand(std::string cmd)
{
    if (cmd == ".")
    {
        if (stack.empty()) throw std::runtime_error("Stack is empty");
        std::cout << stack.top() << std::endl;
        stack.pop();
        return 1;
    }
    if (cmd.compare(0, 2, ".\"") == 0)
    {
        cmd = cmd.substr(2, cmd.length() - 2);
        while (cmd.back() != '\"')
        {
            std::cout << cmd << " ";
            if (commander.empty()) 
            {
                std::cin >> cmd;
            }
            else 
            {
                cmd = GetCommand();
            }
        }
        cmd = cmd.substr(0, cmd.length() - 1);
        std::cout << cmd << std::endl;
        return 1;
    }
    if (cmd == ".S" || cmd == ".s")
    {
        if (stack.empty()) throw std::runtime_error("Stack is empty");
        while (!stack.empty())
        {
            std::cout << stack.top() << " ";
            stack.pop();
        }
        std::cout << std::endl;
        return 1;
    }

    return 0;
}

void Forth::executeCommand(std::string cmd)
{
    if (!dotCommand(cmd))
    {
        if (checkForthCommand(cmd))
        {
            std::transform(cmd.begin(), cmd.end(), cmd.begin(), tolower);
            forthCommands[cmd]();
        }
        else if (checkUserCommand(cmd))
        {
            std::vector<std::string> usrCommands = userCommands[cmd];
            for (int i = 0; i < usrCommands.size(); ++i)
            {
                commander.emplace(commander.begin() + i, usrCommands[i]);
            }
        }
        else if (checkUserVariable(cmd))
        {
            stack.push(userVariables[cmd]);
        }
        else if (checkNum(cmd))
        {
            stack.push(std::stoi(cmd));
        }
        else
        {
            std::cerr << "Command : " << cmd << " not found..." << std::endl;
            throw std::runtime_error("Command not found...");
        }
    }
}

void Forth::executeCommands()
{
    while (!commander.empty())
    {
        std::string cmd = GetCommand();
        executeCommand(cmd);
    }
}

void Forth::defCommands()
{
    registerCommand("+", sum);
    registerCommand("-", sub);
    registerCommand("*", mul);
    registerCommand("/", div);
    registerCommand("dup", dup);
    registerCommand("swap", swap);
    registerCommand(":", createUserFunc);
    registerCommand("var", createUserVar);
    registerCommand("variable", createUserVar);
    registerCommand("=", equal);
    registerCommand("<", less);
    registerCommand(">", more);
    registerCommand("if", ifCmd);
    registerCommand("do", doloopCmd);
}

void Forth::registerCommand(std::string command, void (*def)())
{
    forthCommands[command] = def;
}

void Forth::registerUserCommand(std::string command, std::vector<std::string> commands)
{
    userCommands[command] = commands;
}