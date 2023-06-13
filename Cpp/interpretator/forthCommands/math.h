#ifndef MATHCOMMANDS_H
#define MATHCOMMANDS_H

void sum()
{
    if (stack.size() < 2) throw std::runtime_error("Stack is empty!");
    else
    {
        int a = stack.top();
        stack.pop();
        int b = stack.top();
        stack.pop();
        stack.push(a + b);
    }
}

void sub()
{
    if (stack.size() < 2) throw std::runtime_error("Stack is empty!");
    else
    {
        int a = stack.top();
        stack.pop();
        int b = stack.top();
        stack.pop();
        stack.push(b - a);
    }
}

void mul()
{
    if (stack.size() < 2) throw std::runtime_error("Stack is empty!");
    else
    {
        int a = stack.top();
        stack.pop();
        int b = stack.top();
        stack.pop();
        stack.push(a * b);
    }
}

void div()
{
    if (stack.size() < 2) throw std::runtime_error("Stack is empty!");
    else
    {
        int a = stack.top();
        stack.pop();
        int b = stack.top();
        stack.pop();
        stack.push(b / a);
    }
}

void equal()
{
    if (stack.size() < 2) throw std::runtime_error("Stack is empty!");
    int a = stack.top();
    stack.pop();
    int b = stack.top();
    stack.pop();
    if (a == b) stack.push(-1);
    else stack.push(0);
}

void less()
{
    if (stack.size() < 2) throw std::runtime_error("Stack is empty!");
    int a = stack.top();
    stack.pop();
    int b = stack.top();
    stack.pop();
    if (b < a) stack.push(-1);
    else stack.push(0);
}

void more()
{
    if (stack.size() < 2) throw std::runtime_error("Stack is empty!");
    int a = stack.top();
    stack.pop();
    int b = stack.top();
    stack.pop();
    if (b > a) stack.push(-1);
    else stack.push(0);
}
#endif