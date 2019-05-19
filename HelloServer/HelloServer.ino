#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <ESP8266WebServer.h>
#include <ESP8266mDNS.h>

#ifndef STASSID
#define STASSID "Jay-Suthar"
#define STAPSK  "jrs10499a"
#endif

const char* ssid = STASSID;
const char* password = STAPSK;
const int led1 = 16, led2 = 5, led3 = 4, led4 = 0, LEDATT = 14, LEDMED = 13;


//Static IP address configuration
IPAddress staticIP(192, 168, 43, 90); //ESP static ip
IPAddress gateway(192, 168, 43, 1);   //IP Address of your WiFi Router (Gateway)
IPAddress subnet(255, 255, 255, 0);  //Subnet mask
IPAddress dns(8, 8, 8, 8);  //DNS


ESP8266WebServer server(80);

const int led = 13;

void handleRoot() {
  digitalWrite(led, 1);
  server.send(200, "text/plain", "hello from esp8266!");
  digitalWrite(led, 0);
}

void handleNotFound() {
  digitalWrite(led, 1);
  String message = "File Not Found\n\n";
  message += "URI: ";
  message += server.uri();
  message += "\nMethod: ";
  message += (server.method() == HTTP_GET) ? "GET" : "POST";
  message += "\nArguments: ";
  message += server.args();
  message += "\n";
  for (uint8_t i = 0; i < server.args(); i++) {
    message += " " + server.argName(i) + ": " + server.arg(i) + "\n";
  }
  server.send(404, "text/plain", message);
  digitalWrite(led, 0);
}

void setup(void) {
  pinMode(led, OUTPUT);
  digitalWrite(led, 0);
  Serial.begin(115200);
  WiFi.config(staticIP, subnet, gateway, dns);
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
  Serial.println("");

  //Jay
  pinMode(led1, OUTPUT);
  pinMode(led2, OUTPUT);
  digitalWrite(led1, HIGH);
  digitalWrite(led2, HIGH);
  pinMode(led3, OUTPUT);
  pinMode(led4, OUTPUT);
  digitalWrite(led3, HIGH);
  digitalWrite(led4, HIGH);

  pinMode(LEDATT, OUTPUT);
  pinMode(LEDMED, OUTPUT);

  //End

  // Wait for connection
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.print("Connected to ");
  Serial.println(ssid);
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());

  if (MDNS.begin("esp8266")) {
    Serial.println("MDNS responder started");
  }

  server.on("/", handleRoot);

  server.on("/inline", []() {
    server.send(200, "text/plain", "this works as well");
  });

  server.on("/toggle1", []() {
    digitalWrite(led1, !digitalRead(led1));
    server.send(200, "text/html", "<H1>It Works</H1>");
  });
  server.on("/toggle1ON", []() {
    digitalWrite(led1, LOW);
    server.send(200, "text/html", "<H1>It Works</H1>");
  });
  server.on("/toggle1OF", []() {
    digitalWrite(led1, HIGH);
    server.send(200, "text/html", "<H1>It Works</H1>");
  });
  server.on("/toggle2", []() {
    digitalWrite(led2, !digitalRead(led2));
    server.send(200, "text/html", "<H1>It Works</H1>");
  });
  server.on("/toggle3", []() {
    digitalWrite(led3, !digitalRead(led3));
    server.send(200, "text/html", "<H1>It Works</H1>");
  });

  server.on("/toggle4", []() {
    digitalWrite(led4, !digitalRead(led4));
    server.send(200, "text/html", "<H1>It Works</H1>");
  });


  server.on("/LEDATT0", []() {
    analogWrite(LEDATT, 0);
    server.send(200, "text/html", "<H1>It Works</H1>");
  });

  server.on("/LEDATT20", []() {
    analogWrite(LEDATT, 51);
    server.send(200, "text/html", "<H1>It Works</H1>");
  });

  server.on("/LEDATT2140", []() {
    analogWrite(LEDATT, 102);
    server.send(200, "text/html", "<H1>It Works</H1>");
  });

  server.on("/LEDATT4160", []() {
    analogWrite(LEDATT, 128);
    server.send(200, "text/html", "<H1>It Works</H1>");
  });

  server.on("/LEDATT6180", []() {
    analogWrite(LEDATT, 179);
    server.send(200, "text/html", "<H1>It Works</H1>");
  });

  server.on("/LEDATT81100", []() {
    analogWrite(LEDATT, 255);
    server.send(200, "text/html", "<H1>It Works</H1>");
  });

 server.on("/LEDMED0", []() {
    analogWrite(LEDMED, 0);
    server.send(200, "text/html", "<H1>It Works</H1>");
  });

  server.on("/LEDMED20", []() {
    analogWrite(LEDMED, 51);
    server.send(200, "text/html", "<H1>It Works</H1>");
  });

  server.on("/LEDMED2140", []() {
    analogWrite(LEDMED, 102);
    server.send(200, "text/html", "<H1>It Works</H1>");
  });

  server.on("/LEDMED4160", []() {
    analogWrite(LEDMED, 128);
    server.send(200, "text/html", "<H1>It Works</H1>");
  });

  server.on("/LEDMED6180", []() {
    analogWrite(LEDMED, 179);
    server.send(200, "text/html", "<H1>It Works</H1>");
  });

  server.on("/LEDMED81100", []() {
    analogWrite(LEDMED, 255);
    server.send(200, "text/html", "<H1>It Works</H1>");
  });
  server.on("/STOP", []() {
    digitalWrite(16, HIGH);
    digitalWrite(5, HIGH);
    digitalWrite(4, HIGH);
    digitalWrite(0, HIGH);
    server.send(200, "text/html", "<H1>It Works</H1>");
  });

  server.onNotFound(handleNotFound);

  server.begin();
  Serial.println("HTTP server started");
}

void loop(void) {
  server.handleClient();
  MDNS.update();
}
