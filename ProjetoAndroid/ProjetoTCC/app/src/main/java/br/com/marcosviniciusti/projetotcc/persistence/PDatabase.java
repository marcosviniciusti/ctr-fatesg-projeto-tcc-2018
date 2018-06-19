package br.com.marcosviniciusti.projetotcc.persistence;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import br.com.marcosviniciusti.projetotcc.entities.EUsuario;

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
