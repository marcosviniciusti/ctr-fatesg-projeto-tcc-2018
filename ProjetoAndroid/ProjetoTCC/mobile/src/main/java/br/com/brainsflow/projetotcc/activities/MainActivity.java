package br.com.brainsflow.projetotcc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.com.brainsflow.projetotcc.R;
import br.com.brainsflow.projetotcc.entities.Definition;
import br.com.brainsflow.projetotcc.util.StableArrayAdapter;

public class MainActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_main);
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
        //
        listView = (ListView) findViewById(R.id.lvDefinition);
        //  Função do botão flutuante de adicionar as definições de controle remoto
        fab = (FloatingActionButton) findViewById(R.id.btnAdd);
    }

    private void createEvents() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Criamos a intent pois é através desse objeto que trafegamos dados de uma tela para outra.
                Intent intent = new Intent(view.getContext(), FormActivity.class);

                // Enviamos o intent para a outra tela.
                view.getContext().startActivity(intent);
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

    private void makeArrayRequest() {

    }

}