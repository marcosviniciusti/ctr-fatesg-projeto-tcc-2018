package br.com.senai.ctr.dal;

import br.com.senai.ctr.model.Grupo;
import br.com.senai.ctr.model.Usuario;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UsuarioDAO extends DAO<Usuario> {

    public UsuarioDAO(DatabaseReference reference) {
        super(reference, "usuarios");
    }

    @Override
    protected ValueEventListener generateNewValueEventListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Usuario obj = snapshot.getValue(Usuario.class);
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


