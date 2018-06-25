package br.com.marcosviniciusti.projetotcc.persistencia;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import br.com.marcosviniciusti.projetotcc.entidade.EModelo;

public class PModelo {

    private DatabaseReference ctrRef, modeloRef;

    // Atributos da classe.
    private static final String TAG = "PModelo";
    private List<EModelo> listaModelos;
    private EModelo modelo;

    public PModelo() {
        ctrRef = FirebaseDatabase.getInstance().getReference().child("CTR");
        modeloRef = ctrRef.child("modelos");
    }

    public void salvar(EModelo modelo) {
        if (modelo.getId() != null || !modelo.getId().isEmpty()) {
            modeloRef.child(modelo.getId()).setValue(modelo);
        } else {
            modeloRef.push().setValue(modelo);
        }
    }

    public void deletar(EModelo modelo) {
        modeloRef.child(modelo.getId()).removeValue();
    }

    public void consultar(EModelo modelo, ValueEventListener event) {
        modeloRef.child(modelo.getId()).addValueEventListener(event);
    }

    public void listar(ValueEventListener event) {
        modeloRef.addValueEventListener(event);
    }

    public List<EModelo> getListaModelos() {
        return listaModelos;
    }

    private void setListaModelos(List<EModelo> listaModelos) {
        this.listaModelos = listaModelos;
    }

    public EModelo getModelo() {
        return modelo;
    }

    private void setModelo(EModelo modelo) {
        this.modelo = modelo;
    }

}
