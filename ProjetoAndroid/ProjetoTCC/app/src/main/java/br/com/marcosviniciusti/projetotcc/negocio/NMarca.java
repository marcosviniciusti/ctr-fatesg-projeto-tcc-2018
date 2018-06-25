package br.com.marcosviniciusti.projetotcc.negocio;

import android.util.Log;

import com.google.firebase.database.ValueEventListener;

import br.com.marcosviniciusti.projetotcc.entidade.EMarca;
import br.com.marcosviniciusti.projetotcc.persistencia.PMarca;

public class NMarca {

    // Atributos da classe.
    private static final String TAG = "NMarca";
    private EMarca marca;
    private PMarca persistencia;

    public NMarca() {
        try {
            this.persistencia = new PMarca();
        } catch (Exception error) {
            Log.e(TAG, "ERRO no construtor: "+error.getMessage());
        }
    }

    public void salvar(EMarca marca) {
        try {
            this.persistencia.salvar(marca);
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método salvar: "+error.getMessage());
        }
    }

    public void deletar(EMarca marca) {
        try {
            this.persistencia.deletar(marca);
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método deletar: "+error.getMessage());
        }
    }

    public void consultar(EMarca marca, ValueEventListener event) {
        try {
            this.persistencia.consultar(marca, event);
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método salvar: "+error.getMessage());
        }
    }

    public void listar(ValueEventListener event) {
        try {
            this.persistencia.listar(event);
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método salvar: "+error.getMessage());
        }
    }
}
