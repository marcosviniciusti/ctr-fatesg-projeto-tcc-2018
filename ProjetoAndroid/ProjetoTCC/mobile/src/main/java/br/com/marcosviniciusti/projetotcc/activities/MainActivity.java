package br.com.marcosviniciusti.projetotcc.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.marcosviniciusti.projetotcc.R;
import br.com.marcosviniciusti.projetotcc.entities.EquipmentGroup;

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
    private MenuItem btnConnect;
    private MenuItem btnDisconnect;
    private FloatingActionButton btnAdd;
    private ListView listView;

    // Atributos da classe.
    private static final String TAG = "MainActivity";
    private final String URL_GET = "/home/marcos/";
    private final String URL_POST = "/home/marcos/";
    private final String URL_PUT = "/home/marcos/";
    private final String URL_DELETE = "/home/marcos/";
    private final String URL_REQUEST = "/home/marcos/";
    private List<EquipmentGroup> lista;
    private List<HashMap<String, String>> listaHashMap;

    // Atributos da biblioteca do Firebase.
    private FirebaseAuth auth; // Declaração do sistema de autenticação.
    private GoogleSignInClient googleSignInClient; // Declaração da autenticação do Google.
    private FirebaseAnalytics firebaseAnalytics; // Declaração do analytics.
    private FirebaseDatabase database; // Declaração do banco de dados.
    private DatabaseReference listDefRef, defRef; // Declaração de referencias do banco de dados.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //  Função que instancia os atributos da interface e atributos da classe.
        bindView();
        //  Função que cria eventos dos componentes.
        createEvents();
        // Função que carrega dados e insere nos componentes.
        loadComponentsNav();
    }

    private void bindView() {
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnConnect = (MenuItem) findViewById(R.id.btnConnect);
        btnDisconnect = toolbar.getMenu().findItem(R.id.btnDisconnect);
        if ((MenuItem) findViewById(R.id.btnConnect)==null) Log.d(TAG, "Atributo btnConnect é null");
        else btnConnect.setEnabled(true);
        /*btnDisconnect.setEnabled(false);*/
        setSupportActionBar(toolbar);

        // Referencia instancias da interface.
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.status_navigation_drawer_open, R.string.status_navigation_drawer_close);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        imageView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView);
        txvName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txvName);
        txvEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txvEmail);
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
        listDefRef = database.getReference().child("listaDefinicao");
        defRef = listDefRef.child("definicao2");

        lista = new ArrayList<EquipmentGroup>();
        listaHashMap = new ArrayList<HashMap<String, String>>();
    }

    private void createEvents() {

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monitorarBotaoFlutuante();
                // Criamos a intent pois é através desse objeto que trafegamos dados de uma tela para outra.
                Intent intent = new Intent(view.getContext(), FormActivity.class);
                // Criamos um pacote para enviar objetos dessa classe para outra.
                Bundle bundle = new Bundle();
                bundle.putSerializable("lista", (Serializable) lista);
                intent.putExtra("bundle", bundle);
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

    private void loadComponentsNav() {

        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            if (user.getEmail() != null || !user.getEmail().isEmpty()) {
                Log.d(TAG, "URL: " + user.getPhotoUrl());
                // Fazemos o download da imagem na rede.
                Bitmap bitmap = getImageBitmap(user.getPhotoUrl().toString());
                // Carregamos a imagem no elemento ImageView.
                imageView.setImageBitmap(bitmap);

                // Carregamos o nome e e-mail do usuário nos TextField's.
                txvName.setText(user.getDisplayName());
                txvEmail.setText(user.getEmail());
            }
        }
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
            Log.e(TAG, "Error: "+erro.getMessage(), erro.getCause());
        }
        return bm;
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*if (btnConnect.isEnabled()) {
            btnDisconnect.setEnabled(false);
        } else {
            btnDisconnect.setEnabled(true);
        }*/
        // Função que carrega dados do banco de dados para a listview.
        loadDatabase();
    }

    private void loadDatabase() {

        // Ler o banco de dados.
        listDefRef.addValueEventListener(new ValueEventListener() {
            // Este método é chamado uma vez com o valor inicial e novamente
            // sempre que os dados neste local forem atualizados.
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                List<EquipmentGroup> value = dataSnapshot.getValue(EquipmentGroup.class);
//                loadListView(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read value of listDefRef: "+error.getMessage(), error.toException());
            }
        });

        // Ler o banco de dados
        defRef.addValueEventListener(new ValueEventListener() {
            // Este método é chamado uma vez com o valor inicial e novamente
            // sempre que os dados neste local forem atualizados.
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                EquipmentGroup value = dataSnapshot.getValue(EquipmentGroup.class);
                Log.d(TAG, "Definição nome: "+value.getNome());
                loadListView(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read value of defRef: "+error.getMessage(), error.toException());
            }
        });
    }

    private void loadListView(EquipmentGroup equipmentGroup) {
        try {
            lista.add(equipmentGroup);
            Log.d(TAG, "Definição: "+ equipmentGroup.getNome());
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("nome", equipmentGroup.getNome());
            listaHashMap.add(hashMap);
            String[] from= {"nome"};
            int[] to = {R.id.txName};
            SimpleAdapter adapter = new SimpleAdapter(this, listaHashMap, R.layout.content_listview, from, to);
            listView.setAdapter(adapter);
        } catch (Exception e) {
            Log.e(TAG, "ERRO no loadListView: "+e.getMessage(), e.getCause());
        }
    }

    private void monitorarBotaoFlutuante() {
        try {
            String id =  "btnAdd";
            String name = "adicionar definição";

            // [START FloatingActionButton_view_event]
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "button");
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            // [END FloatingActionButton_view_event]
        } catch (Exception e) {
            Log.e(TAG, "ERRO no monitorarBotaoFlutuante: "+ e.getMessage(), e.getCause());
        }
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
        if (id == R.id.btnConnect) {
            // Método de conectar com o arduino.
            return true;
        }
        if (id == R.id.btnDisconnect) {
            // Método de desconectar do arduino
            return true;
        }
        if (id == R.id.btnControlRemote) {
            startActivity(createIntent(this, RemoteControlActivity.class, null));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Intent createIntent(Context context, Class aClass, Object object) {
        Intent intent = new Intent(context, aClass);
        Bundle bundle = new Bundle();
        bundle.putSerializable("objeto", (Serializable) object);
        intent.putExtra("bundle", bundle);
        return intent;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.btnSettings) {
            Toast.makeText(this, "SETTINGS!!!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.btnSignOut) {
            signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
}