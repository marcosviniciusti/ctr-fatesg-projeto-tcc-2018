package br.com.senai.ctr.dal;

import br.com.senai.ctr.model.Equipamento;
import br.com.senai.ctr.model.Transmissao;
import com.google.firebase.database.*;

import java.util.LinkedList;
import java.util.List;

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
}
