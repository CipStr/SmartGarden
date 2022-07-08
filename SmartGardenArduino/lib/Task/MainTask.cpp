#include "MainTask.h"
#include "Utils.h"
#include <Arduino.h>
#include <math.h>
#include <ServoTimer2.h>
#define delta 9

IrrigationTask* taskIrrigation;

MainTask::MainTask(){    
  
}

void MainTask::init(int period,int pinLed1,int pinLed2,int pinLed3,int pinLed4){
  Task::init(period);
  this->pinLed1 = pinLed1;
  this->pinLed2 = pinLed2;
  this->pinLed3 = pinLed3;
  this->pinLed4 = pinLed4;
  pinMode(pinLed1, OUTPUT);
  pinMode(pinLed2, OUTPUT);
  pinMode(pinLed3, OUTPUT);
  pinMode(pinLed4, OUTPUT);
  this->brightness = 0;
  this->temperature = 0;
  state = AUTO; 
}

void MainTask::addIrrigationTask(IrrigationTask* irrigationtask){
  taskIrrigation = irrigationtask;
}

void MainTask::tick(){
    //Serial.println(state);
    switch(state){
        case AUTO:
            readData();
        break;
  }
}

void MainTask::readData(){
    //if not started star timer
    if(!timer.isStarted()){
        timer.startTimer();
    }
    //if timer expired
    if(timer.checkExpired(3000)){
        //get random value between 0 and 8
        brightness = rand() % 9;
        Serial.println(brightness);
        if(brightness<5){
            digitalWrite(pinLed1, HIGH);
            digitalWrite(pinLed2, HIGH);
            analogWrite(pinLed3, map(brightness,0,5,255,0));
            analogWrite(pinLed4, map(brightness,0,5,255,0));
            if(brightness<2){
                temperature = rand() % 5;
                if(!taskIrrigation->getIsAsleep()){
                    Serial.println("temp");
                    Serial.println(temperature);
                    Serial.println("speed");
                    taskIrrigation->resetState(temperature);
                }
            }
        }
        else{
            digitalWrite(pinLed1, LOW);
            digitalWrite(pinLed2, LOW);
            analogWrite(pinLed3, 0);
            analogWrite(pinLed4, 0);
        }
        timer.startTimer();
    }
}