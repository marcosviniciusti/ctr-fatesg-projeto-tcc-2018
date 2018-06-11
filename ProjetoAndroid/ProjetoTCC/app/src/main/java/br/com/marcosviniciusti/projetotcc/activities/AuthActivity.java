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
import br.com.marcosviniciusti.projetotcc.business.BAuth;



/**
 * Uma tela de bAuth que oferece bAuth via email / senha.
 */
public class AuthActivity extends BaseActivity {

    // Referencias da UI.
    private View containerScrollView;
    private View widgetProgressBar;
    private EditText txtEmail;
    private EditText txtPassword;
    private Button btnCreateButton;
    private Button btnSignInButton;
    private SignInButton btnSignInGoogle;

    // Atributos da classe.
    private static final String TAG = "AuthActivity";

    // Atributos Firebase.
    private BAuth bAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Método onCreate");
        try {
            super.onCreate(savedInstanceState);
            bind(); // Inicializa os atributos e referencias.
            creatEvent(); // Cria eventos para os componentes da tela.
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
            setContentView(R.layout.activity_auth);

            // Referencia os componentes da interface.
            inicilizeComponetsInterface();

            // Inicializa atributos do Firebase.
            inicializeFirebase();

        } catch (Exception error) {
            // Caso, haja algum erro, será imprimido no log.
            Log.e(TAG, "ERRO no método bind: "+error.getMessage(), error);
        }
    }

    private void inicilizeComponetsInterface() {
        containerScrollView = findViewById(R.id.containerScrollView);
        widgetProgressBar = findViewById(R.id.widgetProgressBar);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnCreateButton = (Button) findViewById(R.id.btnCreateAccount);
        btnSignInButton = (Button) findViewById(R.id.btnSignIn);
        btnSignInGoogle = (SignInButton) findViewById(R.id.btnSignInGoogle);
    }

    private void inicializeFirebase() {
        // Inicializa objeto de negócios do login (passando o id do cliente web padrão e o contexto
        // da aplicação).
        bAuth = new BAuth(getString(R.string.default_web_client_id), this);
    }

    // Cria eventos;
    private void creatEvent() {
        Log.d(TAG, "Método creatEvent");
        try {
            // Cria o evento de ação de clicar no botão
            btnCreateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnCreateCount(); // tenta criar uma conta no sistema.
                }
            });

            btnSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnSignIn(view); // tenta conectar no sistema.
                }
            });

            btnSignInGoogle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnSignIn(view); // tenta conectar no sistema.
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
            checkAuth(bAuth.hasAuth());
        } catch (Exception error) {
            Log.e(TAG,"ERRO no método onStart: "+error.getMessage(), error);
        }
    }

    // Verifica se existe alguma autentificação e atualiza a interface de acordo.
    public void checkAuth(boolean hasAuth) {
        Log.d(TAG, "Método checkAuth");
        try {
            if (hasAuth) {
                openScreenMain();
            } else {
                showProgress(false);
                txtEmail.setText(null);
                txtPassword.setText(null);
            }
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método checkAuth: "+error.getMessage(), error);
        }
    }

    // Abri uma nova tela.
    private void openScreenMain() {
        Log.d(TAG, "Método openScreenMain");
        try {
            showProgress(true); // Apresenta pop-ups de carregamento.
            startActivity(createIntent(this, MainActivity.class)); // Inicializa outra janela.
            showProgress(false);
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método openScreenMain: "+error.getMessage(), error);
        }
    }

    // Cria uma intenção para enviar para outra janela.
    private Intent createIntent(Context context, Class aClass) {
        Log.d(TAG, "Método createIntent");
        try {
            Intent intent = new Intent(context, aClass);
            return intent;
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método createIntent: "+error.getMessage(), error);
        }
        return null;
    }

    // Cria uma conta de usuário do aplicativo.
    private void btnCreateCount() {
        Log.d(TAG, "Método btnCreateCount");
        try {
            String email = txtEmail.getText().toString();
            String password = txtPassword.getText().toString();

            if (!bAuth.isFormValid(email, password)) {
                return;
            }

            showProgressDialog();
            Task<AuthResult> task = bAuth.createCount(email, password);
            if (task != null) {
                task.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Entra com sucesso, atualiza a interface do usuário com as
                            // informações do usuário conectado.
                            Log.d(TAG, "btnCreateCount:success");
                            checkAuth(bAuth.hasAuth());
                        } else {
                            Log.d(TAG, "btnCreateCount:failure");
                            checkAuth(false);
                        }
                        hideProgressDialog();
                    }
                });
            }

        } catch (Exception error) {
            Log.e(TAG, "ERRO no método btnCreateCount: "+error.getMessage(), error);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "Método onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == bAuth.getRcSignIn()) {
            if (resultCode == RESULT_OK) {
                showProgressDialog();
                Task<AuthResult> task = bAuth.onActivityResult(data);
                if (task != null) {
                    if (task.isSuccessful()) {
                        // Sign in com successo, atualiza a interface do usuário com as informações do usuário conectado.
                        Log.d(TAG, "firebaseAuthWithGoogle:success");
                        checkAuth(bAuth.hasAuth());
                    } else {
                        checkAuth(false);
                    }
                }
                hideProgressDialog();
            }
        }
    }

    // Entra na do usuário inserido no formulário.
    private void btnSignIn(View view) {
        Log.d(TAG, "Método btnSignIn");
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        int i = view.getId();
        if (i == R.id.btnSignIn) {
            showProgressDialog();
            Task<AuthResult> task = bAuth.signIn(email, password);
            if (task != null) {
                if (task.isSuccessful()) {
                    // Entra com sucesso, atualiza a interface do usuário com as
                    // informações do usuário conectado.
                    Log.d(TAG, "signInWithEmail:success");
                    openScreenMain();
                }
            }
            hideProgressDialog();
        } else if (i == R.id.btnSignInGoogle) {
            bAuth.signInGoogle();
        }
    }

    // Sair da conta do usuário no aplicativo.
    private void signOut() {
        Log.d(TAG, "Método signOut");
        showProgressDialog();
        bAuth.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        checkAuth(false);
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