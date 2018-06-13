package br.com.senai.ctr.dal;

import br.com.senai.ctr.model.GrupoEquipamento;
import br.com.senai.ctr.model.GrupoUsuario;
import br.com.senai.ctr.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GrupoUsuarioDAO extends DAO<GrupoUsuario> {

    public GrupoUsuarioDAO(DatabaseReference reference) {
        super(reference, "gruposUsuario");
    }

    @Override
    protected ValueEventListener generateNewValueEventListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                GrupoUsuario obj = snapshot.getValue(GrupoUsuario.class);
                obj.setId(snapshot.getKey());

                map.put(snapshot.getKey(), obj);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        };
    }

    public List<Usuario> syncRetrieveUsuariosByGrupo(GrupoUsuario grp) {
        if (grp == null || grp.getId() == null) {
            return null;
        }

        HashMap<String, Boolean> refmap = syncRetrieveReferences(grp, "usuariosList");

        List<Usuario> ret = new ArrayList<>();
        for (String k : refmap.keySet()) {
            ret.add(new Usuario().setId(k));
        }

        grp.setUsuarios(ret);

        return ret;
    }

    public List<GrupoEquipamento> syncRetrieveGruposEquipamentosByGrupoUsuarios(GrupoUsuario grp) {
        if (grp == null || grp.getId() == null) {
            return null;
        }

        HashMap<String, Boolean> refmap = syncRetrieveReferences(grp, "gruposEquipamentos");

        List<GrupoEquipamento> ret = new ArrayList<>();
        for (String k : refmap.keySet()) {
            ret.add(new GrupoEquipamento().setId(k));
        }

        grp.setGruposEquipamento(ret);

        return ret;
    }

}
