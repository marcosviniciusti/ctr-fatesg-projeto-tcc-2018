package br.com.brainsflow.projetoctr.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import br.com.brainsflow.projetoctr.R;
import br.com.brainsflow.projetoctr.entities.EDefinition;
//import br.com.brainsflow.projetoctr.util.StableArrayAdapter;

public class FormActivity extends AppCompatActivity {

    private EditText txtNome;
    private Spinner spArcondicionado;
    private Spinner spDatashow;
    private Spinner spDatashowOrientation;
    private Spinner spDatashowResolution;
    private Spinner spComputer;
    private Button btnSalvar;
    private Button btnCancelar;
    private EDefinition definicao;
    private ArrayList<String> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        bindView();
        loadSpinner();
        createEvents();
    }

    private void bindView(){
        txtNome = (EditText) findViewById(R.id.txtName);
        spArcondicionado = (Spinner) findViewById(R.id.spArcondicionado);
        spDatashow = (Spinner) findViewById(R.id.spDatashow);
        spDatashowOrientation = (Spinner) findViewById(R.id.spDatashowOrientation);
        spDatashowResolution = (Spinner) findViewById(R.id.spDatashowResolution);
        spComputer = (Spinner) findViewById(R.id.spComputer);
        btnSalvar = (Button) findViewById(R.id.btnSave);
        btnCancelar = (Button) findViewById(R.id.btnCancel);
        definicao = new EDefinition();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        lista = bundle.getStringArrayList("listaString");
    }

    public void loadSpinner() {
        // Aqui fazemos uma referencia aos arrays de dados que inserimos no arquivo "string.xml"
        ArrayAdapter<CharSequence> adapter_ar = ArrayAdapter.createFromResource(this, R.array.spinner_ar, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter_datashow = ArrayAdapter.createFromResource(this, R.array.spinner_datashow, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter_datashow_orientation = ArrayAdapter.createFromResource(this, R.array.spinner_datashow_orientation, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter_datashow_resolutation = ArrayAdapter.createFromResource(this, R.array.spinner_datashow_resolution, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter_computer = ArrayAdapter.createFromResource(this, R.array.spinner_computer, android.R.layout.simple_spinner_item);

        // Aqui declaramos ao ArrayAdapter, qual o tipo de spinner iremos trabalhar, que no caso aqui é o "DropDownView".
        adapter_ar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_datashow.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_datashow_orientation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_datashow_resolutation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_computer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Aqui inserimos os dados que criamos no arquivo "sting.xml" que serão persistentes no spinner graças ao "ArrayAdapter".
        spArcondicionado.setAdapter(adapter_ar);
        spDatashow.setAdapter(adapter_datashow);
        spDatashowOrientation.setAdapter(adapter_datashow_orientation);
        spDatashowResolution.setAdapter(adapter_datashow_resolutation);
        spComputer.setAdapter(adapter_computer);
    }

    private void createEvents() {
        // Aqui é a implementação do método ouvinte de evento de seleção no spinner dia
        spArcondicionado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Aqui é a implementação do método ouvinte de evento de seleção no spinner mes
        spDatashow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spDatashowOrientation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spDatashowResolution.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spComputer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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
        String arCondicionado = spArcondicionado.getSelectedItem().toString();
        String datashow = spDatashow.getSelectedItem().toString();
        String datashowOrientation = spDatashowOrientation.getSelectedItem().toString();
        String datashowResolution = spDatashowResolution.getSelectedItem().toString();
        String computer = spComputer.getSelectedItem().toString();

        lista.add(nome);
        lista.add(arCondicionado);
        lista.add(datashow);
        lista.add(datashowOrientation);
        lista.add(datashowResolution);
        lista.add(computer);

        //  Insere os dados no objeto EDefinition
        /*definicao.setName(nome);
        definicao.setDatashow(datashow);
        definicao.setDatashowOrientation(datashowOrientation);
        definicao.setDatashowResolution(datashowResolution);
        definicao.setComputer(computer);*/

        // Adiciona o objeto na listView
        /*ArrayList<EDefinition> lista = new ArrayList<>();
        lista.add(definicao);*/

        /*
        ArrayList<HashMap> lista = new ArrayList<>();
        if(true) {
            HashMap<String,String> hashMap = new HashMap<>();//   Cria um hashmap para armazenar o dado em par de chaves valor
            hashMap.put("temperatura", definicao.getTemperaturaAr());
            hashMap.put("datashow", definicao.getDatashow());
            hashMap.put("orientation", definicao.getDatashowOrientation());
            hashMap.put("resolution", definicao.getDatashowResolution());
            hashMap.put("computer", definicao.getComputer());
            lista.add(hashMap);//add o hashmap no arrayList
        }
        SimpleAdapter adapter = new Adapter();
        */

        //StableArrayAdapter adapter = new StableArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,lista);// Cria um object e seta os parametros no simpleAdapter

        //  Grava o objeto no dispositivo
        /*DefinitionDao definition = new DefinitionDao();
        ArrayList<EDefinition> lista = new ArrayList<>();
        lista.add(definicao);
        StableArrayAdapter adapter = new StableArrayAdapter
                (this.getApplicationContext(), R.layout.content_listview,lista);*/// Cria um object e seta os parametros no adapter

        //lvDefinition.setAdapter(adapter);// sets o adapter na listView
    }
}