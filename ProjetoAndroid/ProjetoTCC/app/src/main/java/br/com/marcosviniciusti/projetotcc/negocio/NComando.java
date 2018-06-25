package br.com.marcosviniciusti.projetotcc.negocio;

import android.util.Log;

import com.google.firebase.database.ValueEventListener;

import br.com.marcosviniciusti.projetotcc.entidade.EComando;
import br.com.marcosviniciusti.projetotcc.persistencia.PComando;

public class NComando {

    // Atributos da classe.
    private static final String TAG = "NComando";
    private EComando comando = new EComando();
    private PComando persistencia;

    public NComando() {
        try {
            this.persistencia = new PComando();
        } catch (Exception error) {
            Log.e(TAG, "ERRO no construtor: "+error.getMessage());
        }
    }

    public void salvar(EComando comando) {
        try {
            this.persistencia.salvar(comando);
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método salvar: "+error.getMessage());
        }
    }

    public void deletar(EComando comando) {
        try {
            this.persistencia.deletar(comando);
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método deletar: "+error.getMessage());
        }
    }

    public void consultar(EComando comando, ValueEventListener event) {
        try {
            this.persistencia.consultar(comando, event);
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
