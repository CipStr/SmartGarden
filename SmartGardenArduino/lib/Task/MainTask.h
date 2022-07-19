#ifndef __MAINTASK__
#define __MAINTASK__

#include "Task.h"
#include "Utils.h"
#include "IrrigationTask.h"
class IrrigationTask;

class MainTask: public Task {
 
    enum{
        AUTO,
        MANUAL,
        ALARM
    } state;

  public:
    MainTask();
    void init(int period,int pinLed1,int pinLed2,int pinLed3,int pinLed4);
    void tick();
    void addIrrigationTask(IrrigationTask* irrigationtask);
    void readData();
    void getTemperature();
    void goManuel();

  private:
    CustomTimer timer;
    int position;
    int pinLed1;
    int pinLed2;
    int pinLed3;
    int pinLed4;
    int brightness;
    int temperature;
};
#endif
