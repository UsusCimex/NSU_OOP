#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define MAX_INPUT_SIZE 1500
#define MAX_STACK_SIZE 1000

void BadInput() {
    printf("syntax error");
    exit(0);
}

void DivByZero() {
    printf("division by zero");
    exit(0);
}

typedef struct TStack{
    int Val[MAX_STACK_SIZE];
    int Pos;
} TStack;

int IsEmpty(TStack *stack) {
    return stack->Pos < 0;
}

int PopStack(TStack *stack) {
    if (IsEmpty(stack)) BadInput();
    return stack->Val[stack->Pos--];
}

void PushStack(TStack *stack, int val) {
    stack->Pos++;
    stack->Val[stack->Pos] = val;
}

int Peek(TStack *stack) {
    if (IsEmpty(stack)) BadInput();
    return stack->Val[stack->Pos];
}

int IsOperation(char ch) {
    return ch == '+' || ch == '-' || ch == '/' || ch == '*';
}

int IsNumber(char ch) {
    return ch >= '0' && ch <= '9';
}

void Calculate(TStack *nums, char op) {
    int num2 = PopStack(nums);
    int num1 = PopStack(nums);

    if(op == '+') {
        PushStack(nums, num1 + num2);
    } else if(op == '-') {
        PushStack(nums, num1 - num2);
    } else if(op == '*') {
        PushStack(nums, num1 * num2);
    } else if(op == '/') {
        if(!num2) DivByZero();
        PushStack(nums, num1 / num2);
    } else BadInput();
}

int GetNum(char *s, int* pos, int len) {
    int num = 0;
    while (*pos < len && IsNumber(s[*pos])) {
        num = num * 10 + s[*pos] - '0';
        (*pos)++;
    }
    (*pos)--;
    return num;
}

int StartCalculate(char *s, int len) {
    TStack nums; nums.Pos = -1;
    TStack ops; ops.Pos = -1;

    int *priority = calloc(256, sizeof(char));
    priority['*'] = 2;
    priority['/'] = 2;
    priority['+'] = 1;
    priority['-'] = 1;

    for (int i = 0; i < len; ++i) {
        if(s[i] == '(') PushStack(&ops, '(');
        else if(s[i] == ')') {
            if(!i || s[i-1] == '(') BadInput();
            char tmp;
            while ((tmp = (char)PopStack(&ops)) != '(') Calculate(&nums, tmp);
        }
        else if(IsOperation(s[i])) {
            while(!IsEmpty(&ops) && priority[Peek(&ops)] >= priority[(int)s[i]]) 
                Calculate(&nums, (char)PopStack(&ops));
            PushStack(&ops,(int)s[i]);
        }
        else if(IsNumber(s[i])) {
            int num = GetNum(s, &i, len);
            PushStack(&nums, num);
        }
    }
    while(!IsEmpty(&ops)) {
        Calculate(&nums, (char)PopStack(&ops));
    }
    free(priority);
    return PopStack(&nums);
}

void CheckMath(char* s, int len) {
    for(int i = 0; i < len; i++) {
        if(!IsOperation(s[i]) && !IsNumber(s[i]) && s[i] != '(' && s[i] != ')') {
            BadInput();
        }
    }
}

int main() {
    FILE* fin = fopen("in.txt", "r");
    char math[MAX_INPUT_SIZE];
    if (fgets(math, MAX_INPUT_SIZE, fin) == 0) exit(0);
    int len = strlen(math);
    math[--len] = 0;

    CheckMath(math, len);
    int result = StartCalculate(math, len);
    printf("%d", result);
    fclose(fin);
    return 0;
}
