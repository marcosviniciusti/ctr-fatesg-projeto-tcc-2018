package br.com.brainsflow.projetoctr.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import br.com.brainsflow.projetoctr.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Atributos da biblioteca do firebase.
    private FirebaseAuth mAuth;
    private GoogleSignInClient pGoogleSignInClient;

    // Atributos da classe.
    private static final String TAG = "MainActivity";
    private FirebaseAnalytics mFirebaseAnalytics ;
    private final String URL_GET = "/home/marcos/";
    private final String URL_POST = "/home/marcos/";
    private final String URL_PUT = "/home/marcos/";
    private final String URL_DELETE = "/home/marcos/";
    private final String URL_REQUEST = "/home/marcos/";
    private FloatingActionButton btnAdd;
    private ListView listView;

    //  Referencias da UI.
    Toolbar toolbar;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //  Função que inicializa os atributos
        bindView();
        //  Função que cria eventos dos componentes
        createEvents();
        //  Função que carrega a ListView
        makeArrayRequest();
    }

    private void bindView() {
        mAuth = FirebaseAuth.getInstance();
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]
        pGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Obter a instância FirebaseAnalytics.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //  Obter a instância da listView da tela.
        listView = (ListView) findViewById(R.id.listViewDefinition);
        //  Obter a instância do botão flutuante da tela.
        btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.status_navigation_drawer_open, R.string.status_navigation_drawer_close);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
    }

    private void createEvents() {

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        btnAdd.setOnClickListener(new View.OnClickListener() {
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
        String id =  "btnAdd";
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
//            ArrayList<EDefinition> lista = new ArrayList<>();
//            StableArrayAdapter adapter = new StableArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1, lista);// Cria um object e seta os parametros no simpleAdapter
//            listView.setAdapter(adapter);// sets o adapter na listView
//        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_connect) {
            // Método de conectar com o arduino.
            return true;
        }
        if (id == R.id.action_disconnect) {
            // Método de desconectar do arduino
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        // Firebase sign out
        if (mAuth != null)
            mAuth.signOut();

        // Google sign out
        if (pGoogleSignInClient != null)
            pGoogleSignInClient.signOut().addOnCompleteListener(this,
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            finish();
                        }
                    });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.buttonSettings) {

        } else if (id == R.id.buttonSignOut) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}