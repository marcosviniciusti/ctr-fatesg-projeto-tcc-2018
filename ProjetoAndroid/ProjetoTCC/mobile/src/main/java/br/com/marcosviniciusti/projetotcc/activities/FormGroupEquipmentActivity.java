package br.com.marcosviniciusti.projetotcc.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import br.com.marcosviniciusti.projetotcc.R;
import br.com.marcosviniciusti.projetotcc.business.BAuth;
import br.com.marcosviniciusti.projetotcc.entities.EquipmentGroup;

public class FormGroupEquipmentActivity extends AppCompatActivity {

    //  Referencias da UI.
    private EditText txtNome;
    private Button btnSalvar;
    private Button btnCancelar;

    //  Atributos da classe.
    private List<EquipmentGroup> lista;
    private EquipmentGroup equipmentGroup;
    private BAuth bAuth;

    // Referenciando o banco de dados do Firebase.
    private FirebaseDatabase database;
    private DatabaseReference groupEquipRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        bindView();
        createEvents();
    }

    private void bindView(){
        // Referencia de instancia de componentes da interface.
        txtNome = (EditText) findViewById(R.id.txtNome);
        btnSalvar = (Button) findViewById(R.id.btnSave);
        btnCancelar = (Button) findViewById(R.id.btnCancel);

        // Referencia a instancia de autenticação do Firebase.
        bAuth = new BAuth(getString(R.string.default_web_client_id), this);

        // Referencia a instância do banco de dados do Firebase.
        database = FirebaseDatabase.getInstance();

        // Referencia nós do banco de dados.
        groupEquipRef = database.getReference().child("grupoEquipamentos");

        lista = (List<EquipmentGroup>) getIntent().getBundleExtra("bundle")
                .getSerializable("object");

//        equipmentGroup = (EquipmentGroup) getIntent().getBundleExtra("bundle")
//                .getSerializable("object");
//
//        if (equipmentGroup!=null) txtNome.setText(equipmentGroup.getNome());
    }


    private void createEvents() {
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDefinition();
                finish();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveDefinition() {
        //  Grava os dados inseridos pelo usuário nas váriaves
        String nome = txtNome.getText().toString();

        groupEquipRef.setValue(nome);
    }
}