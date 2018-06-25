package br.com.marcosviniciusti.projetotcc.activities;

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
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.marcosviniciusti.projetotcc.R;
import br.com.marcosviniciusti.projetotcc.negocio.NEquipamento;
import br.com.marcosviniciusti.projetotcc.negocio.NFirebaseAuth;
import br.com.marcosviniciusti.projetotcc.negocio.NGrupoEquipamento;
import br.com.marcosviniciusti.projetotcc.negocio.NUsuario;
import br.com.marcosviniciusti.projetotcc.entidade.EEquipamento;
import br.com.marcosviniciusti.projetotcc.entidade.EGrupoEquipamento;
import br.com.marcosviniciusti.projetotcc.entidade.EUsuario;
import br.com.marcosviniciusti.projetotcc.util.EnumTipoEquipamento;
import br.com.marcosviniciusti.projetotcc.util.EquipamentoAdapter;
import br.com.marcosviniciusti.projetotcc.util.GrupoEquipamentoAdapter;
import br.com.marcosviniciusti.projetotcc.util.UsuariosAdapter;


/**
 * Uma tela de nFirebaseAuth que oferece nFirebaseAuth via email / senha.
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
    private TextView txvNome;
    private TextView txvEmail;
    private ListView listView;

    // Atributos da classe.
    private static final String TAG = "AutenticacaoActivity";
    private static final String STATE_IMAGEVIEW = "STATE_IMAGEVIEW";
    private static final String STATE_NOME = "STATE_NOME";
    private static final String STATE_EMAIL = "STATE_EMAIL";
    private static final String STATE_LISTA_GRUPO_EQUIPAMENTO = "STATE_LISTA_GRUPO_EQUIPAMENTO";
    private static final String STATE_LISTA_EQUIPAMENTO = "STATE_LISTA_EQUIPAMENTO";
    private static final String STATE_GRUPO_EQUIPAMENTO = "STATE_GRUPO_EQUIPAMENTO";
    private static final String STATE_EQUIPAMENTO = "STATE_EQUIPAMENTO";
    private static final String GRUPO_EQUIPAMENTO = "GRUPO_EQUIPAMENTO";
    private static final String EQUIPAMENTO = "EQUIPAMENTO";
    private static final String USUARIO = "USUARIO";
    private String window;
    private View viewSelecionada;
    private int viewPosicao;
    private List<EGrupoEquipamento> listaGrupoEquipamentos;
    private List<String> listaUsuarios;
    private List<EUsuario> grupoUsuarios;
    private List<EEquipamento> listaEquipamentos;
    private EGrupoEquipamento grupoEquipamento;
    private EEquipamento equipamento;
    private EUsuario usuario = new EUsuario();
    private Handler handler;

    // Atributos do Firebase.
    private NFirebaseAuth nFirebaseAuth;
    private NUsuario nUsuario;
    private NGrupoEquipamento nGrupoEquipamento;
    private NEquipamento nEquipamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Método onCreate");
        try {
            super.onCreate(savedInstanceState);
            Inicializar(); // Inicializa os atributos e referencias.
            criarEventos(); // Cria eventos para os componentes da tela.
            carregarComponentesNav();
        } catch (Exception error) {
            // Caso, haja algum erro, será imprimido no log.
            Log.e(TAG, "ERRO no método onCreate: "+error.getMessage(), error);
        }
    }

    // Inicializa os atributos e referencias.
    private void Inicializar() {
        Log.d(TAG, "Método Inicializar");
        try {
            // Referencia a tela XML.
            setContentView(R.layout.activity_main);

            inicilizarComponentesDaInterface();
            inicializarAtributosDaClasse();
            inicializarFirebase();

        } catch (Exception error) {
            // Caso, haja algum erro, será imprimido no log.
            Log.e(TAG, "ERRO no método Inicializar: "+error.getMessage(), error);
        }
    }

    private void inicilizarComponentesDaInterface() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_abrir, R.string.navigation_drawer_fechar);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        imageView = navigationView.getHeaderView(0).findViewById(R.id.imageView);
        txvNome = navigationView.getHeaderView(0).findViewById(R.id.txvNome);
        txvEmail = navigationView.getHeaderView(0).findViewById(R.id.txvEmail);
        listView = findViewById(R.id.listView);
    }

    private void inicializarAtributosDaClasse() {
        window = GRUPO_EQUIPAMENTO;
        viewSelecionada = null;
        viewPosicao = -1;
        listaGrupoEquipamentos = new ArrayList<EGrupoEquipamento>();
        listaUsuarios = new ArrayList<String>();
        grupoUsuarios = new ArrayList<EUsuario>();
        listaEquipamentos = new ArrayList<EEquipamento>();
        grupoEquipamento = new EGrupoEquipamento();
        equipamento = new EEquipamento();
        handler = new Handler();
    }

    private void inicializarFirebase() {
        // Inicializa objeto de negócios da API do Firebase.
        nFirebaseAuth = new NFirebaseAuth(getString(R.string.default_web_client_id), this);

        // Inicializa objeto de negócios do database.
        nUsuario = new NUsuario();
        nGrupoEquipamento = new NGrupoEquipamento();
        nEquipamento = new NEquipamento();
    }

    // Cria eventos;
    private void criarEventos() {
        Log.d(TAG, "Método criarEventos");
        try {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if (window.equals(GRUPO_EQUIPAMENTO)) {
                        if (viewSelecionada == null || viewSelecionada != view) {
                            view.setSelected(true);
                            viewSelecionada = view;
                            viewPosicao = position;
                            grupoEquipamento = (EGrupoEquipamento) listView
                                    .getItemAtPosition(viewPosicao);
                        } else {
                            viewSelecionada = null;
                            viewPosicao = -1;
                            grupoEquipamento = null;
                        }
                    } else {
                        if (viewSelecionada == null || viewSelecionada != view) {
                            view.setSelected(true);
                            viewSelecionada = view;
                            viewPosicao = position;
                            equipamento = (EEquipamento) listView
                                    .getItemAtPosition(viewPosicao);
                        } else {
                            viewSelecionada = null;
                            viewPosicao = -1;
                            equipamento = null;
                        }
                    }
                }
            });

//            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//                @Override
//                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//
//                    return false;
//                }
//            });

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (viewSelecionada != null) {
                        if (window.equals(GRUPO_EQUIPAMENTO)) {
                            grupoEquipamento = (EGrupoEquipamento) listView
                                    .getItemAtPosition(viewPosicao);
                            startActivity(criarIntent(MainActivity.this,
                                    FormularioActivity.class, grupoEquipamento, viewPosicao, window));
                        } else {
                            equipamento = (EEquipamento) listView
                                    .getItemAtPosition(viewPosicao);
                            startActivity(criarIntent(MainActivity.this,
                                    FormularioActivity.class, grupoEquipamento, viewPosicao, window));
                        }
                    } else {
                        if (window.equals(GRUPO_EQUIPAMENTO))
                            startActivity(criarIntent(MainActivity.this,
                                    FormularioActivity.class, null, viewPosicao, window));
                        else
                            startActivity(criarIntent(MainActivity.this,
                                    FormularioActivity.class, grupoEquipamento, viewPosicao, window));
                    }
                }
            });
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método createEvent: "+error.getMessage(), error);
        }
    }

    // Cria uma intenção para enviar para outra janela.
    private Intent criarIntent(Context context, Class aClass, Object objeto, int posicao,
                               String window) {
        Log.d(TAG, "Método criarIntent");
        try {
            Intent intent = new Intent(context, aClass);
            Bundle bundle = new Bundle();
            bundle.putSerializable("objeto", (Serializable) objeto);
            bundle.putInt("posicao", posicao);
            bundle.putString("window", window);
            intent.putExtra("bundle", bundle);
            return intent;
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método criarIntent: "+error.getMessage(), error);
        }
        return null;
    }

    private void carregarComponentesNav() {
        Log.d(TAG, "Método carregarComponentesNav");
        try {
            if (validarAutenticacao()) {
                carregarImageBitmap();
                if (validarCampoNome()) {
                    usuario.setId(nFirebaseAuth.getUser().getUid());
                    nUsuario.consultar(usuario, new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            usuario = dataSnapshot.getValue(EUsuario.class);
                            txvNome.setText(usuario.getNome());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    txvNome.setText(nFirebaseAuth.getUser().getDisplayName());
                }
                txvEmail.setText(nFirebaseAuth.getUser().getEmail());
            }
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método carregarComponentesNav: "+error.getMessage(), error);
        }
    }

    private boolean validarAutenticacao() {
        return nFirebaseAuth.hasAuth() && nFirebaseAuth.getUser().getEmail() != null
                && !nFirebaseAuth.getUser().getEmail().isEmpty();
    }

    private boolean validarCampoNome() {
        return nFirebaseAuth.getUser().getDisplayName() == null
                || nFirebaseAuth.getUser().getDisplayName().isEmpty();
    }

    private void carregarImageBitmap() {
        new Thread() {
            public void run(){
                Log.d(TAG, "Método carregarImageBitmap");
                try {
                    if (nFirebaseAuth.getUser().getPhotoUrl() != null) {
                        // Cria objeto para abrir conexão.
                        URL url = new URL(nFirebaseAuth.getUser().getPhotoUrl().toString());
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
                    Log.e(TAG, "ERRO no método carregarImageBitmap: "+e.getMessage(), e);
                } catch (Exception error) {
                    Log.e(TAG, "ERRO no método carregarImageBitmap: "+error.getMessage(), error);
                }
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (window.equals(EQUIPAMENTO)){
            window = GRUPO_EQUIPAMENTO;
            grupoEquipamento = null;
            equipamento = null;
            onRestart();
        } else {
            onStart();
        }
//        super.onBackPressed();
    }

    @Override
    public void onStart() {
        Log.d(TAG, "Método onStart");
        try {
            super.onStart();

            // Verifica se existe alguma autentificação e atualiza a interface de acordo.
            consultarBancoDeDados();
            grupoEquipamento = null;
            equipamento = null;
        } catch (Exception error) {
            Log.e(TAG,"ERRO no método onStart: "+error.getMessage(), error);
        }
    }

    private void consultarBancoDeDados() {
        Log.d(TAG, "Método consultarBancoDeDados");
        try {
            if (window.equals(GRUPO_EQUIPAMENTO)) {
                nGrupoEquipamento.listar(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listaGrupoEquipamentos.clear();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            EGrupoEquipamento grupoEquipamento = child.getValue(EGrupoEquipamento.class);
                            grupoEquipamento.setId(child.getKey());
                            if (nFirebaseAuth.getUser().getUid().equals(grupoEquipamento.getIdUsuarioDono())) {
                                listaGrupoEquipamentos.add(grupoEquipamento);
                            }
                        }
                        loadListView(listaGrupoEquipamentos);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "Não foi possível acessar o database.");
                    }
                });
            } else if (window.equals(EQUIPAMENTO)) {
                nEquipamento.listar(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listaEquipamentos.clear();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            EEquipamento equipamento = child.getValue(EEquipamento.class);
                            equipamento.setId(child.getKey());
                            if (grupoEquipamento.getId().equals(equipamento.getIdGrupoEquipamento())) {
                                listaEquipamentos.add(equipamento);
                            }
                        }
                        grupoEquipamento.setEquipamentos(listaEquipamentos);
                        loadListView(grupoEquipamento);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método consultarBancoDeDados: "+error.getMessage(), error);
        }
    }

    private void loadListView(Object object) {
        Log.d(TAG, "Método loadListView");
        try {
            if (window.equals(GRUPO_EQUIPAMENTO) && object instanceof List) {
                GrupoEquipamentoAdapter adapter =
                        new GrupoEquipamentoAdapter(this, (List<EGrupoEquipamento>) object);
                listView.setAdapter(adapter);
            } else if (window.equals(EQUIPAMENTO)) {
                EquipamentoAdapter adapter =
                        new EquipamentoAdapter(this, (EGrupoEquipamento) object);

                listView.setAdapter(adapter);
            } else {
                listaUsuarios = grupoEquipamento.getListaUsuarios();
                nUsuario.listar(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        grupoUsuarios.clear();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            EUsuario usuario = child.getValue(EUsuario.class);
                            for (String s : listaUsuarios) {
                                if (usuario.getId().equals(s)) {
                                    grupoUsuarios.add(usuario);
                                }
                            }
                        }
                        UsuariosAdapter adapter =
                                new UsuariosAdapter(MainActivity.this, grupoUsuarios);

                        listView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método loadListView: "+error.getMessage(), error);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        if (window.equals(EQUIPAMENTO)) {
            toolbar.getMenu().findItem(R.id.btnAbrirGrupo).setVisible(false);
            toolbar.getMenu().findItem(R.id.btnDeletarGrupo).setTitle(getString(R.string.action_deletar_equipamento));
            toolbar.getMenu().getItem(R.id.btnAdicionarUsuario).setVisible(false);
            toolbar.getMenu().getItem(R.id.btnRemoverUsuario).setVisible(false);
            toolbar.getMenu().getItem(R.id.btnControleRemoto).setVisible(true);
        }

        return true;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // Sempre chame a superclasse para que possa
        // restaurar a hierarquia da view.
        super.onRestoreInstanceState(savedInstanceState);

        // Restaura estados membros da instância salva.
//        imageView.setImageAlpha(savedInstanceState.getInt(STATE_IMAGEVIEW));
//        txvNome.setText(savedInstanceState.getString(STATE_NOME));
//        txvEmail.setText(savedInstanceState.getString(STATE_EMAIL));
//
//        grupoEquipamento = (EGrupoEquipamento) savedInstanceState.getSerializable(STATE_GRUPO_EQUIPAMENTO);
//        equipamento = (EEquipamento) savedInstanceState.getSerializable(STATE_EQUIPAMENTO);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.btnAbrirGrupo) {
            if (viewSelecionada != null) {
                window = EQUIPAMENTO;
                viewSelecionada = null;
                viewPosicao = -1;
                item.setVisible(false);
                consultarBancoDeDados();
            } else {
                Toast.makeText(getApplicationContext(), "Selecione um item da lista.", Toast.LENGTH_SHORT).show();
            }
        }
        if (id == R.id.btnDeletarGrupo) {
            if (viewSelecionada != null) {
                if (window.equals(GRUPO_EQUIPAMENTO)) {
                    grupoEquipamento = (EGrupoEquipamento) listView
                            .getItemAtPosition(viewPosicao);
                    nGrupoEquipamento.deletar(grupoEquipamento);
                } else if (window.equals(EQUIPAMENTO)) {
                    equipamento = (EEquipamento) listView
                            .getItemAtPosition(viewPosicao);
                    nEquipamento.deletar(equipamento);
                }
            } else {
                Toast.makeText(getApplicationContext(), "Selecione um item da lista.", Toast.LENGTH_SHORT);
            }
        }
        if (id == R.id.btnAdicionarUsuario) {

            if (viewSelecionada != null) {
                window = USUARIO;
                viewSelecionada = null;
                viewPosicao = -1;
                startActivity(criarIntent(this, FormularioActivity.class, null, viewPosicao, window));
            } else {
                Toast.makeText(getApplicationContext(), "Selecione um item da lista.", Toast.LENGTH_SHORT).show();
            }
        }
        if (id == R.id.btnRemoverUsuario) {
            if (viewSelecionada != null) {
                window = USUARIO;
                viewSelecionada = null;
                viewPosicao = -1;
                loadListView(grupoEquipamento);
            } else {
                Toast.makeText(getApplicationContext(), "Selecione um item da lista.", Toast.LENGTH_SHORT).show();
            }
        }
        if (id == R.id.btnControleRemoto) {
            if (equipamento.getModelo().getTipo().equals(EnumTipoEquipamento.TV)) {
                startActivity(criarIntent(this, RemoteControlActivity.class, grupoEquipamento, viewPosicao, null));
            } else if (equipamento.getModelo().getTipo().equals(EnumTipoEquipamento.PROJETOR)) {
                startActivity(criarIntent(this, RemoteControlActivity.class, grupoEquipamento, viewPosicao, null));
            } else {
                startActivity(criarIntent(this, RemoteControlActivity.class, grupoEquipamento, viewPosicao, null));
            }
        }

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.btnSair) {
            signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut() {
        try {
            nFirebaseAuth.signOut();
            finish();
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método signOut: "+error.getMessage(), error);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        // Salva o estado atual do jogo do usuário
        savedInstanceState.putInt(STATE_IMAGEVIEW, imageView.getImageAlpha());
        savedInstanceState.putString(STATE_NOME, txvNome.getText().toString());
        savedInstanceState.putString(STATE_EMAIL, txvEmail.getText().toString());

        // Sempre chame a superclasse para que seja salvo
        // o estado da hierarquia da view
        super.onSaveInstanceState(savedInstanceState);
    }
}