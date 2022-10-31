#ifndef DIFFCOMMANDS_H
#define DIFFCOMMANDS_H

#include <string>

void newLine()
{
    std::cout << std::endl;
}

void dup()
{
    if (stack.empty()) throw std::runtime_error("Stack is empty!");
    stack.push(stack.top());
}

void over()
{
    if (stack.size() < 2) throw std::runtime_error("Stack is empty!");
    int tempVal = stack.top();
    stack.pop();
    int tempVal2 = stack.top();
    stack.push(tempVal);
    stack.push(tempVal2);
}

void drop()
{
    if (stack.empty()) throw std::runtime_error("Stack is empty!");
    stack.pop();
}

void swap()
{
    if (stack.size() < 2) throw std::runtime_error("Stack is empty!");
    int a = stack.top();
    stack.pop();
    int b = stack.top();
    stack.pop();

    stack.push(a);
    stack.push(b);
}

void createUserFunc()
{
    std::string value;
    value = GetCommand();
    std::string name = value;

    std::vector<std::string> commands;
    value = GetCommand();
    while (value != ";")
    {
        if (value == ":") throw std::runtime_error("Sorry, don't initialization func in func");
        // std::cerr << "[\033[34m DEBUG \033[0m] " << name << ": " << value << std::endl;
        commands.push_back(value);
        value = GetCommand();
    }

    forth.registerUserCommand(name, commands);
}

void ifCmd()
{
    if (stack.empty()) throw std::runtime_error("Stack is empty!");
    std::string value;
    int status = stack.top();
    signed ifCounter = 0;
    signed endifCounter = 0;
    stack.pop();
    if (status == -1)
    {
        value = GetCommand();
        std::string lowerValue = value;
        std::transform(lowerValue.begin(), lowerValue.end(), lowerValue.begin(), tolower);
        while(lowerValue != "else" && lowerValue != "then" && lowerValue != "endif")
        {
            forth.executeCommand(value);
            value = GetCommand();
            lowerValue = value;
            std::transform(lowerValue.begin(), lowerValue.end(), lowerValue.begin(), tolower);
        }
        if (lowerValue == "else")
        {
            ifCounter = 1;
            while (ifCounter != endifCounter)
            {
                while (lowerValue != "then" && lowerValue != "endif") 
                {
                    value = GetCommand();
                    lowerValue = value;
                    std::transform(lowerValue.begin(), lowerValue.end(), lowerValue.begin(), tolower);
                    if (lowerValue == "if") ifCounter++;
                }
                endifCounter++;
                if (ifCounter != endifCounter) 
                {
                    value = GetCommand();
                    lowerValue = value;
                    std::transform(lowerValue.begin(), lowerValue.end(), lowerValue.begin(), tolower);
                    if (lowerValue == "if") ifCounter++;
                }
            }
        }
    }
    else if (status == 0)
    {
        value = GetCommand();
        std::string lowerValue = value;
        std::transform(lowerValue.begin(), lowerValue.end(), lowerValue.begin(), tolower);
        do
        {
            while (lowerValue != "else" && lowerValue != "than" && lowerValue != "endif")
            {
                value = GetCommand();
                lowerValue = value;
                std::transform(lowerValue.begin(), lowerValue.end(), lowerValue.begin(), tolower);
                if (lowerValue == "if") ifCounter++;
            }
            if (lowerValue == "than" || lowerValue == "endif") endifCounter++;
        } while (ifCounter != endifCounter);
        if (lowerValue == "else")
        {
            value = GetCommand();
            lowerValue = value;
            std::transform(lowerValue.begin(), lowerValue.end(), lowerValue.begin(), tolower);
            while (lowerValue != "than" && lowerValue != "endif")
            {
                forth.executeCommand(value);
                value = GetCommand();
                lowerValue = value;
                std::transform(lowerValue.begin(), lowerValue.end(), lowerValue.begin(), tolower);
            }
        }
    }
    else
    {
        throw std::runtime_error("Invalid if condition");
    }
}

void doCmd()
{
    if (stack.size() < 2) throw std::runtime_error("Stack is empty!");
    int to = stack.top();
    stack.pop();
    int from = stack.top();
    stack.pop();

    std::vector<std::string> commands;
    commands.push_back("var");
    commands.push_back("i");
    commands.push_back("0");

    std::string value;
    std::string lowerValue;
    signed doCounter = 1;
    signed loopCounter = 0;
    while (doCounter != loopCounter)
    {
        do
        {
            value = GetCommand();
            commands.push_back(value);
            lowerValue = value;
            std::transform(lowerValue.begin(), lowerValue.end(), lowerValue.begin(), tolower);
            if (lowerValue == "do") doCounter++;
        } while (lowerValue != "loop");
        loopCounter++;
    }
    if (from < to)
    {
        for (int i = from; i < to; ++i)
        {
            commands.erase(commands.begin() + 2);
            commands.insert(commands.begin() + 2, std::to_string(i));
            for (int j = 0; j < commands.size(); ++j)
            {
                commander.insert(commander.begin() + (i - from)*commands.size() + j, commands[j]);
            }
        }
    }
    else
    {
        for (int i = from; i > to; --i)
        {
            commands.erase(commands.begin() + 2);
            commands.insert(commands.begin() + 2, std::to_string(i));
            for (int j = 0; j < commands.size(); ++j)
            {
                commander.insert(commander.begin() + (from - i)*commands.size() + j, commands[j]);
            }
        }
    }
}

void loopCmd()
{
    forth.userVariables.erase("i");
}

void createUserVar()
{
    std::string nameVar;
    nameVar = GetCommand();
    std::string value;
    value = GetCommand();
    forth.userVariables[nameVar] = stoi(value);
}


#endif