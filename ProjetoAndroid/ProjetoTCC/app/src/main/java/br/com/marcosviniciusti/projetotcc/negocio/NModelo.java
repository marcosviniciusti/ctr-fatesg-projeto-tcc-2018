package br.com.marcosviniciusti.projetotcc.negocio;

import android.util.Log;

import com.google.firebase.database.ValueEventListener;

import br.com.marcosviniciusti.projetotcc.entidade.EModelo;
import br.com.marcosviniciusti.projetotcc.persistencia.PModelo;

public class NModelo {

    // Atributos da classe.
    private static final String TAG = "NModelo";
    private EModelo modelo;
    private PModelo persistencia;

    public NModelo() {
        try {
            this.persistencia = new PModelo();
        } catch (Exception error) {
            Log.e(TAG, "ERRO no construtor: "+error.getMessage());
        }
    }

    public void salvar(EModelo modelo) {
        try {
            this.persistencia.salvar(modelo);
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método salvar: "+error.getMessage());
        }
    }

    public void deletar(EModelo modelo) {
        try {
            this.persistencia.deletar(modelo);
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método deletar: "+error.getMessage());
        }
    }

    public void consultar(EModelo modelo, ValueEventListener event) {
        try {
            this.persistencia.consultar(modelo, event);
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
