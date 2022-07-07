#ifndef UTILS_H
#define UTILS_H
#include <string.h>

class CustomTimer{
    private:
       unsigned long initialTime;
       public:
       CustomTimer(); //constructor
       void startTimer();
       bool isStarted();
       bool checkExpired(unsigned long t);
};
#endif