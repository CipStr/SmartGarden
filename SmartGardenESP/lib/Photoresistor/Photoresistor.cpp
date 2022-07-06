#include "Photoresistor.h"
#include <Arduino.h>
#define pinPhotoResistor 7

Photoresistor::Photoresistor(){
}

void Photoresistor::init(){
    pinMode(pinPhotoResistor, INPUT);
}

int Photoresistor::getValue(){
  int value=analogRead(pinPhotoResistor);
  //map value in [0,1023] to [0,8]
  int mapValue=map(value,0,4095,0,8);
  return mapValue;
}