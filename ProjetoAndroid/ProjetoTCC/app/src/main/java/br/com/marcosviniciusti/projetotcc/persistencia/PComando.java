package br.com.marcosviniciusti.projetotcc.persistencia;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.marcosviniciusti.projetotcc.entidade.EComando;

public class PComando {

    private DatabaseReference ctrRef, usuarioRef;

    // Atributos da classe.
    private static final String TAG = "PComando";
    private List<EComando> comandos;
    private EComando comando;

    public PComando() {
        ctrRef = FirebaseDatabase.getInstance().getReference().child("CTR");
        usuarioRef = ctrRef.child("usuarios");
        comandos = new ArrayList<>();
        comando = new EComando();
    }

    public void salvar(EComando comando) {
        String id = comando.getId();
        if (id != null || !id.isEmpty()) {
            usuarioRef.child(id).setValue(comando);
        } else {
            usuarioRef.push().setValue(comando);
        }
    }

    public void deletar(EComando comando) {
        usuarioRef.child(comando.getId()).removeValue();
    }

    public void consultar(EComando comando, ValueEventListener event) {
        usuarioRef.child(comando.getId()).addValueEventListener(event);
    }

    public void listar(ValueEventListener event) {
        usuarioRef.addValueEventListener(event);
    }
}
