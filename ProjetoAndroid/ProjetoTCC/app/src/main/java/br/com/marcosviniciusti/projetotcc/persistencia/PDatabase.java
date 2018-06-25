package br.com.marcosviniciusti.projetotcc.persistencia;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class PDatabase {

    // Atributos da biblioteca do Firebase.
    private FirebaseDatabase database; // Declaração do banco de dados.

    // Atributos da classe.
    private String TAG = "PDatabase";

    public PDatabase() {
    }

    public PDatabase(Context context) {
        // Inicializa a aplicação Firebase.
        FirebaseApp.initializeApp(context);

        // Referencia a instância do banco de dados do Firebase.
        database = FirebaseDatabase.getInstance();

        // Permiti salvar, alterar arquivos em nuvem e também dentro do app.
        database.setPersistenceEnabled(true);
    }
}
