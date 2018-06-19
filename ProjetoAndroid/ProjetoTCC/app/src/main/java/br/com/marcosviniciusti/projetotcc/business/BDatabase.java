package br.com.marcosviniciusti.projetotcc.business;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.ValueEventListener;

import br.com.marcosviniciusti.projetotcc.persistence.PDatabase;

public class BDatabase {

    // Atributos da biblioteca do Firebase.
    private PDatabase persistencia; // Declaração do banco de dados.

    // Atributos da classe.
    private String TAG = "BDatabase";

    public BDatabase() {
    }

    public BDatabase(Context context) {
        try {
            this.persistencia = new PDatabase(context);
        } catch (Exception error) {
            Log.e(TAG, "ERRO no construtor: "+error.getMessage(), error);
        }
    }

}
