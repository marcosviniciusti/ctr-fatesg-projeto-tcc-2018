package br.com.marcosviniciusti.projetotcc.persistencia;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import br.com.marcosviniciusti.projetotcc.entidade.EEquipamento;

public class PEquipamento {

    private DatabaseReference ctrRef, equipamentoRef;

    // Atributos da classe.
    private static final String TAG = "PEquipamento";
    private List<EEquipamento> listaEquipamento;
    private EEquipamento equipamento;

    public PEquipamento() {
        ctrRef = FirebaseDatabase.getInstance().getReference().child("CTR");
        equipamentoRef = ctrRef.child("equipamentos");
    }

    public void salvar(EEquipamento equipamento) {
        if (equipamento.getId() != null) {
            equipamentoRef.child(equipamento.getId()).setValue(equipamento);
        } else {
            equipamentoRef.push().setValue(equipamento);
        }
    }

    public void deletar(EEquipamento equipamento) {
        equipamentoRef.child(equipamento.getId()).removeValue();
    }

    public void consultar(EEquipamento equipamento, ValueEventListener event) {
        equipamentoRef.child(equipamento.getId()).addValueEventListener(event);
    }

    public void listar(ValueEventListener event) {
        equipamentoRef.addValueEventListener(event);
    }
}
