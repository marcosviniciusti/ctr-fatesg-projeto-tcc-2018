package br.com.senai.ctr.dal;

import br.com.senai.ctr.App;
import br.com.senai.ctr.model.Equipamento;
import br.com.senai.ctr.model.Transmissao;
import com.google.firebase.database.*;

import java.util.ArrayList;
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

    public void syncTransmissaoByEquipamento(Equipamento eq) {
        Query q = reference.child(child)
                .orderByChild("equipamentoTransmissao").equalTo(eq.getId());

        q.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                Transmissao t = snapshot.getValue(Transmissao.class);

                if (t.getDtHrTransmissao() == null) {
                    App.transmissoes.add(t);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}
