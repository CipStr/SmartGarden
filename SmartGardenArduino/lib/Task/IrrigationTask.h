#ifndef __MAKINGTASK__
#define __MAKINGTASK__

#include "Task.h"
#include "ServoMotorImpl.h"
#include "Utils.h"
#include "IrrigationTask.h"
class MainTask;

class IrrigationTask: public Task {

  enum {IRRIGATING,SLEEP} state;

public:

  IrrigationTask();  
  void init(int period, ServoMotor* servo); 
  void tick();
  void addMainTask(MainTask* mainTask);
  void resetState(int temperature);
  void move();
  bool getIsAsleep();
  
private:
  ServoMotor* servo;
  CustomTimer timer;
  int position;
  bool isAsleep;
  int speed;
};

#endif
