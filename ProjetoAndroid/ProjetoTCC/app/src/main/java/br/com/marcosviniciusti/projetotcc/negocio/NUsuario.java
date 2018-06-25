package br.com.marcosviniciusti.projetotcc.negocio;

import android.util.Log;

import com.google.firebase.database.ValueEventListener;

import br.com.marcosviniciusti.projetotcc.entidade.EUsuario;
import br.com.marcosviniciusti.projetotcc.persistencia.PUsuario;

public class NUsuario {

    // Atributos da classe.
    private static final String TAG = "NUsuario";
    private EUsuario usuario = new EUsuario();
    private PUsuario persistencia;

    public NUsuario() {
        try {
            this.persistencia = new PUsuario();
        } catch (Exception error) {
            Log.e(TAG, "ERRO no construtor: "+error.getMessage());
        }
    }

    public void salvar(EUsuario usuario) {
        try {
            this.persistencia.salvar(usuario);
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método salvar: "+error.getMessage());
        }
    }

    public void deletar(EUsuario usuario) {
        try {
            this.persistencia.deletar(usuario);
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método deletar: "+error.getMessage());
        }
    }

    public void consultar(EUsuario usuario, ValueEventListener event) {
        try {
            this.persistencia.consultar(usuario, event);
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
