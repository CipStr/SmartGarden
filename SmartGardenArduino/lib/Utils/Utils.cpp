#include "Utils.h"
#include <Arduino.h>

CustomTimer::CustomTimer() {
  initialTime = 0;
}
void CustomTimer::startTimer() {
  initialTime = millis();
}
bool CustomTimer::isStarted() {
  return initialTime != 0;
}
bool CustomTimer::checkExpired(unsigned long t) {
  if(millis() - initialTime >= t) {
    return true;
  }
  return false;
}