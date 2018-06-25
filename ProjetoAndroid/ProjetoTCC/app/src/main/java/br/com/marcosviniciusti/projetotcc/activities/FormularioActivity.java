package br.com.marcosviniciusti.projetotcc.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.marcosviniciusti.projetotcc.R;
import br.com.marcosviniciusti.projetotcc.negocio.NEmissor;
import br.com.marcosviniciusti.projetotcc.negocio.NEquipamento;
import br.com.marcosviniciusti.projetotcc.negocio.NFirebaseAuth;
import br.com.marcosviniciusti.projetotcc.negocio.NGrupoEquipamento;
import br.com.marcosviniciusti.projetotcc.negocio.NMarca;
import br.com.marcosviniciusti.projetotcc.negocio.NModelo;
import br.com.marcosviniciusti.projetotcc.negocio.NUsuario;
import br.com.marcosviniciusti.projetotcc.entidade.EEmissor;
import br.com.marcosviniciusti.projetotcc.entidade.EEquipamento;
import br.com.marcosviniciusti.projetotcc.entidade.EGrupoEquipamento;
import br.com.marcosviniciusti.projetotcc.entidade.EMarca;
import br.com.marcosviniciusti.projetotcc.entidade.EModelo;
import br.com.marcosviniciusti.projetotcc.entidade.EUsuario;
import br.com.marcosviniciusti.projetotcc.util.EnumTipoEquipamento;
import br.com.marcosviniciusti.projetotcc.util.TipoEquipamentoAdapter;

public class FormularioActivity extends AppCompatActivity {

    //  Referencias da UI.
    private EditText edtNomeGrupoEquipamento;
    private TextView edtNomeEquipamento;
    private TextView edtEmailUsuario;
    private Spinner spTipo;
    private Spinner spMarca;
    private Spinner spModelo;
    private Spinner spEmissor;
    private Button btnSalvar;
    private Button btnCancelar;

    //  Atributos da classe.
    private static final String GRUPO_EQUIPAMENTO = "GRUPO_EQUIPAMENTO";
    private static final String EQUIPAMENTO = "EQUIPAMENTO";
    private String window;
    private EGrupoEquipamento grupoEquipamento;
    private EEquipamento equipamento;
    private List<EEquipamento> equipamentos;
    private List<EMarca> listaMarcas;
    private List<EModelo> listaModelos;
    private List<EEmissor> listaEmissor;
    private ArrayAdapter adapterTipo;
    private ArrayAdapter adapterMarca;
    private ArrayAdapter adapterModelo;
    private ArrayAdapter adapterEmissor;

    // Referenciando o banco de dados do Firebase.
    private NFirebaseAuth nFirebaseAuth;
    private NGrupoEquipamento nGrupoEquipamento;
    private NEquipamento nEquipamento;
    private NMarca nMarca;
    private NModelo nModelo;
    private NEmissor nEmissor;
    private NUsuario nUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);
        inicializar();
        criarEventos();
    }

    private void inicializar(){
        carregarIntent();// Carregar o intent que carrega dados que vem de outra tela.
        inicializarComponentesDeInterface();
        inicializarFirebase();
        criarEventos();
        if (window.equals(EQUIPAMENTO)) {
            pegarDoBanco();
            preencherSpinner();
        }
        carregarDados();
    }

    private void carregarIntent() {

        grupoEquipamento = (EGrupoEquipamento) getIntent().getBundleExtra("bundle")
                .getSerializable("objeto");

        window = getIntent().getBundleExtra("bundle").getString("window");
    }

    private void inicializarComponentesDeInterface() {

        edtNomeGrupoEquipamento = (EditText) findViewById(R.id.edtGrupoEquipamento);
        edtNomeEquipamento = findViewById(R.id.edtEquipamento);
        edtEmailUsuario = findViewById(R.id.edtEmailUsuario);
        spTipo = findViewById(R.id.spTipo);
        spMarca = findViewById(R.id.spMarca);
        spModelo = findViewById(R.id.spModelo);
        spEmissor = findViewById(R.id.spEmissor);
        btnSalvar = (Button) findViewById(R.id.btnSalvar);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);

        alterarVisibilidadeCampos();
    }

    private void alterarVisibilidadeCampos() {
        if (window.equals(GRUPO_EQUIPAMENTO)) {
            edtNomeEquipamento.setVisibility(View.GONE);
            spTipo.setVisibility(View.GONE);
            spMarca.setVisibility(View.GONE);
            spModelo.setVisibility(View.GONE);
            spEmissor.setVisibility(View.GONE);
            edtEmailUsuario.setVisibility(View.GONE);
        } else if (window.equals(EQUIPAMENTO)) {
            edtNomeGrupoEquipamento.setEnabled(false);
            edtEmailUsuario.setVisibility(View.GONE);
        } else {
            edtNomeGrupoEquipamento.setVisibility(View.GONE);
            edtNomeEquipamento.setVisibility(View.GONE);
            spTipo.setVisibility(View.GONE);
            spMarca.setVisibility(View.GONE);
            spModelo.setVisibility(View.GONE);
            spEmissor.setVisibility(View.GONE);
        }
    }

    private void inicializarFirebase() {
        nFirebaseAuth = new NFirebaseAuth(getString(R.string.default_web_client_id), this);
        if (window.equals(GRUPO_EQUIPAMENTO)) {
            nGrupoEquipamento = new NGrupoEquipamento();
        } else if (window.equals(EQUIPAMENTO)) {
            nEquipamento = new NEquipamento();
            nMarca = new NMarca();
            nModelo = new NModelo();
            nEmissor = new NEmissor();
        } else {
            nUsuario = new NUsuario();
        }
    }

    private void criarEventos() {
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
                if (window.equals(GRUPO_EQUIPAMENTO)) {
                    salvarGrupoEquipamento();
                } else if (window.equals(EQUIPAMENTO)) {
                    salvarEquipamento();
                } else {
                    salvarUsuario();
                }
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

    private void pegarDoBanco() {
        TipoEquipamentoAdapter adapterTipo = new TipoEquipamentoAdapter(this,
                android.R.layout.simple_spinner_item,
                Arrays.asList(EnumTipoEquipamento.values()));

        listaMarcas = new ArrayList<>();
        nMarca.listar(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    EMarca marca = child.getValue(EMarca.class);
                    listaMarcas.add(marca);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ArrayAdapter<EMarca> adapterMarca = new ArrayAdapter<EMarca>(this,
                android.R.layout.simple_spinner_item, listaMarcas);

        listaModelos = new ArrayList<>();
        nModelo.listar(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    EModelo modelo = child.getValue(EModelo.class);
                    listaModelos.add(modelo);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ArrayAdapter<EModelo> adapterModelo = new ArrayAdapter<EModelo>(this,
                android.R.layout.simple_spinner_item, listaModelos);

        listaEmissor = new ArrayList<>();
        nEmissor.listar(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    EEmissor emissor = child.getValue(EEmissor.class);
                    listaEmissor.add(emissor);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ArrayAdapter<EEmissor> adapterEmissor = new ArrayAdapter<EEmissor>(this,
                android.R.layout.simple_spinner_item, listaEmissor);

        // Aqui declaramos ao ArrayAdapter, qual o tipo de spinner iremos trabalhar,
        // que no caso aqui é o "DropDownView".
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterMarca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterModelo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterEmissor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Aqui inserimos os dados que criamos no arquivo "sting.xml" que serão persistentes no
        // spinner graças ao "ArrayAdapter".
        spTipo.setAdapter(adapterTipo);
        spMarca.setAdapter(adapterMarca);
        spModelo.setAdapter(adapterModelo);
        spEmissor.setAdapter(adapterEmissor);
    }

    private void preencherSpinner() {
        // Aqui fazemos uma referencia aos arrays de dados que inserimos no arquivo "string.xml"
        adapterTipo = ArrayAdapter.createFromResource
                (this, R.array.spinner_tipo, android.R.layout.simple_spinner_item);
        adapterMarca = ArrayAdapter.createFromResource
                (this, R.array.spinner_marca, android.R.layout.simple_spinner_item);
        adapterModelo = ArrayAdapter.createFromResource
                (this, R.array.spinner_modelo, android.R.layout.simple_spinner_item);
        adapterEmissor = ArrayAdapter.createFromResource
                (this, R.array.spinner_emissor, android.R.layout.simple_spinner_item);

        // Aqui declaramos ao ArrayAdapter, qual o tipo de spinner iremos trabalhar,
        // que no caso aqui é o "DropDownView".
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterMarca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterModelo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterEmissor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Aqui inserimos os dados que criamos no arquivo "sting.xml" que serão persistentes no
        // spinner graças ao "ArrayAdapter".
        spTipo.setAdapter(adapterTipo);
        spMarca.setAdapter(adapterMarca);
        spModelo.setAdapter(adapterModelo);
        spModelo.setAdapter(adapterModelo);
        spEmissor.setAdapter(adapterEmissor);
    }

    private void carregarDados() {
        if (grupoEquipamento != null) {
            edtNomeGrupoEquipamento.setText(grupoEquipamento.getNome());

            if (window.equals(EQUIPAMENTO)) {
                int position = getIntent().getBundleExtra("bundle")
                        .getInt("posicao");
                equipamentos = grupoEquipamento.getEquipamentos();
                if (position >= 0) {
                    equipamento = equipamentos.get(position);
                    edtNomeEquipamento.setText(equipamento.getNome());
                    for (int i = 0; i< adapterTipo.getCount(); i++) {
                        if (equipamento.getModelo().getTipo().toString()
                                .equals(adapterTipo.getItem(i).toString())) {
                            spTipo.setSelection(i);
                            break;
                        }
                    }

                    for (int i = 0; i< adapterMarca.getCount(); i++) {
                        if (equipamento.getModelo().getMarca().toString()
                                .equals(adapterMarca.getItem(i).toString())){
                            spMarca.setSelection(i);
                            break;
                        }
                    }

                    for (int i = 0; i< adapterModelo.getCount(); i++) {
                        if (equipamento.getModelo().toString()
                                .equals(adapterModelo.getItem(i).toString())) {
                            spModelo.setSelection(i);
                            break;
                        }
                    }

                    for (int i = 0; i< adapterEmissor.getCount(); i++) {
                        if (equipamento.getEmissor().toString()
                                .equals(adapterEmissor.getItem(i).toString())) {
                            spEmissor.setSelection(i);
                            break;
                        }
                    }
                } else {
                    equipamento = new EEquipamento();
                }
            }
        } else {
            grupoEquipamento = new EGrupoEquipamento();
        }
    }

    private void salvarGrupoEquipamento() {
        //  Grava os dados inseridos pelo usuário nas váriaves.
        String nome = edtNomeGrupoEquipamento.getText().toString();

        grupoEquipamento.setNome(nome);
        grupoEquipamento.setIdUsuarioDono(nFirebaseAuth.getUser().getUid());
        nGrupoEquipamento.salvar(grupoEquipamento);
    }

    private void salvarEquipamento() {
        //  Grava os dados inseridos pelo usuário nas váriaves.
        String nome = edtNomeEquipamento.getText().toString();
        EnumTipoEquipamento tipo = (EnumTipoEquipamento) spTipo.getSelectedItem();
        String marca = spMarca.getSelectedItem().toString();
        String modelo = spModelo.getSelectedItem().toString();

        equipamento.setNome(nome);
        EModelo modeloEquipamento = new EModelo();
        modeloEquipamento.setNome(modelo);
        EMarca eMarca = new EMarca();
        eMarca.setNome(marca);
        modeloEquipamento.setMarca(eMarca);
        modeloEquipamento.setTipo(tipo);
        equipamento.setModelo(modeloEquipamento);
        EEmissor emissor = new EEmissor();
        emissor.setNome("emissor 1");
        equipamento.setEmissor(emissor);
        equipamento.setIdGrupoEquipamento(grupoEquipamento.getId());
        nEquipamento.salvar(equipamento);
    }

    private void salvarUsuario() {
        //  Grava os dados inseridos pelo usuário nas váriaves.
        final String email = edtEmailUsuario.getText().toString();

        nUsuario.listar(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    EUsuario usuario = child.getValue(EUsuario.class);
                    if (email.equals(usuario.getEmail())) {

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        nEquipamento.salvar(equipamento);
    }
}