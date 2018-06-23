package br.com.senai.ctr;

import br.com.senai.ctr.dal.EmissorDAO;
import br.com.senai.ctr.model.Emissor;
import br.com.senai.ctr.mqtt.MQTTClientCallback;
import br.com.senai.ctr.mqtt.MQTTClientManager;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class CTRBroker {

    public static final String url_mqtt_server;
    public static final String id_ctr_broker;
    public static final ConcurrentLinkedDeque<TransmissaoIR> transmissoes;
    public static final HashMap<String, Emissor> emissores;
    public static final MonitorTransmissoes monitorRecuperaTransmissoes;
    public static final MonitorTransmissoes monitorEmiteTransmissoes;
    public static final MQTTClientManager mqttClient;
    public static final MQTTClientCallback mqttCallback;

    static {
        url_mqtt_server = "tcp://localhost:1883";
        id_ctr_broker = "CTRBroker";
        transmissoes = new ConcurrentLinkedDeque<>();
        emissores = new HashMap<>();
        monitorRecuperaTransmissoes = new MonitorTransmissoes(1);
        monitorEmiteTransmissoes = new MonitorTransmissoes(2);
        mqttClient = new MQTTClientManager();
        mqttCallback = new MQTTClientCallback(id_ctr_broker);
    }

    private CTRBroker() {

    }

    public static void registraEmissor(String idEmissor) {
        if (CTRBroker.emissores.get(idEmissor) == null) {
            // Recupera Emissor a partir do banco
            EmissorDAO emdao = new EmissorDAO(Firebase.getReference());
            Emissor em = emdao.syncRetrieve(idEmissor);
            emissores.put(em.getId(), em);

            CTRLogs.log("Emissor " + idEmissor + " registrado.");
        }
    }

    public static void inicializar() {
        mqttClient.conectar(url_mqtt_server, id_ctr_broker, mqttCallback);

        Firebase.inicializarFirebase();

        new Thread(monitorRecuperaTransmissoes).start();
        new Thread(monitorEmiteTransmissoes).start();
    }
}
