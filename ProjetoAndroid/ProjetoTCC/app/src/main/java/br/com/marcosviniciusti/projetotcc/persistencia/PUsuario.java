package br.com.marcosviniciusti.projetotcc.persistencia;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.marcosviniciusti.projetotcc.entidade.EUsuario;

public class PUsuario {

    private DatabaseReference ctrRef, usuarioRef;

    // Atributos da classe.
    private static final String TAG = "PUsuario";
    private List<EUsuario> listaUsuario;
    private EUsuario usuario;

    public PUsuario() {
        ctrRef = FirebaseDatabase.getInstance().getReference().child("CTR");
        usuarioRef = ctrRef.child("usuarios");
        listaUsuario = new ArrayList<>();
        usuario = new EUsuario();
    }

    public void salvar(EUsuario usuario) {
        String id = usuario.getId();
        if (id != null || !id.isEmpty()) {
            usuarioRef.child(id).setValue(usuario);
        } else {
            usuarioRef.push().setValue(usuario);
        }
    }

    public void deletar(EUsuario usuario) {
        usuarioRef.child(usuario.getId()).removeValue();
    }

    public void consultar(EUsuario usuario, ValueEventListener event) {
        usuarioRef.child(usuario.getId()).addValueEventListener(event);
    }

    public void listar(ValueEventListener event) {
        usuarioRef.addValueEventListener(event);
    }

}
