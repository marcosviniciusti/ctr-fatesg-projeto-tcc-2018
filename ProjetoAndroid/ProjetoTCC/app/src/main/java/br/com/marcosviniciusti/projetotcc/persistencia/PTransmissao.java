package br.com.marcosviniciusti.projetotcc.persistencia;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.marcosviniciusti.projetotcc.entidade.ETransmissao;

public class PTransmissao {

    private DatabaseReference ctrRef, transmissaoRef;

    // Atributos da classe.
    private static final String TAG = "PTransmissao";
    private List<ETransmissao> transmissoes;
    private ETransmissao transmissao;

    public PTransmissao() {
        ctrRef = FirebaseDatabase.getInstance().getReference().child("CTR");
        transmissaoRef = ctrRef.child("transmissoes");
        transmissoes = new ArrayList<>();
        transmissao = new ETransmissao();
    }

    public void salvar(ETransmissao transmissao) {
        String id = transmissao.getId();
        if (id != null || !id.isEmpty()) {
            transmissaoRef.child(id).setValue(transmissao);
        } else {
            transmissaoRef.push().setValue(transmissao);
        }
    }

    public void deletar(ETransmissao transmissao) {
        transmissaoRef.child(transmissao.getId()).removeValue();
    }

    public void consultar(ETransmissao transmissao, ValueEventListener event) {
        transmissaoRef.child(transmissao.getId()).addValueEventListener(event);
    }

    public void listar(ValueEventListener event) {
        transmissaoRef.addValueEventListener(event);
    }
}
