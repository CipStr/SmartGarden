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
int manualValues[] = {0,0,0,0,0,0};
int manualPosition = 0;

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
        {
            if(btChannel.available()){
                 int c = btChannel.read();
                 if(c == 77){
                    Serial.println("Going manual");
                    btChannel.flush();
                    goManuel();
                 }
            }
            readData();
            break;
        }
        case MANUAL:
        {
            //Parse each byte of the message
            int c = btChannel.read();
            //Serial.println(c);
            if(c >= 48){
                writeManual(c);
                for(int i = 0; i < 6; i++){
                    
                        Serial.print(manualValues[i]);
                        Serial.print(" ");
                    
                }
            }   
            if( c == 66){
                Serial.println("Going auto");
                btChannel.flush();
                state=AUTO;  
            }
            break;
        }
        case ALARM:
            int r = btChannel.read();
            if(r == 75){
                Serial.println("ALARM OFF");
                btChannel.flush();
                state = AUTO;
            }
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
        if(temperature >= 4 && taskIrrigation->getIsAsleep()){
            state = ALARM;
            Serial.println("ALARM ON");
            btChannel.write(65);
        }
        else{
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
       
    } 
    else{
        Serial.read();
    }
}
void MainTask::goManuel(){
    state = MANUAL;
}
void MainTask::writeManual(int c){
    if(manualPosition>5){
        manualPosition = 0;
        
    }
    manualValues[manualPosition] = c;
    manualPosition++;
    if(manualPosition == 6){
        setManual();
    }
}

void MainTask::setManual(){
    if(manualValues[0] == 49){
        digitalWrite(pinLed1, HIGH);
    }
    else{
        digitalWrite(pinLed1, LOW);
    }
    if(manualValues[1] == 49){
        digitalWrite(pinLed2, HIGH);
    }
    else{
        digitalWrite(pinLed2, LOW);
    }
    int c = manualValues[2]-48;
    analogWrite(pinLed3, map(c,0,5,255,0));
    c= manualValues[3]-48;
    analogWrite(pinLed4, map(c,0,5,255,0));
    if(manualValues[4] == 49){
        if(!taskIrrigation->getIsAsleep()){
            taskIrrigation->resetState(manualValues[5]-48);
        }
    }
}
