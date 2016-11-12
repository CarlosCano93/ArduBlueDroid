/**
* Prepare Arduino uno with next connections:
* HC-06/HC-05 -> Arduino Uno
*  +5V/VCC    ->    5V
*    GND      ->    GND
*    TX       ->  RX(pwm 0)
*
* Led in digital pwm 5 and GND
*/

char command;
String string;
boolean ledon = false;
int led1 = 5;

  void setup(){
    Serial.begin(9600);
    pinMode(led1, OUTPUT);
  }

  void loop(){
    if (Serial.available() > 0) {
      string = "";
    }


    // the message recibed from android is sended char by char
    while(Serial.available() > 0) {
      command = ((byte)Serial.read()); // read from bluetooth (or MonitorSerie)
      
      if(command == ':'){ // indicate the end of the message
        break;
      } else {
        string += command; // put message together
      }
      
      delay(1); // to prevent bugs
    }

    // actions to do when message is correct
    if(string == "L1"){ 
        ledOn(led1);
    }
    
    if(string =="L2"){
        ledOff(led1);
    }
 }
 
void ledOn(int led){
      digitalWrite(led, HIGH);
      delay(10);
    }
 
 void ledOff(int led){
      digitalWrite(led, LOW);
      delay(10);
 }
 

    

