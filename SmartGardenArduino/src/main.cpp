#include <Arduino.h>
#include "ServoMotorImpl.h"
#include "Scheduler.h"
#include "MainTask.h"
#include "IrrigationTask.h"
#define SERVO_PIN 9
int pinLed1 = 13;
int pinLed2 = 12;
int pinLed3 = 6;
int pinLed4 = 5;
ServoMotor* servo;
Scheduler scheduler;
int position = 0;


void setup() {
  Serial.begin(9600);
  scheduler.init(100);
  servo = new ServoMotorImpl(SERVO_PIN);
  MainTask* mainTask = new MainTask();
  mainTask->init(100,pinLed1,pinLed2,pinLed3,pinLed4);
  IrrigationTask* irrigationTask = new IrrigationTask();
  irrigationTask->init(100, servo);
  irrigationTask->addMainTask(mainTask);
  irrigationTask->setActive(false);
  mainTask->addIrrigationTask(irrigationTask);
  scheduler.addTask(mainTask);
  scheduler.addTask(irrigationTask);
}

void loop() {
  scheduler.schedule();
  //get which task is active
}