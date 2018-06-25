package br.com.marcosviniciusti.projetotcc.persistencia;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import br.com.marcosviniciusti.projetotcc.entidade.EMarca;

public class PMarca {

    private DatabaseReference ctrRef, marcaRef;

    // Atributos da classe.
    private static final String TAG = "PMarca";
    private List<EMarca> listaMarcas;
    private EMarca marca;

    public PMarca() {
        ctrRef = FirebaseDatabase.getInstance().getReference().child("CTR");
        marcaRef = ctrRef.child("marcas");
    }

    public void salvar(EMarca marca) {
        if (marca.getId() != null || !marca.getId().isEmpty()) {
            marcaRef.child(marca.getId()).setValue(marca);
        } else {
            marcaRef.push().setValue(marca);
        }
    }

    public void deletar(EMarca marca) {
        marcaRef.child(marca.getId()).removeValue();
    }

    public void consultar(EMarca marca, ValueEventListener event) {
        marcaRef.child(marca.getId()).addValueEventListener(event);
    }

    public void listar(ValueEventListener event) {
        marcaRef.addValueEventListener(event);
    }

    public List<EMarca> getListaMarcas() {
        return listaMarcas;
    }

    private void setListaMarcas(List<EMarca> listaMarcas) {
        this.listaMarcas = listaMarcas;
    }

    public EMarca getMaca() {
        return marca;
    }

    private void setMarca(EMarca marca) {
        this.marca = marca;
    }
}
