//package br.com.marcosviniciusti.projetotcc.teste;
//
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.Spinner;
//import android.widget.TextView;
//
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.util.Iterator;
//import java.util.List;
//import java.util.UUID;
//
//import br.com.marcosviniciusti.projetotcc.R;
//
//public class FormEquipmentActivity extends AppCompatActivity {
//
//    //  Referencias da UI.
//    private TextView txvNameEquipment;
//    private Spinner spType;
//    private Spinner spLogo;
//    private Spinner spModel;
//    private Button btnSave;
//    private Button btnCancel;
//
//    //  Atributos da classe.
//    private String status;
//    private EquipmentGroup equipmentGroup;
//    private Equipment equipment;
//    private List<EquipmentGroup> listEquipmentGroup;
//    private Iterator<EquipmentGroup> iteratorEquipmentGroup;
//    private List<Equipment> listEquipment;
//    private Iterator<Equipment> iteratorEquipment;
//
//    // Atributos da biblioteca do Firebase.
//    private FirebaseAuth auth;
//    private GoogleSignInClient googleSignInClient;
//
//    // Referenciando o banco de dados do Firebase.
//    private FirebaseDatabase database;
//    private DatabaseReference equipGroupRef, equipRef;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_form_equipment);
//        bindView();
//        createEvents();
//    }
//
//    private void bindView(){
//        // Referencia de instancia de componentes da interface.
//        inicializeComponentsInterface();
//        inicializeFirebase();
//        loadIntent();
//    }
//
//    private void loadIntent() {
//        equipmentGroup =  (EquipmentGroup) getIntent().getBundleExtra("bundle")
//                .getSerializable("object");
//        txvNameEquipment.setText(equipmentGroup.getName());
//
////        if (equipmentGroup.getListaEquipmento() != null &&
////                equipmentGroup.getListaEquipmento().hasNext()) {
////            int position = getIntent().getBundleExtra("bundle").getInt("position");
////            iteratorEquipment = equipmentGroup.getListaEquipmento();
////            while (iteratorEquipment.hasNext()) {
////                listEquipment.add(iteratorEquipment.next());
////            }
////            equipment = listEquipment.get(position);
////            if (equipment != null){
////                status = "update";
////            }
////        } else {
////            status = "insert";
////        }
//
//        if (equipmentGroup.getListEquipment() != null &&
//                !equipmentGroup.getListEquipment().isEmpty()) {
//            int position = getIntent().getBundleExtra("bundle").getInt("position");
//            listEquipment = (List<Equipment>) equipmentGroup.getListEquipment();
//            equipment = listEquipment.get(position);
//            if (equipment != null){
//                status = "update";
//            }
//        } else {
//            status = "insert";
//        }
//    }
//
//    private void inicializeFirebase() {
//        // Inicializa a aplicação Firebase.
//        FirebaseApp.initializeApp(FormEquipmentActivity.this);
//
//        // Referencia a instância do banco de dados do Firebase.
//        database = FirebaseDatabase.getInstance();
//
//        loadSpinner();
//
//        // Referencia nós do banco de dados.
//        equipGroupRef = database.getReference().child("listEquipmentGroup");
//    }
//
//    private void inicializeComponentsInterface() {
//        txvNameEquipment = (TextView) findViewById(R.id.txvNameEquipmentGroup);
//        spType = (Spinner) findViewById(R.id.spType);
//        spLogo = (Spinner) findViewById(R.id.spLogo);
//        spModel = (Spinner) findViewById(R.id.spModel);
//        btnSave = (Button) findViewById(R.id.btnSaveEquipment);
//        btnCancel = (Button) findViewById(R.id.btnCancelEquipment);
//    }
//
//    public void loadSpinner() {
//        // Aqui fazemos uma referencia aos arrays de dados que inserimos no arquivo "string.xml"
//        ArrayAdapter<CharSequence> adapter_marca = ArrayAdapter.createFromResource
//                (this, R.array.spinner_marca, android.R.layout.simple_spinner_item);
//        ArrayAdapter<CharSequence> adapter_modelo = ArrayAdapter.createFromResource
//                (this, R.array.spinner_modelo, android.R.layout.simple_spinner_item);
//        ArrayAdapter<CharSequence> adapter_tipo = ArrayAdapter.createFromResource
//                (this, R.array.spinner_tipo, android.R.layout.simple_spinner_item);
//
//        // Aqui declaramos ao ArrayAdapter, qual o tipo de spinner iremos trabalhar,
//        // que no caso aqui é o "DropDownView".
//        adapter_marca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        adapter_modelo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        adapter_tipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // Aqui inserimos os dados que criamos no arquivo "sting.xml" que serão persistentes no
//        // spinner graças ao "ArrayAdapter".
//        spLogo.setAdapter(adapter_marca);
//        spModel.setAdapter(adapter_modelo);
//        spType.setAdapter(adapter_tipo);
//
//        if (equipmentGroup != null && equipment != null) {
//            for (int i=0; i<adapter_marca.getCount(); i++) {
//                if (equipment.getMarca().equals(adapter_marca.getItem(i).toString())){
//                    spLogo.setPromptId(i);
//                }
//            }
//            for (int i=0; i<adapter_modelo.getCount(); i++) {
//                if (equipment.getModelo().equals(adapter_modelo.getItem(i).toString())) {
//                    spModel.setPromptId(i);
//                }
//            }
//            for (int i=0; i<adapter_tipo.getCount(); i++) {
//                if (equipment.getTipoEquipamento().equals(adapter_tipo.getItem(i).toString())) {
//                    spType.setPromptId(i);
//                }
//            }
//        }
//    }
//
//    private void createEvents() {
//        // Aqui é a implementação do método ouvinte de evento de seleção no spinner dia
//        spLogo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//
//        // Aqui é a implementação do método ouvinte de evento de seleção no spinner mes
//        spModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//
//        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                saveDefinition();
//                finish();
//            }
//        });
//
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//    }
//
//    private void saveDefinition() {
//
//        //  Grava os dados inseridos pelo usuário nas váriaves
//        String tipo = spType.getSelectedItem().toString();
//        String marca = spLogo.getSelectedItem().toString();
//        String modelo = spModel.getSelectedItem().toString();
//
//        if (status.equals("insert")) {
//            equipment = new Equipment();
//            equipment.setId(UUID.randomUUID().toString());
//        }
//
//        equipment.setTipoEquipamento(tipo);
//        equipment.setMarca(marca);
//        equipment.setModelo(modelo);
//        equipGroupRef.child(equipmentGroup.getId()).child("listEquipment")
//                .child(equipment.getId()).setValue(equipment);
//    }
//}