package br.com.marcosviniciusti.projetotcc.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import br.com.marcosviniciusti.projetotcc.R;
import br.com.marcosviniciusti.projetotcc.negocio.NDatabase;
import br.com.marcosviniciusti.projetotcc.negocio.NFirebaseAuth;
import br.com.marcosviniciusti.projetotcc.negocio.NUsuario;
import br.com.marcosviniciusti.projetotcc.entidade.EUsuario;


/**
 * Uma tela de nFirebaseAuth que oferece nFirebaseAuth via email / senha.
 */
public class AutenticacaoActivity extends BaseActivity {

    // Referencias da UI.
    private View containerScrollView;
    private View widgetProgressBar;
    private EditText edtNome;
    private EditText edtEmail;
    private EditText edtSenha;
    private Button btnCriarConta;
    private Button btnEntrar;
    private SignInButton btnEntrarPeloGoogle;

    // Atributos da classe.
    private static final String TAG = "AutenticacaoActivity";
    private EUsuario usuario;

    // Atributos Firebase.
    private NFirebaseAuth nFirebaseAuth;
    private NDatabase nDatabase;
    private NUsuario nUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Método onCreate");
        try {
            super.onCreate(savedInstanceState);
            inicializar(); // Inicializa os atributos e referencias.
            criarEventos(); // Cria eventos para os componentes da tela.
        } catch (Exception error) {
            // Caso, haja algum erro, será imprimido no log.
            Log.e(TAG, "ERRO no método onCreate: "+error.getMessage(), error);
        }
    }

    // Inicializa os atributos e referencias.
    private void inicializar() {
        Log.d(TAG, "Método inicializar");
        try {
            // Referencia a tela XML.
            setContentView(R.layout.activity_autenticacao);

            inicilizarComponentesDeInterface();
            inicializarAtributosDaClasse();
            inicializarFirebase();

        } catch (Exception error) {
            // Caso, haja algum erro, será imprimido no log.
            Log.e(TAG, "ERRO no método inicializar: "+error.getMessage(), error);
        }
    }

    private void inicilizarComponentesDeInterface() {
        containerScrollView = findViewById(R.id.containerScrollView);
        widgetProgressBar = findViewById(R.id.widgetProgressBar);
        edtNome = (EditText) findViewById(R.id.edtNome);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtSenha = (EditText) findViewById(R.id.edtSenha);
        btnCriarConta = (Button) findViewById(R.id.btnCriarConta);
        btnEntrar = (Button) findViewById(R.id.btnEntrar);
        btnEntrarPeloGoogle = (SignInButton) findViewById(R.id.btnEntrarPeloGoogle);
    }

    private void inicializarAtributosDaClasse() {
        usuario = new EUsuario();
    }

    private void inicializarFirebase() {
        // Inicializa objeto de negócios do login (passando o id do cliente web padrão e o contexto
        // da aplicação).
        nFirebaseAuth = new NFirebaseAuth(getString(R.string.default_web_client_id), this);

        nDatabase = new NDatabase(AutenticacaoActivity.this);
        nUsuario = new NUsuario();
    }

    // Cria eventos;
    private void criarEventos() {
        Log.d(TAG, "Método criarEventos");
        try {
            // Cria o evento de ação de clicar no botão
            btnCriarConta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnCriarConta(); // tenta criar uma conta no sistema.
                }
            });

            btnEntrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnEntrar(view); // tenta conectar no sistema.
                }
            });

            btnEntrarPeloGoogle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnEntrar(view); // tenta conectar no sistema.
                }
            });
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método createEvent: "+error.getMessage(), error);
        }
    }

    @Override
    public void onStart() {
        Log.d(TAG, "Método onStart");
        try {
            super.onStart();
            // Verifica se existe alguma autentificação e atualiza a interface de acordo.
            verificarSeTemAutenticacao(nFirebaseAuth.hasAuth());
        } catch (Exception error) {
            Log.e(TAG,"ERRO no método onStart: "+error.getMessage(), error);
        }
    }

    // Verifica se existe alguma autentificação e atualiza a interface de acordo.
    public void verificarSeTemAutenticacao(boolean existeAutenticacao) {
        Log.d(TAG, "Método verificarSeTemAutenticacao");
        try {
            if (existeAutenticacao) {
                abrirTelaPrincipal();
            } else {
                showProgress(false);
            }
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método verificarSeTemAutenticacao: "+error.getMessage(), error);
        }
    }

    private void abrirTelaPrincipal() {
        Log.d(TAG, "Método abrirTelaPrincipal");
        try {
            showProgress(true); // Apresenta pop-ups de carregamento.
            startActivity(criarIntent(this, MainActivity.class)); // Inicializa outra janela.
            showProgress(false);
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método abrirTelaPrincipal: "+error.getMessage(), error);
        }
    }

    // Cria uma intenção para enviar para outra janela.
    private Intent criarIntent(Context context, Class aClass) {
        Log.d(TAG, "Método criarIntent");
        try {
            Intent intent = new Intent(context, aClass);
            return intent;
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método criarIntent: "+error.getMessage(), error);
        }
        return null;
    }

    // Cria uma conta de usuário do aplicativo.
    private void btnCriarConta() {
        Log.d(TAG, "Método btnCriarConta");
        try {
            final String nome = edtNome.getText().toString();
            String email = edtEmail.getText().toString();
            String senha = edtSenha.getText().toString();

            if (!nFirebaseAuth.validarNome(nome) || !nFirebaseAuth.validarFormulario(email, senha)) {
                return;
            }

            criarConta(nome, email, senha);

        } catch (Exception error) {
            Log.e(TAG, "ERRO no método btnCriarConta: "+error.getMessage(), error);
        }
    }

    private void criarConta(final String nome, String email, String senha) {
        Task<AuthResult> task = nFirebaseAuth.criarConta(email, senha);
        if (task != null) {
            showProgressDialog();
            task.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Entra com sucesso, atualiza a interface do usuário com as
                        // informações do usuário conectado.
                        Log.d(TAG, "btnCriarConta:sucesso");
                        salvarUsuario(nome);
                        desaparecerCampoNome();
                        verificarSeTemAutenticacao(nFirebaseAuth.hasAuth());
                    } else {
                        Log.d(TAG, "btnCriarConta:failure");
                        verificarSeTemAutenticacao(false);
                    }
                    hideProgressDialog();
                }
            });
        }
    }

    private void salvarUsuario(String nome) {
        try {
            if (nome == null || nome.isEmpty()) {
                usuario.setId(nFirebaseAuth.getUser().getUid());
                usuario.setNome(nFirebaseAuth.getUser().getDisplayName());
                usuario.setEmail(nFirebaseAuth.getUser().getEmail());
                nUsuario.salvar(usuario);
            } else {
                usuario.setId(nFirebaseAuth.getUser().getUid());
                usuario.setNome(nome);
                usuario.setEmail(nFirebaseAuth.getUser().getEmail());
                nUsuario.salvar(usuario);
            }
        } catch (Exception error) {
            Log.e(TAG, "ERRO na inserção do objeto auth: "+error.getMessage());
        }
    }

    private void desaparecerCampoNome() {
        AutenticacaoActivity.this.edtNome.setText("");
        AutenticacaoActivity.this.edtNome.setError(null);
        AutenticacaoActivity.this.edtNome.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "Método onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == nFirebaseAuth.getRcSignIn()) {
            if (resultCode == RESULT_OK) {


                Task<AuthResult> task = nFirebaseAuth.onActivityResult(data);
                if (task != null) {
                    showProgressDialog();
                    task.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Entra com sucesso, atualiza a interface do usuário com as
                                // informações do usuário conectado.
                                Log.d(TAG, "btnCriarConta:success");
                                salvarUsuario(null);
                                desaparecerCampoNome();
                                verificarSeTemAutenticacao(nFirebaseAuth.hasAuth());
                            } else {
                                Log.d(TAG, "btnCriarConta:failure");
                                verificarSeTemAutenticacao(false);
                            }
                            hideProgressDialog();
                        }
                    });
                }
            }
        }
    }

    // Entra na do usuário inserido no formulário.
    private void btnEntrar(View view) {
        Log.d(TAG, "Método btnEntrar");
        String email = edtEmail.getText().toString();
        String password = edtSenha.getText().toString();
        int i = view.getId();
        if (i == R.id.btnEntrar) {

            Task<AuthResult> task = nFirebaseAuth.signIn(email, password);
            if (task != null) {
                showProgressDialog();
                task.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Entra com sucesso, atualiza a interface do usuário com as
                            // informações do usuário conectado.
                            Log.d(TAG, "btnCriarConta:success");
                            desaparecerCampoNome();
                            verificarSeTemAutenticacao(nFirebaseAuth.hasAuth());
                        } else {
                            Log.d(TAG, "btnCriarConta:failure");
                            verificarSeTemAutenticacao(false);
                        }
                        hideProgressDialog();
                    }
                });
            }
        } else if (i == R.id.btnEntrarPeloGoogle) {
            nFirebaseAuth.signInGoogle();
        }
    }

    // Sair da conta do usuário no aplicativo.
    private void signOut() {
        Log.d(TAG, "Método signOut");
        showProgressDialog();
        nFirebaseAuth.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        verificarSeTemAutenticacao(false);
                    }
                });
        hideProgressDialog();
    }

    /**
     * Mostra a interface do usuário do progresso e oculta o formulário de Login.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // No Honeycomb MR2, temos as APIs ViewPropertyAnimator, que permitem animações
        // muito fáceis. Se disponível, use essas APIs para ativar o spinner de progresso.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            containerScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
            containerScrollView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    containerScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            widgetProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            widgetProgressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    widgetProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // As APIs do ViewPropertyAnimator não estão disponíveis, portanto, basta mostrar e
            // ocultar os componentes relevantes da interface do usuário.
            widgetProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            containerScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}