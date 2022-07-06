#include <Arduino.h>
#include "TemperatureSensor.h"
#include "Photoresistor.h"
TemperatureSensor* temperatureSensor;
Photoresistor* photoresistor;
int pinLed = 17;

void setup() {
  Serial.begin(115200);
  temperatureSensor= new TemperatureSensor();
  photoresistor= new Photoresistor();
  photoresistor->init();
  pinMode(pinLed, OUTPUT);
}
void loop() {
  if(photoresistor->getValue()>5){
    digitalWrite(pinLed, HIGH);
  }else{
    digitalWrite(pinLed, LOW);
  }
  Serial.println("Temperature: " + String(temperatureSensor->getTemperature()) + " °C");
  //delay(5000);
}