/*********************************************************************
  * Laura Arjona. PMPEE590
  * Example of simple interaction beteween Adafruit Circuit Playground
  * and Android App. Communication with BLE - uart
*********************************************************************/
#include <Arduino.h>
#include <SPI.h>
#include "Adafruit_BLE.h"
#include "Adafruit_BluefruitLE_SPI.h"
#include "Adafruit_BluefruitLE_UART.h"
#include <Adafruit_CircuitPlayground.h>

#include "BluefruitConfig.h"

#if SOFTWARE_SERIAL_AVAILABLE
  #include <SoftwareSerial.h>
#endif


// Strings to compare incoming BLE messages
String start_timer = "RUNTIMER";
String temp = "TEMP";
String timer = "TIMER";
String start_color = "COLOR1";
String end_color = "COLOR2";
String buzz = "BUZZ";

int sensorTemp = 0;

int timer_length = 10;

int start_red = 0;
int start_green = 0;
int start_blue = 0;

int end_red = 0;
int end_green = 0;
int end_blue = 0;

bool buzz_at_end = false;

/*=========================================================================
    APPLICATION SETTINGS
    -----------------------------------------------------------------------*/
    #define FACTORYRESET_ENABLE         0
    #define MINIMUM_FIRMWARE_VERSION    "0.6.6"
    #define MODE_LED_BEHAVIOUR          "MODE"
/*=========================================================================*/

// Create the bluefruit object, either software serial...uncomment these lines

Adafruit_BluefruitLE_UART ble(BLUEFRUIT_HWSERIAL_NAME, BLUEFRUIT_UART_MODE_PIN);

/* ...hardware SPI, using SCK/MOSI/MISO hardware SPI pins and then user selected CS/IRQ/RST */
// Adafruit_BluefruitLE_SPI ble(BLUEFRUIT_SPI_CS, BLUEFRUIT_SPI_IRQ, BLUEFRUIT_SPI_RST);

/* ...software SPI, using SCK/MOSI/MISO user-defined SPI pins and then user selected CS/IRQ/RST */
// Adafruit_BluefruitLE_SPI ble(BLUEFRUIT_SPI_SCK, BLUEFRUIT_SPI_MISO,
//                             BLUEFRUIT_SPI_MOSI, BLUEFRUIT_SPI_CS,
//                             BLUEFRUIT_SPI_IRQ, BLUEFRUIT_SPI_RST);


// A small helper
void error(const __FlashStringHelper*err) {
  Serial.println(err);
  while (1);
}

/**************************************************************************/
/*!
    @brief  Sets up the HW an the BLE module (this function is called
            automatically on startup)
*/
/**************************************************************************/
void setup(void)
{
  CircuitPlayground.begin();
  

  Serial.begin(115200);
  Serial.println(F("Adafruit Bluefruit Command <-> Data Mode Example"));
  Serial.println(F("------------------------------------------------"));

  /* Initialise the module */
  Serial.print(F("Initialising the Bluefruit LE module: "));

  if ( !ble.begin(VERBOSE_MODE) )
  {
    error(F("Couldn't find Bluefruit, make sure it's in CoMmanD mode & check wiring?"));
  }
  Serial.println( F("OK!") );

  if ( FACTORYRESET_ENABLE )
  {
    /* Perform a factory reset to make sure everything is in a known state */
    Serial.println(F("Performing a factory reset: "));
    if ( ! ble.factoryReset() ){
      error(F("Couldn't factory reset"));
    }
  }

  /* Disable command echo from Bluefruit */
  ble.echo(false);

  Serial.println("Requesting Bluefruit info:");
  /* Print Bluefruit information */
  ble.info();
  
  ble.println("AT+GAPDEVNAME");
  // Check response status
  ble.waitForOK();

  Serial.println(F("Please use Adafruit Bluefruit LE app to connect in UART mode"));
  Serial.println(F("Then Enter characters to send to Bluefruit"));
  Serial.println();

  ble.verbose(false);  // debug info is a little annoying after this point!

  /* Wait for connection */
  while (! ble.isConnected()) {
      delay(500);
  }

  Serial.println(F("******************************"));

  // LED Activity command is only supported from 0.6.6
  if ( ble.isVersionAtLeast(MINIMUM_FIRMWARE_VERSION) )
  {
    // Change Mode LED Activity
    Serial.println(F("Change LED activity to " MODE_LED_BEHAVIOUR));
    ble.sendCommandCheckOK("AT+HWModeLED=" MODE_LED_BEHAVIOUR);
  }

  // Set module to DATA mode
  Serial.println( F("Switching to DATA mode!") );
  ble.setMode(BLUEFRUIT_MODE_DATA);

  Serial.println(F("******************************"));
 
  delay(1000);
}
/**************************************************************************/
/*!
    @brief  Constantly poll for new command or response data
*/
/**************************************************************************/
void loop(void)
{
  // Save received data to string
  String recievedText = "";
  while ( ble.available() )
  {
    int c = ble.read();
    //Serial.print((char)c);
    recievedText += (char)c;
    delay(50);
  }

  if(recievedText.length() >= 1)
  {
    Serial.println(recievedText);
    delay(100);
  }

  if(recievedText.startsWith(start_timer)){
    for(int i = 0; i < 10; ++i)
    {
      float endBias = i/9.0;
      float startBias = 1.0 - startBias;
      
      float red = (start_red * startBias) + (end_red * endBias);
      float green = (start_green * startBias) + (end_green * endBias);
      float blue = (start_blue * startBias) + (end_blue * endBias);
      
      CircuitPlayground.setPixelColor(i, (char)red, (char)green, (char)blue);
    }

    ble.print("OK");
  }
  else if(recievedText.startsWith(temp)){
    sensorTemp = CircuitPlayground.temperature(); // returns a floating point number in Centigrade
    delay(10);

   //Send data to Android Device
    char output[8];
    String data = temp + " ";
    data += sensorTemp;
    Serial.println(data);
    data.toCharArray(output,8);
    ble.print(data);
  }
  else if(recievedText.startsWith(timer)){
    String data = recievedText;

    // Get rid of the tag that we don't care about
    data = data.substring(data.indexOf(" ") + 1);

    timer_length = data.toInt();
    
    ble.print("OK");
  }
  else if(recievedText.startsWith(start_color)) {
    String data = recievedText;

    // Get rid of the tag that we don't care about
    data = data.substring(data.indexOf(" ") + 1);

    // At this point we have 3 numbers back to back with spaces inbetween

    // Strip first number
    String redString = data.substring(0, data.indexOf(" "));

    // Remove first number
    data = data.substring(data.indexOf(" ") + 1);

    // Strip second number
    String greenString = data.substring(0, data.indexOf(" ") + 1);

    // Remove second number
    data = data.substring(data.indexOf(" ") + 1);

    String blueString = data;
    blueString.trim();

    // All that is left is the last number

    start_red = redString.toInt();
    start_green = greenString.toInt();
    start_blue = blueString.toInt();
    
    //Serial.println("Starting Color = R: " + String(start_red) + " G: " + String(start_green) + " B: " + String(start_blue));
    ble.print("OK");
  }
  else if(recievedText.startsWith(end_color)) {
    String data = recievedText;

    // Get rid of the tag that we don't care about
    data = data.substring(data.indexOf(" ") + 1);

    // At this point we have 3 numbers back to back with spaces inbetween

    // Strip first number
    String redString = data.substring(0, data.indexOf(" "));

    // Remove first number
    data = data.substring(data.indexOf(" ") + 1);

    // Strip second number
    String greenString = data.substring(0, data.indexOf(" ") + 1);

    // Remove second number
    data = data.substring(data.indexOf(" ") + 1);

    String blueString = data;
    blueString.trim();

    // All that is left is the last number

    end_red = redString.toInt();
    end_green = greenString.toInt();
    end_blue = blueString.toInt();

    //Serial.println("Ending Color = R: " + String(end_red) + " G: " + String(end_green) + " B: " + String(end_blue));
    ble.print("OK");
  }
  else if (recievedText.startsWith(buzz)){
    String data = recievedText;

    // Get rid of the tag that we don't care about
    data = data.substring(data.indexOf(" ") + 1);

    buzz_at_end = data.toInt();

    if(buzz_at_end)
    {
      //Serial.println("Buzzer is on");
    }
    else
    {
      //Serial.println("Buzzer is off");
    }
    ble.print("OK");
  }    
}

 
