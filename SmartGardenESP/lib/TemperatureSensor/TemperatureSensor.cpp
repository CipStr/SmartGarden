#include "TemperatureSensor.h"
#include <Arduino.h>
#define ADC_VREF_mV    3300.0 // in millivolt
#define ADC_RESOLUTION 4096.0
#define PIN_LM35       18

TemperatureSensor::TemperatureSensor(){
}


double TemperatureSensor::getTemperature(){
  int adcVal = analogRead(PIN_LM35);
  // convert the ADC value to voltage in millivolt
  float milliVolt = adcVal * (ADC_VREF_mV / ADC_RESOLUTION);
  // convert the voltage to the temperature in °C
  float tempC = milliVolt / 10;
  return tempC;
}