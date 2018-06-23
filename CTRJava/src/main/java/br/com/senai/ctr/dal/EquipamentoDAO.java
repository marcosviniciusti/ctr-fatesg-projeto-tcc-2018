package br.com.senai.ctr.dal;

import br.com.senai.ctr.model.Emissor;
import br.com.senai.ctr.model.Equipamento;
import com.google.firebase.database.*;

import java.util.LinkedList;
import java.util.List;

public class EquipamentoDAO extends DAO<Equipamento> {

    public EquipamentoDAO(DatabaseReference reference) {
        super(reference, "equipamentos");
    }

    @Override
    protected ValueEventListener generateNewValueEventListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Equipamento obj = snapshot.getValue(Equipamento.class);
                obj.setId(snapshot.getKey());

                map.put(snapshot.getKey(), obj);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                error.toException().printStackTrace();
            }
        };
    }

    public List<Equipamento> syncRetrieveEquipamentosByEmissor(Emissor emissor) {
        return syncRetrieveEquipamentosByEmissor(emissor.getId());
    }

    public List<Equipamento> syncRetrieveEquipamentosByEmissor(String idEmissor) {
        List<Equipamento> equipamentos = new LinkedList<>();
        final boolean[] buscando = new boolean[]{true};

        Query query = reference.child(child).orderByChild("emissorEquipamento").equalTo(idEmissor);
        ValueEventListener listener = query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot s : snapshot.getChildren()) {
                    Equipamento e = s.getValue(Equipamento.class);
                    e.setId(s.getKey());
                    equipamentos.add(e);
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

        return equipamentos;
    }

}
