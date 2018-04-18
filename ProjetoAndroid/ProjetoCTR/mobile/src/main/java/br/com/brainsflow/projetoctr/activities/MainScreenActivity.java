package br.com.brainsflow.projetoctr.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import br.com.brainsflow.projetoctr.R;
import br.com.brainsflow.projetoctr.entities.Definition;
//import br.com.brainsflow.projetoctr.util.StableArrayAdapter;


public class MainScreenActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics ;
    private final String URL_GET = "/home/marcos/";
    private final String URL_POST = "/home/marcos/";
    private final String URL_PUT = "/home/marcos/";
    private final String URL_DELETE = "/home/marcos/";
    private final String URL_REQUEST = "/home/marcos/";
    private FloatingActionButton fab;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //  Função que inicializa os atributos
        bindView();
        //  Função que cria eventos dos componentes
        createEvents();
        //  Função que carrega a ListView
        makeArrayRequest();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void bindView() {
        // Obter a instância FirebaseAnalytics.
        mFirebaseAnalytics = FirebaseAnalytics . getInstance ( this );
        //  Obter a instância da listView da tela.
        listView = (ListView) findViewById(R.id.lvDefinition);
        //  Obter a instância do botão flutuante da tela.
        fab = (FloatingActionButton) findViewById(R.id.btnAdd);
    }

    private void createEvents() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                monitorarBotãoFlutuante();

                // Criamos a intent pois é através desse objeto que trafegamos dados de uma tela para outra.
                Intent intent = new Intent(view.getContext(), FormActivity.class);

                // Enviamos o intent para a outra tela.
                view.getContext().startActivity(intent);
                //makeArrayRequest();
            }
        });

        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    private void monitorarBotãoFlutuante() {
        String id =  "fab";
        String name = "adicionar definição";

        // [START FloatingActionButton_view_event]
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "button");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        // [END FloatingActionButton_view_event]
    }

    private void makeArrayRequest() {
//        if (true) {
//            ArrayList<Definition> lista = new ArrayList<>();
//            StableArrayAdapter adapter = new StableArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1, lista);// Cria um object e seta os parametros no simpleAdapter
//            listView.setAdapter(adapter);// sets o adapter na listView
//        }
    }

}