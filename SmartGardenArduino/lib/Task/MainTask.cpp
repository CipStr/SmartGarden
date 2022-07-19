#include "MainTask.h"
#include "Utils.h"
#include <Arduino.h>
#include <math.h>
#include <ServoTimer2.h>
#include "SoftwareSerial.h"
/*
 *  BT module connection:  
 *  - pin 2 <=> TXD
 *  - pin 3 <=> RXD
 *
 */ 
#define delta 9
SoftwareSerial btChannel(2, 3);
IrrigationTask* taskIrrigation;

MainTask::MainTask(){    
  
}

void MainTask::init(int period,int pinLed1,int pinLed2,int pinLed3,int pinLed4){
  btChannel.begin(9600);
  Task::init(period);
  this->pinLed1 = pinLed1;
  this->pinLed2 = pinLed2;
  this->pinLed3 = pinLed3;
  this->pinLed4 = pinLed4;
  pinMode(pinLed1, OUTPUT);
  pinMode(pinLed2, OUTPUT);
  pinMode(pinLed3, OUTPUT);
  pinMode(pinLed4, OUTPUT);
  this->brightness = 8;
  this->temperature = 0;
  //btChannel.print("AT+NAMEarduinoGarden"); // Set the name to arduinoGarden
  state = AUTO; 
}

void MainTask::addIrrigationTask(IrrigationTask* irrigationtask){
  taskIrrigation = irrigationtask;
}

void MainTask::tick(){
    switch(state){
        case AUTO:
            if(btChannel.available()){
                 int c = btChannel.read();
                 Serial.write(c);
                 if(c == 77){
                    goManuel();
                 }
            }
            readData();
        break;
        case MANUAL:
            Serial.println("SOY MANUEL");
        break;
  }
}

void MainTask::readData(){
    //if timer is not running, start it
    if(!timer.isStarted()){
        timer.startTimer();
    }
    if(Serial.available()>0 && timer.checkExpired(3000)){
        String msg = Serial.readString();
        //find ',', split string and get temperature
        int index = msg.indexOf(',');
        String temp = msg.substring(0,index);
        temperature = temp.toFloat();
        //map temperature in a range between 0 and 4
        temperature = map(temperature,0,40,0,4);
        Serial.println(temperature);
        int secondIndex = msg.indexOf(':');
        String light = msg.substring(index+1,secondIndex);
        Serial.println(light);
        brightness = light.toInt();
        if(brightness<5){
            digitalWrite(pinLed1, HIGH);
            digitalWrite(pinLed2, HIGH);
            analogWrite(pinLed3, map(brightness,0,5,255,0));
            analogWrite(pinLed4, map(brightness,0,5,255,0));
            if(brightness<=2){
                if(!taskIrrigation->getIsAsleep()){
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
    else{
        Serial.read();
    }
}
void MainTask::goManuel(){
    state = MANUAL;
}
