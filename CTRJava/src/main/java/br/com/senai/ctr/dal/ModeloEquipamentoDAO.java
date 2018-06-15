package br.com.senai.ctr.dal;

import br.com.senai.ctr.model.ModeloEquipamento;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ModeloEquipamentoDAO extends DAO<ModeloEquipamento> {

    public ModeloEquipamentoDAO(DatabaseReference reference) {
        super(reference, "modelos");
    }

    @Override
    protected ValueEventListener generateNewValueEventListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ModeloEquipamento obj = snapshot.getValue(ModeloEquipamento.class);
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
