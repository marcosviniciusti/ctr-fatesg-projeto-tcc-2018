package br.com.senai.ctr;

import br.com.senai.ctr.dal.ComandoDAO;
import br.com.senai.ctr.dal.EquipamentoDAO;
import br.com.senai.ctr.dal.TransmissaoDAO;
import br.com.senai.ctr.model.Comando;
import br.com.senai.ctr.model.Emissor;
import br.com.senai.ctr.model.Equipamento;
import br.com.senai.ctr.model.Transmissao;
import br.com.senai.ctr.mqtt.MQTTClientManager;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

public class MonitorTransmissoes implements Runnable {

    private TransmissaoDAO trdao;
    private EquipamentoDAO eqdao;
    private ComandoDAO codao;

    private Integer operacao;

    public MonitorTransmissoes(Integer operacao) {
        this.operacao = operacao;
    }

    @Override
    public void run() {
        if (operacao == 1) {
            recuperaTransmissoes();
        } else if (operacao == 2) {
            monitoraTransmissoes();
        }
    }

    private void recuperaTransmissoes() {
        if (trdao == null) {
            trdao = new TransmissaoDAO(Firebase.getReference());
        }
        if (eqdao == null) {
            eqdao = new EquipamentoDAO(Firebase.getReference());
        }
        if (codao == null) {
            codao = new ComandoDAO(Firebase.getReference());
        }

        while (true) {
            for (String k : CTRBroker.emissores.keySet()) {
                Emissor em = CTRBroker.emissores.get(k);
                if (em != null) {
                    for (Equipamento e : eqdao.syncRetrieveEquipamentosByEmissor(em.getId())) {
                        String[] equipamentoModelo = e.getModeloEquipamento().split("/");
                        if (equipamentoModelo.length == 2) {
                            List<Transmissao> transmissoes = trdao.syncRetrieveTransmissoesPendentesByEquipamento(e);
                            for (Transmissao t : transmissoes) {

                                Comando c = codao
                                        .syncRetrieveComando(
                                                equipamentoModelo[0],
                                                equipamentoModelo[1],
                                                t.getComandoTransmissao()
                                        );
                                TransmissaoIR tir = new TransmissaoIR()
                                        .setIdTransmissao(t.getId())
                                        .setIdEmissor(em.getId())
                                        .setRawData(c.getRawData())
                                        .setType("T");

                                t.setDtHrTransmissao(new Date());
                                trdao.update(t);

                                CTRBroker.transmissoes.addLast(tir);
                            }
                        }

                    }
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void monitoraTransmissoes() {
        while (true) {
            if (CTRBroker.transmissoes.size() > 0) {
                TransmissaoIR t = CTRBroker.transmissoes.removeFirst();

                JSONObject j = new JSONObject()
                        .put("type", t.getType())
                        .put("size", t.getRawData().size())
                        .put("rawData", t.getRawData());

                MQTTClientManager.publicar(t.getIdEmissor(), j.toString().getBytes());
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
