package br.com.marcosviniciusti.projetotcc.negocio;

import android.util.Log;

import com.google.firebase.database.ValueEventListener;

import br.com.marcosviniciusti.projetotcc.entidade.ETransmissao;
import br.com.marcosviniciusti.projetotcc.persistencia.PTransmissao;

public class NTransmissao {

    // Atributos da classe.
    private static final String TAG = "NTransmissao";
    private ETransmissao transmissao;
    private PTransmissao persistencia;

    public NTransmissao() {
        try {
            this.persistencia = new PTransmissao();
        } catch (Exception error) {
            Log.e(TAG, "ERRO no construtor: "+error.getMessage());
        }
    }

    public void salvar(ETransmissao transmissao) {
        try {
            this.persistencia.salvar(transmissao);
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método salvar: "+error.getMessage());
        }
    }

    public void deletar(ETransmissao transmissao) {
        try {
            this.persistencia.deletar(transmissao);
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método deletar: "+error.getMessage());
        }
    }

    public void consultar(ETransmissao transmissao, ValueEventListener event) {
        try {
            this.persistencia.consultar(transmissao, event);
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
