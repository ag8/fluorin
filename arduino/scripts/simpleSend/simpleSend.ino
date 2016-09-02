/* Include the servo library */
#include <Servo.h>

const int ledPin = 2;      // the pin that the LED is attached to

// The serial byte that will be passed from the app
byte serialA;


// Default servo values
int dir = 0;
int pos = 1;
int del = 1;

Servo myservo;  // create servo object to control a servo

Servo a1;

void setup() {
  // initialize the serial communication:
  Serial.begin(9600); //baud rate - make sure it matches that of the module you got. Should be 9600.

  // initialize the ledPin as an output:
  pinMode(ledPin, OUTPUT);

  // attach the servo to the pin:
  myservo.attach(10);  // attaches the servo on pin 9 to the servo object
}

void loop() {
  // Move the servo, as needed
  if (dir == 1) {
    digitalWrite(ledPin, HIGH);

    if (pos < 179) {
      pos++;
    }

    if (pos != 179) {
      myservo.write(pos);
    } else {
      dir = 0;
      digitalWrite(ledPin, LOW);
    }
  } else if (dir == -1) {
    digitalWrite(ledPin, HIGH);

    if (pos > 10) {
      pos--;
    }

    if (pos != 10) {
      myservo.write(pos);
    } else {
      dir = 0;
      digitalWrite(ledPin, LOW);
    }
  } else {
    digitalWrite(ledPin, LOW);
  }


  // Get the serial data from the app
  if (Serial.available() > 0) {
    serialA = Serial.read();
    Serial.println(serialA);
  }


  // Update movements based on serial data
  switch (serialA) {
    case 0:
      dir = 1;
      break;
    case 1:
      dir = -1;
      break;
    case 2:
      dir = 0;
      break;
    default:
      //Serial.println("Unrecognized");
      break;
  }

  delay(5);
}
