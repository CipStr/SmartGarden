#include "IrrigationTask.h"
#include "Utils.h"
#include <Arduino.h>
#include <ServoTimer2.h>
#define delta 10

MainTask* taskMain;

IrrigationTask::IrrigationTask(){    
  
}

void IrrigationTask::init(int period, ServoMotor* servo){
  Task::init(period);
  position = 0;
  this->servo = servo;
  this->servo->on();
  this->servo->setPosition(0);
  this->isAsleep = false;
  this->speed = 0;
  state = IRRIGATING;    
}

void IrrigationTask::addMainTask(MainTask* mainTask){
  taskMain = mainTask;
}

void IrrigationTask::tick(){
    switch(state){
        case IRRIGATING:
          isAsleep = true;
          if(!timer.isStarted()){
            timer.startTimer();
          }
          move();
          if(timer.checkExpired(5000)){
            state=SLEEP;
            isAsleep=true;
            timer.startTimer();
          }
        break;
        case SLEEP:
            if(timer.checkExpired(10000)){
                isAsleep = false;
                this->servo->setPosition(0);
                this->setActive(false);
            }
        break;
  }
}

void IrrigationTask::resetState(int temperature){
  this->setActive(true);
  position = 0;
  isAsleep = false;
  state = IRRIGATING;
  timer.startTimer();
  speed=temperature*5;
}

void IrrigationTask::move(){
  //move servo until it reaches the end then make it go back to the beginning
  if(position<180){
    servo->setPosition(position);
    position+=delta+speed;
  }
  else{
    servo->setPosition(0);
    position = 0;
  }
}

bool IrrigationTask::getIsAsleep(){
  return isAsleep;
}
