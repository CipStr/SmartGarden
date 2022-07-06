#include <Arduino.h>
#include "ServoMotorImpl.h"
int pinLed1 = 10;
int pinLed2 = 11;
int pinLed3 = 12;
int pinLed4 = 13;
#define SERVO_PIN 9
ServoMotor* servo;
int position = 0;


void setup() {
  pinMode(pinLed1, OUTPUT);
  pinMode(pinLed2, OUTPUT);
  pinMode(pinLed3, OUTPUT);
  pinMode(pinLed4, OUTPUT);
  servo = new ServoMotorImpl(SERVO_PIN);
}

void loop() {
  servo->on();
  servo->setPosition(position);
  digitalWrite(pinLed1, HIGH);
  digitalWrite(pinLed2, HIGH);
  digitalWrite(pinLed3, HIGH);
  digitalWrite(pinLed4, HIGH);
  delay(1000);
  digitalWrite(pinLed1, LOW);
  digitalWrite(pinLed2, LOW);
  digitalWrite(pinLed3, LOW);
  digitalWrite(pinLed4, LOW);
  delay(1000);
  if(position<180){
    position=position+60;
  }else{
    position=0;
  }
}