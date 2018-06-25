package br.com.marcosviniciusti.projetotcc.persistencia;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import br.com.marcosviniciusti.projetotcc.entidade.EGrupoEquipamento;

public class PGrupoEquipamento {

    private DatabaseReference ctrRef, grupoEquipamentoRef;

    // Atributos da classe.
    private static final String TAG = "PGrupoEquipamento";
    private List<EGrupoEquipamento> listaGrupoEquipamento;
    private EGrupoEquipamento grupoEquipamento;

    public PGrupoEquipamento() {
        ctrRef = FirebaseDatabase.getInstance().getReference().child("CTR");
        grupoEquipamentoRef = ctrRef.child("grupos");
    }

    public void salvar(EGrupoEquipamento grupoEquipamento) {
        if (grupoEquipamento.getId() != null) {
            grupoEquipamentoRef.child(grupoEquipamento.getId()).setValue(grupoEquipamento);
        } else {
            grupoEquipamentoRef.push().setValue(grupoEquipamento);
        }
    }

    public void deletar(EGrupoEquipamento grupoEquipamento) {
        grupoEquipamentoRef.child(grupoEquipamento.getId()).removeValue();
    }

    public void consultar(EGrupoEquipamento grupoEquipamento, ValueEventListener event) {
        grupoEquipamentoRef.child(grupoEquipamento.getId()).addValueEventListener(event);
    }

    public void listar(ValueEventListener event) {
        grupoEquipamentoRef.addValueEventListener(event);
    }
}
