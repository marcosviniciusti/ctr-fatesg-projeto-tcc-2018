package br.com.marcosviniciusti.projetotcc.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.marcosviniciusti.projetotcc.R;
import br.com.marcosviniciusti.projetotcc.entities.Equipment;
import br.com.marcosviniciusti.projetotcc.entities.EquipmentGroup;

public class FormGroupEquipmentActivity extends AppCompatActivity {

    //  Referencias da UI.
    private EditText txtNameEquipmentGroup;
    private Button btnSaveEquipmentGroup;
    private Button btnCancelEquipmentGroup;

    //  Atributos da classe.
    private String status;
    private List<EquipmentGroup> lista;
    private EquipmentGroup equipmentGroup;

    // Referenciando o banco de dados do Firebase.
    private FirebaseDatabase database;
    private DatabaseReference equipGroupRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_group_equipment);
        bindView();
        createEvents();
    }

    private void bindView(){
        // Referencia de instancia de componentes da interface.
        txtNameEquipmentGroup = (EditText) findViewById(R.id.txtNameEquipmentGroup);
        btnSaveEquipmentGroup = (Button) findViewById(R.id.btnSaveEquipmentGroup);
        btnCancelEquipmentGroup = (Button) findViewById(R.id.btnCancelEquipmentGroup);

        // Inicializa a aplicação Firebase.
        FirebaseApp.initializeApp(FormGroupEquipmentActivity.this);

        // Referencia a instância do banco de dados do Firebase.
        database = FirebaseDatabase.getInstance();

        // Referencia nós do banco de dados.
        equipGroupRef = database.getReference().child("listEquipmentGroup");

        equipmentGroup = (EquipmentGroup) getIntent().getBundleExtra("bundle")
                .getSerializable("object");

        if (equipmentGroup!=null) {
            status = "update";
            txtNameEquipmentGroup.setText(equipmentGroup.getName());
        } else {
            status = "insert";
        }
    }


    private void createEvents() {
        btnSaveEquipmentGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDefinition();
                finish();
            }
        });

        btnCancelEquipmentGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveDefinition() {
        //  Grava os dados inseridos pelo usuário nas váriaves
        String name = txtNameEquipmentGroup.getText().toString();

        if (status.equals("insert")) {
            equipmentGroup = new EquipmentGroup();
            equipmentGroup.setId(UUID.randomUUID().toString());
            equipmentGroup.setName(name);
            equipmentGroup.setListEquipment(new ArrayList<Equipment>());
            equipGroupRef.child(equipmentGroup.getId()).setValue(equipmentGroup);
        } else {
            equipGroupRef.child(equipmentGroup.getId()).child("name").setValue(name);
        }
    }
}