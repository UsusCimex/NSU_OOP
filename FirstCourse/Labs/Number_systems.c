#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <ctype.h>
#define drobConst 16

int convertCharToInt(char s)
{
    if (s=='.' || s=='\n' || s=='\0') return -1;
    s = toupper(s);
    if (s >= 'A' && s <= 'F') return s - 'A' + 10;
    else if (s >= '0' && s <= '9') return s - '0';
    return -2;
}
char convertIntToChar(int i)
{
    if (i>9) return i + 'A' - 10;
    else return (i+'0');
}

int main(void)
{
    int a,b; 
    if( scanf("%d %d", &a, &b) != 2 ) exit(0);
    if (a<2 || b<2 || a>16 || b>16) {printf("Bad input"); return 0;}

    char num[51];
    for (int i = 0; i < 50; i++) num[i]='\n';
    
    if( scanf("%49s", num) != 1 ) exit(0);

    int beforeDot=0, afterDot=0;
    _Bool isDot=1;
    int dotCount = 0;
    if (num[0]=='.') {printf("Bad input"); return 0;}
    for (int i = 0; i < 49; i++) //Расположения чисел относительно точек, +проверки на ошибки
    {
        if (convertCharToInt(num[i])>=a || convertCharToInt(num[i])==-2) {printf("Bad input"); return 0;}
        if ((num[i]=='.' && num[i + 1]=='\0')) {printf("Bad input"); return 0;}
        if (num[i]=='.') dotCount++;
        if (dotCount > 1) {printf("Bad input"); return 0;}

        if (num[i+1] == '\n') break;
        if (num[i] == '.') {isDot = 0; continue;}
        if (isDot) beforeDot++;
        else afterDot++;
    }
    long long celA=0;
    long long drobA=0;
    isDot=1;

    for(int i = 0; i < 50; ++i) {
        if (num[i+1] == '\n') break;
        if (num[i] == '.') {isDot = 0; continue;}
        if (isDot) celA = celA * a + convertCharToInt(num[i]);
        else drobA = drobA * a + convertCharToInt(num[i]);
    }

    drobA = drobA / pow(a,afterDot) * pow(10,drobConst);

    _Bool isCel = celA==0;
    char celB[51], drobB[51];
    for (int i = 0; i < 50; i++)
    {
        celB[i] = '\n';
        drobB[i] = '\n';
    }
    int temp = 2;
    while (celA > 0) //перевод целой части в заданную сс
    {
        celB[50 - temp]=convertIntToChar(celA % b);
        celA /= b;
        temp++;
    }

    temp = 0;
    _Bool isDrob = drobA != 0;
    if (isDrob) //перевод дробной части в заданню сс
    {
        while (drobA > 0 && temp < 12)
        {
            drobA *= b;
            drobB[temp]=convertIntToChar(drobA / pow(10,drobConst));
            drobA %= (long long int)pow(10,drobConst);
            temp++;
        }
    }

    char answ[51]; //Собираем всё в единый ответ
    answ[0] = '0';
    for (int i = 1; i < 50; i++) answ[i] = 0;
    int count = 0;
    for (int i = 0; i < 50; i++)
    {
        if (celB[i] != '\n')
        {
            answ[count] = celB[i];
            count++;
        }
    }
    if (isDrob)
    {
        if (isCel == 1) {answ[0] = '0'; count++;}
        answ[count] = '.';
        count++;
        for (int i = 0; i < 50; i++)
        {
            if (drobB[i] != '\n')
            {
                answ[count] = drobB[i];
                count++;
            }
        }
    }
    printf("%s", answ);
    return EXIT_SUCCESS;
}
