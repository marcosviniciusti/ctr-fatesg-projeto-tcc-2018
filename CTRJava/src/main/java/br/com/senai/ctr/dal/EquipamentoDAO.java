package br.com.senai.ctr.dal;

import br.com.senai.ctr.model.Equipamento;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

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

}
