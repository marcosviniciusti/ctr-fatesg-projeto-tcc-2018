package br.com.marcosviniciusti.projetotcc.persistence;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.marcosviniciusti.projetotcc.entities.EquipmentGroup;

public class PDatabase {

    // Atributos da biblioteca do Firebase.
    private FirebaseAnalytics firebaseAnalytics; // Declaração do analytics.
    private FirebaseDatabase database; // Declaração do banco de dados.
    private DatabaseReference listDefRef, defRef; // Declaração de referencias do banco de dados.

    // Atributos da classe.
    private String TAG = "PDatabase";

    public PDatabase(Context context) {
        // Referencia a instância do Analytics do Firebase.
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        // Referencia a instância do banco de dados do Firebase.
        database = FirebaseDatabase.getInstance();
        // Referencia nós do banco de dados.
        listDefRef = database.getReference().child("listaDefinicao");
        defRef = listDefRef.child("definicao2");
    }

    private void loadDatabase() {

        // Ler o banco de dados.
        listDefRef.addValueEventListener(new ValueEventListener() {
            // Este método é chamado uma vez com o valor inicial e novamente
            // sempre que os dados neste local forem atualizados.
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                List<EquipmentGroup> value = dataSnapshot.getValue(EquipmentGroup.class);
//                loadListView(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read value of listDefRef: "+error.getMessage(), error.toException());
            }
        });

        // Ler o banco de dados
        defRef.addValueEventListener(new ValueEventListener() {
            // Este método é chamado uma vez com o valor inicial e novamente
            // sempre que os dados neste local forem atualizados.
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                EquipmentGroup value = dataSnapshot.getValue(EquipmentGroup.class);
                Log.d(TAG, "Definição nome: "+value.getNome());
                //loadListView(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read value of defRef: "+error.getMessage(), error.toException());
            }
        });
    }

    public void salvar() {

    }

    public void alterar() {

    }

    public void deletar() {

    }

    public void listar() {

    }

}
