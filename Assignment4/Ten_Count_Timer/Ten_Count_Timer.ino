// Demo program for testing library and board - flip the switch to turn on/off buzzer

#include <Adafruit_CircuitPlayground.h>

// we light one pixel at a time, this is our counter
int pixeln = 0;
# define SHAKE_THRESHOLD 30 // Total acceleration threshold for shake detect

// State 0: Waiting to start timer
// State 1: Running timer
// State 2: Timer finished
uint32_t state = 0;

void setup() {
  //while (!Serial);
  Serial.begin(9600);
  delay(100);
  Serial.println("Press the right button to start the 10 second timer.");
  state = 0;

  CircuitPlayground.begin();
  CircuitPlayground.setAccelRange(LIS3DH_RANGE_8_G);
}

float getTotalAccel() {
  // Compute total acceleration
  float X = 0;
  float Y = 0;
  float Z = 0;
  for (int i = 0; i < 10; i++)
  {
    X += CircuitPlayground.motionX();
    Y += CircuitPlayground.motionY();
    Z += CircuitPlayground.motionZ();
    delay(1);
  }

  X /= 10;
  Y /= 10;
  Z /= 10;

  return sqrt(X * X + Y * Y + Z * Z);
}


void loop() {

  // Wait for the right button to start.
  if (state == 0)
  {
    pixeln = 0;
    // Advance to timer state
    if (CircuitPlayground.rightButton())
    {
      Serial.println("Starting the timer by button.");
      state = 1;
      CircuitPlayground.clearPixels();
    }
    else if (getTotalAccel() > SHAKE_THRESHOLD)
    {
      Serial.println("Starting the timer by shaking.");
      state = 1;
      CircuitPlayground.clearPixels();
    }
    else
    {
      delay(100);
    }
  }
  // Run the timer
  else if (state == 1)
  {
    delay(1000);
    //light one led per second

    Serial.println("Advanced timer by 1 second");

    CircuitPlayground.setPixelColor(pixeln, CircuitPlayground.colorWheel(25 * pixeln));
    pixeln++;

    if (pixeln == 10) {
      state = 2;
    }
  }
  // Blink the LEDs twice
  else
  {
    Serial.println("Timer Expired.");
    // Blink 1
    CircuitPlayground.clearPixels();
    delay(100);
    for (int i = 0; i < 10; ++i)
    {
      CircuitPlayground.setPixelColor(i, CircuitPlayground.colorWheel(25 * i));
    }
    delay(100);

    // Blink 2;
    CircuitPlayground.clearPixels();
    delay(100);
    for (int i = 0; i < 10; ++i)
    {
      CircuitPlayground.setPixelColor(i, CircuitPlayground.colorWheel(25 * i));
    }
    delay(100);

    // Turn them all off.
    CircuitPlayground.clearPixels();
    state = 0;
  }

  //delay(1000);
}
