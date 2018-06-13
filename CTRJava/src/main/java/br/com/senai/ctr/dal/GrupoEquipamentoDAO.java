package br.com.senai.ctr.dal;

import br.com.senai.ctr.model.Equipamento;
import br.com.senai.ctr.model.GrupoEquipamento;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GrupoEquipamentoDAO extends DAO {

    public GrupoEquipamentoDAO(DatabaseReference reference) {
        super(reference, "gruposEquipamento");
    }

    @Override
    protected ValueEventListener generateNewValueEventListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                GrupoEquipamento obj = snapshot.getValue(GrupoEquipamento.class);
                obj.setId(snapshot.getKey());

                map.put(snapshot.getKey(), obj);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                error.toException().printStackTrace();
            }
        };
    }

    public List<Equipamento> syncRetrieveEquipamentosByGrupo(GrupoEquipamento grp) {
        if (grp == null || grp.getId() == null) {
            return null;
        }

        HashMap<String, Boolean> refmap = syncRetrieveReferences(grp, "equipamentosList");

        List<Equipamento> ret = new ArrayList<>();
        for (String k : refmap.keySet()) {
            ret.add(new Equipamento().setId(k));
        }

        grp.setEquipamentos(ret);

        return ret;
    }

}
