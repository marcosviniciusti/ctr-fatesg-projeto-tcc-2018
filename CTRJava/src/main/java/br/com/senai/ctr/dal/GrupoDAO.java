package br.com.senai.ctr.dal;

import br.com.senai.ctr.model.Grupo;
import br.com.senai.ctr.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GrupoDAO extends DAO<Grupo> {

    public GrupoDAO(DatabaseReference reference) {
        super(reference, "grupos");
    }

    @Override
    protected ValueEventListener generateNewValueEventListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Grupo obj = snapshot.getValue(Grupo.class);
                obj.setId(snapshot.getKey());

                map.put(snapshot.getKey(), obj);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        };
    }

}
