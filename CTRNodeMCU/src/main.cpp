/*------------------------INCLUDES-------------------------*/
/*________________________FRAMEWORK________________________*/
#include <Arduino.h>

/*______________________INFRAVERMELHO______________________*/
#include <IRremoteESP8266.h>
#include <IRsend.h>

/*__________________________JSON___________________________*/
#include <ArduinoJson.h>

/*__________________________WIFI___________________________*/
#include <ESP8266WiFi.h>

/*__________________________MQTT___________________________*/
#include <PubSubClient.h>
// Para projetos recem importados: é necessário alterar
// PubSubClient.h na definição da variável global
// MQTT_MAX_PACKET_SIZE para o valor 2048
// para que o programa funcione corretamente
/*---------------------------------------------------------*/

/*______________________CONFIGURAÇÕES______________________*/

#define WIFI_SSID "Visitantes"         // Nome da rede WiFi
#define WIFI_PASSWORD "bem-vindos"     // Senha da rede WiFi

#define MQTT_SERVER "192.168.1.215"    // Endereço do broker MQTT Mosquitto
#define MQTT_PORT 1883                 // Porta utilizada pelo broker
#define MQTT_BROKER_TOPICO "CTRBroker" // Tópico de inscrição do dispositivo

#define GREEN_LED 12 // Led para eventuais testes
#define IR_LED 4     // Sinal enviado para a base do transistor ou led Infravermelho

#define IR_BUFFER_SIZE 256 // Tamanho do buffer de parâmetros da transmissão Infravermelho

#define ID_EMISSOR "abcdefg" // Identificação do dispositivo
/*_________________________________________________________*/

/*____________________VARIÁVEIS GLOBAIS____________________*/
WiFiClient espClient;
PubSubClient MQTT(espClient);
IRsend irsend(IR_LED);  // Set the GPIO to be used to sending the message.
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
  irsend.begin();
  inicializaSaida();
  Serial.begin(115200);
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
      Serial.println("Conectado com sucesso ao broker MQTT");
      // {"id" : "abcdefg", "op" : 1}
      String msg = String();
      msg.concat("{\"id\":\"");
      msg.concat(ID_EMISSOR);
      msg.concat("\",\"op\":1}");

      MQTT.publish(MQTT_BROKER_TOPICO, (char *)msg.c_str());
      MQTT.subscribe(ID_EMISSOR);
    }
    else
    {
      Serial.println("Falha ao reconectar no broker.");
      Serial.println("Haverá nova tentativa de conexao em 2s");
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

  DynamicJsonBuffer jsonBuffer(2048);

  JsonObject &root = jsonBuffer.parseObject(msg);

  if (!root.success())
  {
    Serial.println("Erro ao interpretar mensagem");
    return;
  }

  int tamanho_comando = root["size"];
  uint16_t rawData[IR_BUFFER_SIZE];
  for (int i = 0; i < tamanho_comando; i++)  
  {
    rawData[i] = root["rawData"][i];
  }

  irsend.sendRaw(rawData, tamanho_comando, 38);

  Serial.println("Comando enviado");
}
/*_________________________________________________________*/