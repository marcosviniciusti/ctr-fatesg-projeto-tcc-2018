package br.com.marcosviniciusti.projetotcc.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.marcosviniciusti.projetotcc.R;
import br.com.marcosviniciusti.projetotcc.entidade.EComando;
import br.com.marcosviniciusti.projetotcc.entidade.EEquipamento;
import br.com.marcosviniciusti.projetotcc.entidade.EGrupoEquipamento;
import br.com.marcosviniciusti.projetotcc.entidade.EMarca;
import br.com.marcosviniciusti.projetotcc.entidade.EModelo;
import br.com.marcosviniciusti.projetotcc.entidade.ETransmissao;
import br.com.marcosviniciusti.projetotcc.negocio.NComando;
import br.com.marcosviniciusti.projetotcc.negocio.NFirebaseAuth;
import br.com.marcosviniciusti.projetotcc.negocio.NMarca;
import br.com.marcosviniciusti.projetotcc.negocio.NModelo;
import br.com.marcosviniciusti.projetotcc.negocio.NTransmissao;
import br.com.marcosviniciusti.projetotcc.util.EnumTipoEquipamento;


public class RemoteControlActivity extends AppCompatActivity {

    public final String TAG = "RemoteControlActivity";

    private FloatingActionButton fabPower;
    private FloatingActionButton fabVolUp;
    private FloatingActionButton fabVolDown;
    private FloatingActionButton fabChUp;
    private FloatingActionButton fabChDown;

    private Button btn25;
    private Button btn24;
    private Button btn23;
    private Button btn22;
    private Button btn21;
    private Button btn20;
    private Button btn19;
    private Button btn18;
    private Button btn17;

    private EGrupoEquipamento grupoEquipamento;
    private EEquipamento equipamento;
    private int posicao;
    private EModelo modelo;
    private EMarca marca;
    private ETransmissao transmissao;
    private List<EComando> comandos;
    private String comando;

    private NFirebaseAuth nFirebaseAuth;
    private NMarca nMarca;
    private NModelo nModelo;
    private NComando nComando;
    private NTransmissao nTransmissao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pegarIntent();
        inicializar();
        criarEventos();
    }

    private void pegarIntent() {
        grupoEquipamento = (EGrupoEquipamento) getIntent()
                .getBundleExtra("bundle").getSerializable("objeto");
        posicao = getIntent().getBundleExtra("bundle").getInt("posicao");
        equipamento = grupoEquipamento.getEquipamentos().get(posicao);
    }

    private void inicializar() {
        inicializarAtributosDaClasse();
        inicializarFirebase();
        definirContentView();
        inicializarAtributosDaInterface();
    }

    public void inicializarAtributosDaClasse() {
        modelo = new EModelo();
        marca = new EMarca();
        transmissao = new ETransmissao();
        comandos = new ArrayList<>();
    }

    public void inicializarFirebase() {
        nFirebaseAuth = new NFirebaseAuth(getString(R.string.default_web_client_id), this);

        nModelo = new NModelo();
        nModelo.listar(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    EModelo m = child.getValue(EModelo.class);
                    if (m.getId().equals(equipamento.getModelo())) {
                        modelo = m;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "ERRO ao listar modelos.");
            }
        });

        nMarca = new NMarca();
        nMarca.listar(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    EMarca m = child.getValue(EMarca.class);
                    if (m.getId().equals(modelo.getIdMarca())) {
                        marca = m;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "ERRO ao listar marcas.");
            }
        });

        nComando = new NComando();
        nComando.listar(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    EComando c = child.getValue(EComando.class);
                    String[] split = c.getId().split(":");
                    if (split[0].equals(modelo.getId())) {
                        comandos.add(c);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "ERRO ao listar comandos.");
            }
        });

        nTransmissao = new NTransmissao();
    }

    private void definirContentView() {
        if (igualTipoTV())
            setContentView(R.layout.activity_tv);
        else if (igualTipoProjetor())
            setContentView(R.layout.activity_projetor);
        else {
            setContentView(R.layout.activity_ar);
        }
    }

    private void inicializarAtributosDaInterface() {
        if (igualTipoTV()) {
            fabPower = findViewById(R.id.fabPower);
            fabVolUp = findViewById(R.id.fabVolUp);
            fabVolDown = findViewById(R.id.fabVolDown);
            fabChUp = findViewById(R.id.fabChUp);
            fabChDown = findViewById(R.id.fabChDown);
        } else if (igualTipoProjetor()) {
            fabPower = findViewById(R.id.fabPower);
            fabVolUp = findViewById(R.id.fabVolUp);
            fabVolDown = findViewById(R.id.fabVolDown);
            fabChUp = findViewById(R.id.fabChUp);
            fabChDown = findViewById(R.id.fabChDown);
        } else {
            btn25 = findViewById(R.id.btn25);
            btn24 = findViewById(R.id.btn24);
            btn23 = findViewById(R.id.btn23);
            btn22 = findViewById(R.id.btn22);
            btn21 = findViewById(R.id.btn21);
            btn20 = findViewById(R.id.btn20);
            btn19 = findViewById(R.id.btn19);
            btn18 = findViewById(R.id.btn18);
            btn17 = findViewById(R.id.btn17);
        }
    }

    private void criarEventos() {
        if (igualTipoTV()) {
            fabPower.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EComando comando = new EComando();
                    for (int i=0; i<comandos.size(); i++) {
                        EComando c = comandos.get(i);
                        if (c.getNome().equals("Power")) {
                            comando = c;
                        }
                    }
                    transmissao = new ETransmissao();
                    transmissao.setComando(comando.getId());
                    transmissao.setDtHrSubmissao(new Date());
                    transmissao.setEquipamento(equipamento);
                    transmissao.setIdUsuario(nFirebaseAuth.getUser().getUid());
                    nTransmissao.salvar(transmissao);
                }
            });
            fabVolUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EComando comando = new EComando();
                    for (int i=0; i<comandos.size(); i++) {
                        EComando c = comandos.get(i);
                        if (c.getNome().equals("Power")) {
                            comando = c;
                        }
                    }
                    transmissao = new ETransmissao();
                    transmissao.setComando(comando.getId());
                    transmissao.setDtHrSubmissao(new Date());
                    transmissao.setEquipamento(equipamento);
                    transmissao.setIdUsuario(nFirebaseAuth.getUser().getUid());
                    nTransmissao.salvar(transmissao);
                }
            });
            fabVolDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EComando comando = new EComando();
                    for (int i=0; i<comandos.size(); i++) {
                        EComando c = comandos.get(i);
                        if (c.getNome().equals("Power")) {
                            comando = c;
                        }
                    }
                    transmissao = new ETransmissao();
                    transmissao.setComando(comando.getId());
                    transmissao.setDtHrSubmissao(new Date());
                    transmissao.setEquipamento(equipamento);
                    transmissao.setIdUsuario(nFirebaseAuth.getUser().getUid());
                    nTransmissao.salvar(transmissao);
                }
            });
            fabChUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EComando comando = new EComando();
                    for (int i=0; i<comandos.size(); i++) {
                        EComando c = comandos.get(i);
                        if (c.getNome().equals("Power")) {
                            comando = c;
                        }
                    }
                    transmissao = new ETransmissao();
                    transmissao.setComando(comando.getId());
                    transmissao.setDtHrSubmissao(new Date());
                    transmissao.setEquipamento(equipamento);
                    transmissao.setIdUsuario(nFirebaseAuth.getUser().getUid());
                    nTransmissao.salvar(transmissao);
                }
            });
            fabChDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EComando comando = new EComando();
                    for (int i=0; i<comandos.size(); i++) {
                        EComando c = comandos.get(i);
                        if (c.getNome().equals("Power")) {
                            comando = c;
                        }
                    }
                    transmissao = new ETransmissao();
                    transmissao.setComando(comando.getId());
                    transmissao.setDtHrSubmissao(new Date());
                    transmissao.setEquipamento(equipamento);
                    transmissao.setIdUsuario(nFirebaseAuth.getUser().getUid());
                    nTransmissao.salvar(transmissao);
                }
            });
        } else if (igualTipoProjetor()) {
            fabPower.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EComando comando = new EComando();
                    for (int i=0; i<comandos.size(); i++) {
                        EComando c = comandos.get(i);
                        if (c.getNome().equals("Power")) {
                            comando = c;
                        }
                    }
                    transmissao = new ETransmissao();
                    transmissao.setComando(comando.getId());
                    transmissao.setDtHrSubmissao(new Date());
                    transmissao.setEquipamento(equipamento);
                    transmissao.setIdUsuario(nFirebaseAuth.getUser().getUid());
                    nTransmissao.salvar(transmissao);
                }
            });
            fabVolUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EComando comando = new EComando();
                    for (int i=0; i<comandos.size(); i++) {
                        EComando c = comandos.get(i);
                        if (c.getNome().equals("Power")) {
                            comando = c;
                        }
                    }
                    transmissao = new ETransmissao();
                    transmissao.setComando(comando.getId());
                    transmissao.setDtHrSubmissao(new Date());
                    transmissao.setEquipamento(equipamento);
                    transmissao.setIdUsuario(nFirebaseAuth.getUser().getUid());
                    nTransmissao.salvar(transmissao);
                }
            });
            fabVolDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EComando comando = new EComando();
                    for (int i=0; i<comandos.size(); i++) {
                        EComando c = comandos.get(i);
                        if (c.getNome().equals("Power")) {
                            comando = c;
                        }
                    }
                    transmissao = new ETransmissao();
                    transmissao.setComando(comando.getId());
                    transmissao.setDtHrSubmissao(new Date());
                    transmissao.setEquipamento(equipamento);
                    transmissao.setIdUsuario(nFirebaseAuth.getUser().getUid());
                    nTransmissao.salvar(transmissao);
                }
            });
            fabChUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EComando comando = new EComando();
                    for (int i=0; i<comandos.size(); i++) {
                        EComando c = comandos.get(i);
                        if (c.getNome().equals("Power")) {
                            comando = c;
                        }
                    }
                    transmissao = new ETransmissao();
                    transmissao.setComando(comando.getId());
                    transmissao.setDtHrSubmissao(new Date());
                    transmissao.setEquipamento(equipamento);
                    transmissao.setIdUsuario(nFirebaseAuth.getUser().getUid());
                    nTransmissao.salvar(transmissao);
                }
            });
            fabChDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EComando comando = new EComando();
                    for (int i=0; i<comandos.size(); i++) {
                        EComando c = comandos.get(i);
                        if (c.getNome().equals("Power")) {
                            comando = c;
                        }
                    }
                    transmissao = new ETransmissao();
                    transmissao.setComando(comando.getId());
                    transmissao.setDtHrSubmissao(new Date());
                    transmissao.setEquipamento(equipamento);
                    transmissao.setIdUsuario(nFirebaseAuth.getUser().getUid());
                    nTransmissao.salvar(transmissao);
                }
            });
        } else {
            btn25.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EComando comando = new EComando();
                    for (int i=0; i<comandos.size(); i++) {
                        EComando c = comandos.get(i);
                        if (c.getNome().equals("Power")) {
                            comando = c;
                        }
                    }
                    transmissao = new ETransmissao();
                    transmissao.setComando(comando.getId());
                    transmissao.setDtHrSubmissao(new Date());
                    transmissao.setEquipamento(equipamento);
                    transmissao.setIdUsuario(nFirebaseAuth.getUser().getUid());
                    nTransmissao.salvar(transmissao);
                }
            });
            btn24.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EComando comando = new EComando();
                    for (int i=0; i<comandos.size(); i++) {
                        EComando c = comandos.get(i);
                        if (c.getNome().equals("Power")) {
                            comando = c;
                        }
                    }
                    transmissao = new ETransmissao();
                    transmissao.setComando(comando.getId());
                    transmissao.setDtHrSubmissao(new Date());
                    transmissao.setEquipamento(equipamento);
                    transmissao.setIdUsuario(nFirebaseAuth.getUser().getUid());
                    nTransmissao.salvar(transmissao);
                }
            });
            btn23.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EComando comando = new EComando();
                    for (int i=0; i<comandos.size(); i++) {
                        EComando c = comandos.get(i);
                        if (c.getNome().equals("Power")) {
                            comando = c;
                        }
                    }
                    transmissao = new ETransmissao();
                    transmissao.setComando(comando.getId());
                    transmissao.setDtHrSubmissao(new Date());
                    transmissao.setEquipamento(equipamento);
                    transmissao.setIdUsuario(nFirebaseAuth.getUser().getUid());
                    nTransmissao.salvar(transmissao);
                }
            });
            btn22.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EComando comando = new EComando();
                    for (int i=0; i<comandos.size(); i++) {
                        EComando c = comandos.get(i);
                        if (c.getNome().equals("Power")) {
                            comando = c;
                        }
                    }
                    transmissao = new ETransmissao();
                    transmissao.setComando(comando.getId());
                    transmissao.setDtHrSubmissao(new Date());
                    transmissao.setEquipamento(equipamento);
                    transmissao.setIdUsuario(nFirebaseAuth.getUser().getUid());
                    nTransmissao.salvar(transmissao);
                }
            });
            btn21.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EComando comando = new EComando();
                    for (int i=0; i<comandos.size(); i++) {
                        EComando c = comandos.get(i);
                        if (c.getNome().equals("Power")) {
                            comando = c;
                        }
                    }
                    transmissao = new ETransmissao();
                    transmissao.setComando(comando.getId());
                    transmissao.setDtHrSubmissao(new Date());
                    transmissao.setEquipamento(equipamento);
                    transmissao.setIdUsuario(nFirebaseAuth.getUser().getUid());
                    nTransmissao.salvar(transmissao);
                }
            });
            btn20.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EComando comando = new EComando();
                    for (int i=0; i<comandos.size(); i++) {
                        EComando c = comandos.get(i);
                        if (c.getNome().equals("Power")) {
                            comando = c;
                        }
                    }
                    transmissao = new ETransmissao();
                    transmissao.setComando(comando.getId());
                    transmissao.setDtHrSubmissao(new Date());
                    transmissao.setEquipamento(equipamento);
                    transmissao.setIdUsuario(nFirebaseAuth.getUser().getUid());
                    nTransmissao.salvar(transmissao);
                }
            });
            btn19.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EComando comando = new EComando();
                    for (int i=0; i<comandos.size(); i++) {
                        EComando c = comandos.get(i);
                        if (c.getNome().equals("Power")) {
                            comando = c;
                        }
                    }
                    transmissao = new ETransmissao();
                    transmissao.setComando(comando.getId());
                    transmissao.setDtHrSubmissao(new Date());
                    transmissao.setEquipamento(equipamento);
                    transmissao.setIdUsuario(nFirebaseAuth.getUser().getUid());
                    nTransmissao.salvar(transmissao);
                }
            });
            btn18.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EComando comando = new EComando();
                    for (int i=0; i<comandos.size(); i++) {
                        EComando c = comandos.get(i);
                        if (c.getNome().equals("Power")) {
                            comando = c;
                        }
                    }
                    transmissao = new ETransmissao();
                    transmissao.setComando(comando.getId());
                    transmissao.setDtHrSubmissao(new Date());
                    transmissao.setEquipamento(equipamento);
                    transmissao.setIdUsuario(nFirebaseAuth.getUser().getUid());
                    nTransmissao.salvar(transmissao);
                }
            });
            btn17.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EComando comando = new EComando();
                    for (int i=0; i<comandos.size(); i++) {
                        EComando c = comandos.get(i);
                        if (c.getNome().equals("Power")) {
                            comando = c;
                        }
                    }
                    transmissao = new ETransmissao();
                    transmissao.setComando(comando.getId());
                    transmissao.setDtHrSubmissao(new Date());
                    transmissao.setEquipamento(equipamento);
                    transmissao.setIdUsuario(nFirebaseAuth.getUser().getUid());
                    nTransmissao.salvar(transmissao);
                }
            });
        }
    }

    private boolean igualTipoTV() {
        return modelo.getTipo().equals(EnumTipoEquipamento.TV);
    }

    private boolean igualTipoProjetor() {
        return modelo.getTipo().equals(EnumTipoEquipamento.PROJETOR);
    }
}