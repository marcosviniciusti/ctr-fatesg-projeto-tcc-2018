package br.com.senai.ctr.dal;

import br.com.senai.ctr.CTRBroker;
import br.com.senai.ctr.model.Comando;
import br.com.senai.ctr.model.Equipamento;
import br.com.senai.ctr.model.Transmissao;
import br.com.senai.ctr.mqtt.MQTTClientManager;
import com.google.firebase.database.*;
import org.json.JSONObject;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class TransmissaoDAO extends DAO<Transmissao> {

    public TransmissaoDAO(DatabaseReference reference) {
        super(reference, "transmissoes");
    }

    @Override
    protected ValueEventListener generateNewValueEventListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Transmissao obj = snapshot.getValue(Transmissao.class);
                obj.setId(snapshot.getKey());

                map.put(snapshot.getKey(), obj);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                error.toException().printStackTrace();
            }
        };
    }

    public List<Transmissao> syncRetrieveTransmissoesPendentesByEquipamento(Equipamento equipamento) {
        return syncRetrieveTransmissoesPendentesByEquipamento(equipamento.getId());
    }

    public List<Transmissao> syncRetrieveTransmissoesPendentesByEquipamento(String idEquipamento) {
        List<Transmissao> transmissoes = new LinkedList<>();
        final boolean[] buscando = new boolean[]{true};

        Query query = reference.child(child).orderByChild("equipamentoTransmissao").equalTo(idEquipamento);
        ValueEventListener listener = query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot s : snapshot.getChildren()) {
                    Transmissao e = s.getValue(Transmissao.class);
                    if (e.getDtHrTransmissao() == null) {
                        e.setId(s.getKey());

                        transmissoes.add(e);
                    }
                }
                buscando[0] = false;
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        while (buscando[0]) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
//                e.printStackTrace();
            }
        }
        query.removeEventListener(listener);

        return transmissoes;
    }

//    public void syncTransmissaoByEmissor(final String id_emissor, final ConcurrentLinkedDeque<CTRBroker.TransmissaoIR> transmissoes) {
//
//        reference.child("equipamentos").orderByChild("emissorEquipamento").equalTo(id_emissor)
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot snapshot) {
//                        for (DataSnapshot s : snapshot.getChildren()) {
//                            Equipamento e = s.getValue(Equipamento.class);
//                            e.setId(s.getKey());
//
//                            String[] m = e.getModeloEquipamento().split("/");
//                            if (m.length == 2) {
//                                reference.child("transmissoes").orderByChild("equipamentoTransmissao").equalTo(e.getId())
//                                        .addValueEventListener(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(DataSnapshot snapshot) {
//                                                for (DataSnapshot s : snapshot.getChildren()) {
//                                                    Transmissao t = s.getValue(Transmissao.class);
//                                                    t.setId(s.getKey());
//
//
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onCancelled(DatabaseError error) {
//
//                                            }
//                                        });
//                            }
//
//
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError error) {
//
//                    }
//                });
//
//    }
}
