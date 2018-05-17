package br.com.brainsflow.projetoctr.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import br.com.brainsflow.projetoctr.R;
import br.com.brainsflow.projetoctr.entities.Definicao;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //  Referencias da UI.
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private ImageView imageView;
    private TextView txvName;
    private TextView txvEmail;
    private FloatingActionButton btnAdd;
    private ListView listView;

    // Atributos da classe.
    private static final String TAG = "MainActivity";
    private final String URL_GET = "/home/marcos/";
    private final String URL_POST = "/home/marcos/";
    private final String URL_PUT = "/home/marcos/";
    private final String URL_DELETE = "/home/marcos/";
    private final String URL_REQUEST = "/home/marcos/";

    // Atributos da biblioteca do Firebase.
    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAnalytics firebaseAnalytics;
    // Referenciando o banco de dados do Firebase.
    FirebaseDatabase database;
    DatabaseReference ctrRef, defRef, remoteRef, groupRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //  Função que instancia os atributos da interface e atributos da classe.
        bindView();
        //  Função que cria eventos dos componentes
        createEvents();
        //  Função que carrega a ListView
        makeArrayRequest();
    }

    private void bindView() {
        // Referencia instancias da interface.
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.status_navigation_drawer_open, R.string.status_navigation_drawer_close);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        imageView = (ImageView) findViewById(R.id.imageView);
        txvName = (TextView) findViewById(R.id.txvName);
        txvEmail = (TextView) findViewById(R.id.txvEmail);
        listView = (ListView) findViewById(R.id.listView);
        btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);

        // Referencia a instancia de autenticação do Firebase.
        auth = FirebaseAuth.getInstance();
        // Configura o Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Referencia a instancia de autenticação do Firebase pela Google.
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        // Referencia a instância do Analytics do Firebase.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        // Referencia a instância do banco de dados do Firebase.
        database = FirebaseDatabase.getInstance();

        // Referencia nós do banco de dados.
        ctrRef = database.getReference().child("ctr");
        defRef = ctrRef.child("definition");
        remoteRef = ctrRef.child("remote");
        groupRef = ctrRef.child("group");
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

        // Ler o banco de dados
        ctrRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Este método é chamado uma vez com o valor inicial e novamente
                // sempre que os dados neste local forem atualizados.
                //String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + "CTR");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        // Ler o banco de dados
        defRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Este método é chamado uma vez com o valor inicial e novamente
                // sempre que os dados neste local forem atualizados.
                Definicao value = dataSnapshot.getValue(Definicao.class);
                Log.d(TAG, "Value is: " + "Definicao");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        // Ler o banco de dados
        remoteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Este método é chamado uma vez com o valor inicial e novamente
                // sempre que os dados neste local forem atualizados.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + "remote");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        // Ler o banco de dados
        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Este método é chamado uma vez com o valor inicial e novamente
                // sempre que os dados neste local forem atualizados.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + "Group");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void loadData() {

        FirebaseUser user = auth.getCurrentUser();

        // Carregamos a imagem no elemento ImageView.
        // Aqui pegamos a referencia da imagem em string e a transformamos em uma referencia para int
        int imageResource = R.drawable.firebase_lockup_400;
        // Aqui pega a imagem e trás para tela referenciada
        Drawable res = ContextCompat.getDrawable(getApplicationContext(), imageResource);

        // Carregamos a imagem no elemento.
        Log.w(TAG, "URL: "+user.getPhotoUrl());
        Bitmap bitmap = getImageBitmap(user.getPhotoUrl().toString());
        imageView.setImageBitmap(bitmap);

        // Carregamos o nome e e-mail do usuário no TextField.
        txvName.setText(user.getDisplayName());
        txvEmail.setText(user.getEmail());
    }

    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e(TAG, "Error getting bitmap: "+e.getMessage(), e);
        } catch (Exception erro) {
            Log.e(TAG, "Error: "+erro.getMessage(), erro);
        }
        return bm;
    }

    private void monitorarBotãoFlutuante() {
        String id =  "btnAdd";
        String name = "adicionar definição";

        // [START FloatingActionButton_view_event]
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "button");
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        // [END FloatingActionButton_view_event]
    }

    private void makeArrayRequest() {
//        if (true) {
//            ArrayList<Definicao> lista = new ArrayList<>();
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
            if (id == R.id.buttonConnect) {
            // Método de conectar com o arduino.
            return true;
        }
        if (id == R.id.buttonDisconnect) {
            // Método de desconectar do arduino
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        // Firebase sign out
        if (auth != null)
            auth.signOut();

        // Google sign out
        if (googleSignInClient != null)
            googleSignInClient.signOut().addOnCompleteListener(this,
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