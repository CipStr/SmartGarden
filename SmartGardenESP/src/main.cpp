#include <Arduino.h>
#include "TemperatureSensor.h"
#include "Photoresistor.h"
#include <HTTPClient.h>
#include <WiFiClient.h>
TemperatureSensor* temperatureSensor;
Photoresistor* photoresistor;
const char *serviceURI = "http://192.168.1.66:12345/";
int pinLed = 17;
const char* ssid = "StricescuFamily";
const char* password = "bulanelciupitu";

void connectToWifi(const char* ssid, const char* password){
  WiFi.begin(ssid, password);
  Serial.println("Connecting");
  while(WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.print("Connected to WiFi network with IP Address: ");
  Serial.println(WiFi.localIP());
}

int sendData(String address, float temp, int light){  
   HTTPClient http;
   http.begin(address);      
   http.addHeader("Content-Type", "application/json");    
    
   String msg = 
    String("{ \"temp\": ") + String(temp) + 
    ", \"light\": \"" + String(light) +"\" }";
   
   int retCode = http.POST(msg);   
   http.end();  
   return retCode;
}

void setup() {
  Serial.begin(115200);
  temperatureSensor= new TemperatureSensor();
  photoresistor= new Photoresistor();
  photoresistor->init();
  pinMode(pinLed, OUTPUT);
  connectToWifi(ssid, password);
}
void loop() {
  int light = photoresistor->getValue();
  float temp = temperatureSensor->getTemperature();
  if (WiFi.status()== WL_CONNECTED){ 
    sendData(serviceURI,temp,light);
  }
}
