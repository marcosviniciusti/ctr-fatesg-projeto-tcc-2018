package br.com.senai.ctr.mqtt;

import br.com.senai.ctr.App;
import br.com.senai.ctr.CTRBroker;
import br.com.senai.ctr.CTRLogs;
import br.com.senai.ctr.model.Emissor;
import jdk.nashorn.internal.parser.JSONParser;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MQTTClientCallback implements MqttCallback {

    private final String id_ctr_broker;

    public MQTTClientCallback(String id_ctr_broker) {
        this.id_ctr_broker = id_ctr_broker;
    }

    @Override
    public void connectionLost(Throwable throwable) {
        CTRLogs.log("Conexão Perdida com o MQTT");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // Quando alguma mensagem for publicada no tópico do id do broker, será feita a tentativa do registro do emissor
        if (topic.equals(id_ctr_broker)) {
            /*
            Recebe um JSON do NodeMCU:
            {
                id : "<id do emissor>",
                op : <código da operação, 1 para inclusão, 0 para exclusão do broker
            }
             */

            try {
                JSONObject recv = new JSONObject(message.toString());

                String id = recv.get("id").toString();
                Integer op = (Integer) recv.get("op");

                switch (op) {
                    case 0: {
                        CTRBroker.emissores.remove(id);
                        break;
                    }
                    case 1: {
                        CTRBroker.registraEmissor(id);
                        break;
                    }
                }

            } catch (JSONException e) {
                CTRLogs.log("Erro ao ler parâmetros do Emissor");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        // NTDH
    }
}
