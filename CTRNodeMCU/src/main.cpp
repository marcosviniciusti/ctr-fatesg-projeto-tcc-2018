/*------------------------INCLUDES-------------------------*/
/*________________________FRAMEWORK________________________*/
#include <Arduino.h>

/*______________________INFRAVERMELHO______________________*/
#include <IRremoteESP8266.h>
#include <IRrecv.h>
#include <IRutils.h>

/*__________________________JSON___________________________*/
#include <ArduinoJson.h>

/*__________________________WIFI___________________________*/
#include <ESP8266WiFi.h>

/*__________________________MQTT___________________________*/
#include <PubSubClient.h>
/*---------------------------------------------------------*/

/*______________________CONFIGURAÇÕES______________________*/

#define WIFI_SSID "*"     // Nome da rede WiFi
#define WIFI_PASSWORD "*" // Senha da rede WiFi

#define MQTT_SERVER "*"              // Endereço do broker MQTT Mosquitto
#define MQTT_PORT 1883               // Porta utilizada pelo broker
#define MQTT_BROKER_TOPICO "MQTTCTR" // Tópico de inscrição do dispositivo

#define GREEN_LED 12 // Led para eventuais testes
#define IV_LED 4     // Sinal enviado para a base do transistor ou led Infravermelho

#define ID_EMISSOR "e94jr8tg" // Identificação do dispositivo

/*_________________________________________________________*/

/*____________________VARIÁVEIS GLOBAIS____________________*/
WiFiClient espClient;
PubSubClient MQTT(espClient);
char EstadoSaida = '0';
/*_________________________________________________________*/

/*____________________DEFINE PROTOTIPOS____________________*/
void inicializaSaida();
void inicializaWiFi();
void inicializaMQTT();
void reconectarWiFi();
void reconectarMQTT();
void mqtt_callback(char *topic, byte *payload, unsigned int length);
void verificarWifiMQTT();
/*_________________________________________________________*/

/*__________________________SETUP__________________________*/
void setup()
{
  inicializaSaida();
  Serial.begin(9600);
  inicializaWiFi();
  inicializaMQTT();
}
/*_________________________________________________________*/

/*__________________________LOOP___________________________*/
void loop()
{
  verificarWifiMQTT();

  MQTT.loop();
}

void verificarWifiMQTT()
{
  if (!MQTT.connected())
  {
    reconectarMQTT();
  }

  reconectarWiFi();
}
/*_________________________________________________________*/

/*_________________________OUTPUT__________________________*/
void inicializaSaida()
{
  pinMode(GREEN_LED, OUTPUT);
  digitalWrite(GREEN_LED, LOW);
}
/*_________________________________________________________*/

/*__________________________WIFI___________________________*/
void inicializaWiFi()
{
  delay(10);
  Serial.println("------Conexao WI-FI------");
  Serial.print("Conectando-se na rede: ");
  Serial.println(WIFI_SSID);
  Serial.println("Aguarde");

  reconectarWiFi();
}

void reconectarWiFi()
{
  if (WiFi.status() == WL_CONNECTED)
    return;

  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);

  while (WiFi.status() != WL_CONNECTED)
  {
    delay(100);
    Serial.print(".");
  }

  Serial.println();
  Serial.print("Conectado com sucesso na rede ");
  Serial.print(WIFI_SSID);
  Serial.println("IP obtido: ");
  Serial.println(WiFi.localIP());
}
/*_________________________________________________________*/

/*__________________________MQTT___________________________*/
void inicializaMQTT()
{
  MQTT.setServer(MQTT_SERVER, MQTT_PORT);
  MQTT.setCallback(mqtt_callback);
}

void reconectarMQTT()
{
  while (!MQTT.connected() && WiFi.status() == WL_CONNECTED)
  {
    Serial.print("* Tentando se conectar ao Broker MQTT: ");
    Serial.println(MQTT_SERVER);
    if (MQTT.connect(ID_EMISSOR))
    {
      Serial.println("Conectado com sucesso ao broker MQTT!");
      String msg = ID_EMISSOR;

      StaticJsonBuffer<200> jsonBuffer;
      JsonObject &root = jsonBuffer.createObject();
      root["id"] = ID_EMISSOR;
      root["operacao"] = 0;
      root.printTo(msg);

      MQTT.publish(MQTT_BROKER_TOPICO, (char *)msg.c_str());
      MQTT.subscribe(ID_EMISSOR);
    }
    else
    {
      Serial.println("Falha ao reconectar no broker.");
      Serial.println("Haverá nova tentatica de conexao em 2s");
      delay(2000);
    }
  }
}

void mqtt_callback(char *topic, byte *payload, unsigned int length)
{
  String msg;

  for (unsigned int i = 0; i < length; i++)
  {
    char c = (char)payload[i];
    msg += c;
  }

  DynamicJsonBuffer jsonBuffer(1024);

  JsonObject &root = jsonBuffer.parseObject(msg);

  if (!root.success())
  {
    Serial.println("parseObject() failed");
    return;
  }
}
/*_________________________________________________________*/