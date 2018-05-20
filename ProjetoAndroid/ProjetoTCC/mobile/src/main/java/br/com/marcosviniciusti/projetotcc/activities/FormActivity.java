package br.com.marcosviniciusti.projetotcc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.marcosviniciusti.projetotcc.R;
import br.com.marcosviniciusti.projetotcc.entities.Definicao;
import br.com.marcosviniciusti.projetotcc.entities.Equipamento;

public class FormActivity extends AppCompatActivity {

    //  Referencias da UI.
    private EditText txtNome;
    private Spinner spMarca;
    private Spinner spModelo;
    private Spinner spTipo;
    private Button btnSalvar;
    private Button btnCancelar;

    //  Atributos da classe.
    private Equipamento equipamento;
    private ArrayList<Definicao> lista;

    // Atributos da biblioteca do Firebase.
    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;
    // Referenciando o banco de dados do Firebase.
    private FirebaseDatabase database;
    private DatabaseReference ctrRef, defRef, remoteRef, groupRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        bindView();
        loadSpinner();
        createEvents();
    }

    private void bindView(){

        // Referencia de instancia de componentes da interface.
        txtNome = (EditText) findViewById(R.id.txtNome);
        spMarca = (Spinner) findViewById(R.id.spMarca);
        spModelo = (Spinner) findViewById(R.id.spModelo);
        spTipo = (Spinner) findViewById(R.id.spTipo);
        btnSalvar = (Button) findViewById(R.id.btnSave);
        btnCancelar = (Button) findViewById(R.id.btnCancel);

        equipamento = new Equipamento();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        lista = (ArrayList<Definicao>) bundle.getSerializable("lista");

        // Referencia a instancia de autenticação do Firebase.
        auth = FirebaseAuth.getInstance();
        // Configura o Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Referencia a instancia de autenticação do Firebase pela Google.
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        // Referencia a instância do banco de dados do Firebase.
        database = FirebaseDatabase.getInstance();

        // Referencia nós do banco de dados.
        ctrRef = database.getReference().child("tcc");
        defRef = ctrRef.child("definition");
        remoteRef = ctrRef.child("remote");
        groupRef = ctrRef.child("group");
    }

    public void loadSpinner() {
        // Aqui fazemos uma referencia aos arrays de dados que inserimos no arquivo "string.xml"
        ArrayAdapter<CharSequence> adapter_marca = ArrayAdapter.createFromResource(this, R.array.spinner_marca, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter_modelo = ArrayAdapter.createFromResource(this, R.array.spinner_modelo, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter_tipo = ArrayAdapter.createFromResource(this, R.array.spinner_tipo, android.R.layout.simple_spinner_item);

        // Aqui declaramos ao ArrayAdapter, qual o tipo de spinner iremos trabalhar, que no caso aqui é o "DropDownView".
        adapter_marca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_modelo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_tipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Aqui inserimos os dados que criamos no arquivo "sting.xml" que serão persistentes no spinner graças ao "ArrayAdapter".
        spMarca.setAdapter(adapter_marca);
        spModelo.setAdapter(adapter_modelo);
        spTipo.setAdapter(adapter_tipo);
    }

    private void createEvents() {
        // Aqui é a implementação do método ouvinte de evento de seleção no spinner dia
        spMarca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Aqui é a implementação do método ouvinte de evento de seleção no spinner mes
        spModelo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

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
        String marca = spMarca.getSelectedItem().toString();
        String modelo = spModelo.getSelectedItem().toString();
        String tipo = spTipo.getSelectedItem().toString();

        equipamento.setMarca(marca);
        equipamento.setModelo(modelo);
        equipamento.setTipoEquipamento(tipo);
        List<Equipamento> listaEquipamentos = new ArrayList<Equipamento>();
        listaEquipamentos.add(equipamento);

        Definicao definicao = new Definicao();
        definicao.setNome(nome);
        definicao.setEquipamentos(listaEquipamentos);

        lista.add(definicao);
    }
}