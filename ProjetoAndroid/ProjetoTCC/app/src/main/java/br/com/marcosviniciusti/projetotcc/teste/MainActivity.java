package br.com.marcosviniciusti.projetotcc.teste;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.marcosviniciusti.projetotcc.R;
import br.com.marcosviniciusti.projetotcc.activities.BaseActivity;
import br.com.marcosviniciusti.projetotcc.business.BAuth;
import br.com.marcosviniciusti.projetotcc.entities.Equipment;
import br.com.marcosviniciusti.projetotcc.entities.EquipmentGroup;
import br.com.marcosviniciusti.projetotcc.util.EquipmentAdapter;
import br.com.marcosviniciusti.projetotcc.util.EquipmentGroupAdapter;


/**
 * Uma tela de bAuth que oferece bAuth via email / senha.
 */
public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Referencias da UI.
    private Toolbar toolbar;
    private FloatingActionButton btnAdd;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private ImageView imageView;
    private TextView txvName;
    private TextView txvEmail;
    private ListView listView;

    // Atributos da classe.
    private static final String TAG = "AuthActivity";
    private static final String EQUIPMENT_GROUP = "EQUIPMENT_GROUP";
    private static final String EQUIPMENT = "EQUIPMENT";
    private String window;
    private BAuth bAuth;
    private List<EquipmentGroup> listEquipmentGroup;
    private List<Equipment> listEquipments;
    private EquipmentGroup equipmentGroup;
    private Equipment equipment;
    private Handler handler;

    // Atributos do Firebase.
    FirebaseDatabase database;
    DatabaseReference equipGroupRef, equipRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Método onCreate");
        try {
            super.onCreate(savedInstanceState);
            bind(); // Inicializa os atributos e referencias.
            creatEvent(); // Cria eventos para os componentes da tela.
            loadComponentsNav();
        } catch (Exception error) {
            // Caso, haja algum erro, será imprimido no log.
            Log.e(TAG, "ERRO no método onCreate: "+error.getMessage(), error);
        }
    }

    // Inicializa os atributos e referencias.
    private void bind() {
        Log.d(TAG, "Método bind");
        try {
            // Referencia a tela XML.
            setContentView(R.layout.activity_main);

            // Referencia os componentes da interface.
            inicilizeComponetsInterface();

            // Inicializa atributos da classe.
            inicializeVarClass();

            // Inicializa atributos do Firebase.
            inicializeFirebase();

        } catch (Exception error) {
            // Caso, haja algum erro, será imprimido no log.
            Log.e(TAG, "ERRO no método bind: "+error.getMessage(), error);
        }
    }

    private void inicilizeComponetsInterface() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        imageView = navigationView.getHeaderView(0).findViewById(R.id.imageView);
        txvName = navigationView.getHeaderView(0).findViewById(R.id.txvName);
        txvEmail = navigationView.getHeaderView(0).findViewById(R.id.txvEmail);
        listView = findViewById(R.id.listView);
    }

    private void inicializeVarClass() {
        window = EQUIPMENT_GROUP;
        listEquipmentGroup = new ArrayList<EquipmentGroup>();
        listEquipments = new ArrayList<Equipment>();
        equipmentGroup = new EquipmentGroup();
        equipment = new Equipment();
        handler = new Handler();
    }

    private void inicializeFirebase() {
        // Inicializa objeto de negócios do login (passando o id do cliente web padrão e o contexto
        // da aplicação).
        bAuth = new BAuth(getString(R.string.default_web_client_id), this);

        // Referencia a instância do banco de dados do Firebase.
        database = FirebaseDatabase.getInstance();

        // Permiti salvar, alterar arquivos em nuvem e também dentro do app.
        database.setPersistenceEnabled(true);

        // Referencia nós do banco de dados.
        equipGroupRef = database.getReference().child("listEquipmentGroup");
    }

    // Cria eventos;
    private void creatEvent() {
        Log.d(TAG, "Método creatEvent");
        try {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ((TextView) view.findViewById(R.id.txName))
                            .setTextColor(getColor(R.color.colorCtrBlack));
                    return;
                }
            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    view.setSelected(true);
                    ((TextView) view.findViewById(R.id.txName))
                            .setTextColor(getColor(R.color.colorCtrWhite));
                    return true;
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

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (window.equals(EQUIPMENT_GROUP))
                        startActivity(createIntent(MainActivity.this,
                                FormGroupEquipmentActivity.class, null));
                    else
                        startActivity(createIntent(MainActivity.this,
                                FormEquipmentActivity.class, null));
                }
            });
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método createEvent: "+error.getMessage(), error);
        }
    }

    // Cria uma intenção para enviar para outra janela.
    private Intent createIntent(Context context, Class aClass, Object object) {
        Log.d(TAG, "Método createIntent");
        try {
            Intent intent = new Intent(context, aClass);
            Bundle bundle = new Bundle();
            bundle.putSerializable("objeto", (Serializable) object);
            intent.putExtra("bundle", bundle);
            return intent;
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método createIntent: "+error.getMessage(), error);
        }
        return null;
    }

    private void loadComponentsNav() {
        Log.d(TAG, "Método loadComponentsNav");
        try {
            if (bAuth.hasAuth() && bAuth.getUser().getEmail() != null
                    && !bAuth.getUser().getEmail().isEmpty()) {
                loadImageBitmap();
                txvName.setText(bAuth.getUser().getDisplayName()==null
                        ? "":bAuth.getUser().getDisplayName());
                txvEmail.setText(bAuth.getUser().getEmail());
            }
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método loadComponentsNav: "+error.getMessage(), error);
        }
    }

    private void loadImageBitmap() {
        new Thread() {
            public void run(){
                Log.d(TAG, "Método loadImageBitmap");
                try {
                    if (bAuth.getUser().getPhotoUrl() != null) {
                        // Cria objeto para abrir conexão.
                        URL url = new URL(bAuth.getUser().getPhotoUrl().toString());
                        // Aqui abri uma conexão.
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        // Aqui baixa a imagem.
                        InputStream input = connection.getInputStream();
                        // Aqui decodificamos o conteúdo baixado para imagem bitmap.
                        Bitmap bitmap = BitmapFactory.decodeStream(input);
                        // Carregamos a imagem no elemento.
                        final Bitmap aux = bitmap;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(aux);
                            }
                        });
                    }
                } catch (IOException e) {
                    Log.e(TAG, "ERRO no método loadImageBitmap: "+e.getMessage(), e);
                } catch (Exception error) {
                    Log.e(TAG, "ERRO no método loadImageBitmap: "+error.getMessage(), error);
                }
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(window.equals(EQUIPMENT)){
            window = EQUIPMENT_GROUP;
            onStart();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        toolbar.getMenu().findItem(R.id.btnDisconnect).setVisible(false);
        return true;
    }

    @Override
    public void onStart() {
        Log.d(TAG, "Método onStart");
        try {
            super.onStart();
            // Verifica se existe alguma autentificação e atualiza a interface de acordo.
            loadDatabase();
        } catch (Exception error) {
            Log.e(TAG,"ERRO no método onStart: "+error.getMessage(), error);
        }
    }

    private void loadDatabase() {
        Log.d(TAG, "Método loadDatabase");
        try {
            if (window.equals(EQUIPMENT_GROUP)) {
                equipGroupRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listEquipmentGroup.clear();
                        for (DataSnapshot objetoSnapshot:dataSnapshot.getChildren()) {
                            EquipmentGroup equipmentGroup = objetoSnapshot
                                    .getValue(EquipmentGroup.class);
                            listEquipmentGroup.add(equipmentGroup);
                        }
                        loadListView(listEquipmentGroup);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "ERRO na comunicação com a referencia equipGroupRef: "
                                +databaseError.getMessage(), databaseError.toException());
                    }
                });
            } else {
                equipGroupRef.child(equipmentGroup.getId()).child(equipRef.getParent().toString())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                listEquipments.clear();
                                for (DataSnapshot objetoSnapshot:dataSnapshot.getChildren()) {
                                    Equipment equipment = objetoSnapshot.getValue(Equipment.class);
                                    listEquipments.add(equipment);
                                }
                                equipmentGroup.setListEquipment(listEquipments);
                                loadListView(equipmentGroup);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e(TAG, "ERRO na comunicação com a referencia: "
                                        +databaseError.getMessage(), databaseError.toException());
                            }
                        });
            }
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método loadDatabase: "+error.getMessage(), error);
        }
    }

    private void loadListView(Object object) {
        Log.d(TAG, "Método loadListView");
        try {
            if (window.equals(EQUIPMENT_GROUP) && object instanceof List) {
                EquipmentGroupAdapter adapter =
                        new EquipmentGroupAdapter(this, (List<EquipmentGroup>) object);
                listView.setAdapter(adapter);
            } else {
                EquipmentAdapter adapter =
                        new EquipmentAdapter(this, (EquipmentGroup) object);
                listView.setAdapter(adapter);
            }
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método loadListView: "+error.getMessage(), error);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.btnConnect) {
            item.setVisible(false);
            toolbar.getMenu().findItem(R.id.btnDisconnect).setVisible(true);
        }
        if (id == R.id.btnDisconnect) {
            item.setVisible(false);
            toolbar.getMenu().findItem(R.id.btnConnect).setVisible(true);
        }
        if (id == R.id.btnOpen) {
            if (listView.isSelected()) {
                window = EQUIPMENT;
                loadDatabase();
            } else {
                Toast.makeText(this, "Selecione um item da lista.", Toast.LENGTH_SHORT);
            }
        }
        if (id == R.id.btnUpdate) {
            if (listView.isSelected()) {
                if (window.equals(EQUIPMENT_GROUP)) {
                    equipmentGroup = (EquipmentGroup) listView.getSelectedItem();
                    startActivity(createIntent(MainActivity.this,
                            FormGroupEquipmentActivity.class, equipmentGroup));
                } else if (window.equals(EQUIPMENT)) {
                    equipment = (Equipment) listView.getSelectedItem();
                    startActivity(createIntent(MainActivity.this,
                            FormEquipmentActivity.class, equipment));
                }
            } else {
                Toast.makeText(this, "Selecione um item da lista.", Toast.LENGTH_SHORT);
            }
        }
        if (id == R.id.btnControlRemote) {
            startActivity(createIntent(this, RemoteControlActivity.class, null));
        }

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /*if (id == R.id.btnSettings) {

        }*/
        if (id == R.id.btnSignOut) {
            signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut() {
        try {
            bAuth.signOut();
            finish();
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método signOut: "+error.getMessage(), error);
        }
    }
}