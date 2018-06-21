package br.com.senai.ctr;

import br.com.senai.ctr.dal.TransmissaoDAO;
import br.com.senai.ctr.model.Emissor;
import br.com.senai.ctr.model.Transmissao;
import br.com.senai.ctr.mqtt.MQTTClientManager;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class App {

    private static final String url_mqtt_server = "tcp://localhost:1883";
    public static final String id_ctr_broker = "CTRBroker";
    public static final Thread monitorRecuperaTransmissoes = new Thread(new MonitorTransmissoes(1));
    public static final Thread monitorEmiteTransmissoes = new Thread(new MonitorTransmissoes(2));

    public static void main(String[] args) {
        MQTTClientManager.conectar(url_mqtt_server, id_ctr_broker);

        Firebase.inicializarFirebase();

        monitorRecuperaTransmissoes.start();
        monitorEmiteTransmissoes.start();

    }
}