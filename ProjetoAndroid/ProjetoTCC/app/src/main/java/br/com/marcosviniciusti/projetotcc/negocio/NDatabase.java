package br.com.marcosviniciusti.projetotcc.negocio;

import android.content.Context;
import android.util.Log;

import br.com.marcosviniciusti.projetotcc.persistencia.PDatabase;

public class NDatabase {

    // Atributos da biblioteca do Firebase.
    private PDatabase persistencia; // Declaração do banco de dados.

    // Atributos da classe.
    private String TAG = "NDatabase";

    public NDatabase() {
    }

    public NDatabase(Context context) {
        try {
            this.persistencia = new PDatabase(context);
        } catch (Exception error) {
            Log.e(TAG, "ERRO no construtor: "+error.getMessage(), error);
        }
    }

}
