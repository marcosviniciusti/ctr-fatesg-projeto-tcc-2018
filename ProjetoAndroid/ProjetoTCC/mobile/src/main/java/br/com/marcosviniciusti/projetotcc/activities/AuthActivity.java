package br.com.marcosviniciusti.projetotcc.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import br.com.marcosviniciusti.projetotcc.R;
import br.com.marcosviniciusti.projetotcc.business.BAuth;

;

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

    // Atributos da biblioteca do firebase.
    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;

    // Atributos da classe.
    private static final String TAG = "AuthActivity";
    private static final int RC_SIGN_IN = 9001;
    private BAuth bAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind(); // Inicializa os atributos e referencias.
        creatEvent(); // Cria eventos para os componentes da tela.
    }

    // Inicializa os atributos.
    public void bind() {
        // Referencia a tela XML.
        setContentView(R.layout.activity_auth);
        // Instacia atributo da biblioteca do Firebase.
        auth = FirebaseAuth.getInstance();
        // Configura o login pelo Google.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        // Inicializa objeto de negócios do login.
        bAuth = new BAuth();
        // Inicializa os componentes da interface.
        containerScrollView = findViewById(R.id.containerScrollView);
        widgetProgressBar = findViewById(R.id.widgetProgressBar);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnCreateButton = (Button) findViewById(R.id.btnCreateAccount);
        btnSignInButton = (Button) findViewById(R.id.btnSignIn);
        btnSignInGoogle = (SignInButton) findViewById(R.id.btnSignInGoogle);
    }

    // Cria eventos;
    public void creatEvent() {
        // Cria o evento de ação de clicar no botão
        btnCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCreateCount(); // tenta conectar no sistema.
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
    }

    @Override
    public void onStart() {
        super.onStart();
        // Imprime no log o resultado da existencia de uma autentificação.
        Log.d(TAG, "METHOD: hasAuth(): "+ bAuth.hasAuth(auth));
        openScreenMain(bAuth.hasAuth(auth));
    }

    // Verifica se existe alguma autentificação e atualiza a interface de acordo.
    private void openScreenMain(boolean user) {
        hideProgressDialog(); // Oculta pop-ups de carregamento.
        if (user) {
            showProgress(true); // Apresenta pop-ups de carregamento.
            startActivity(createIntent(this, MainActivity.class)); // Inicializa outra janela.
        } else {
            showProgress(false); // Oculta pop-ups de carregamento.
            txtEmail.setText(null);
            txtPassword.setText(null);
        }
    }

    // Cria uma intenção para enviar para outra janela.
    private Intent createIntent(Context context, Class aClass) {
        Intent intent = new Intent(context, aClass);
        return intent;
    }

    // Cria uma conta de usuário do aplicativo.
    private void btnCreateCount() {
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        Log.d(TAG, "METHOD: btnCreateCount - email: "+email+" password: "+password);

        String[] result = bAuth.isFormValid(email, password, this);
        if (!result[0].isEmpty()) {
            txtEmail.setError(result[0]);
            txtEmail.requestFocus();
            return;
        } else if (!result[1].isEmpty()) {
            txtPassword.setError(result[1]);
            txtPassword.requestFocus();
            return;
        } else {
            showProgressDialog();
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                openScreenMain(bAuth.hasAuth(auth));
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure - "+task.getException().getMessage(), task.getException());
                                Toast.makeText(AuthActivity.this, "Erro na Authenticação.",
                                        Toast.LENGTH_SHORT).show();
                                openScreenMain(false);
                            }

                            // [START_EXCLUDE]
                            hideProgressDialog();
                            // [END_EXCLUDE]
                        }
                    });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                openScreenMain(false);
                // [END_EXCLUDE]
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            openScreenMain(bAuth.hasAuth(auth));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.drawer_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            openScreenMain(false);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    // Entra na do usuário inserido no formulário.
    private void btnSignIn(View view) {
        int i = view.getId();
        if (i == R.id.btnSignInGoogle) {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } else {
            String email = txtEmail.getText().toString();
            String password = txtPassword.getText().toString();
            Log.d(TAG, "btnSignIn:" + email);

            String[] result = bAuth.isFormValid(email, password, this);
            if (!result[0].isEmpty()){
                txtEmail.setError(result[0]);
                txtEmail.requestFocus();
                return;
            } else if (!result[1].isEmpty()) {
                txtPassword.setError(result[1]);
                txtPassword.requestFocus();
                return;
            } else {

                showProgressDialog();

                // [START sign_in_with_email]
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    openScreenMain(bAuth.hasAuth(auth));
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure: " + task.getException().getMessage(), task.getException());
                                    if (task.getException().getMessage().equals(getString(R.string.error_message_email))) {
                                        txtEmail.setError(getString(R.string.error_incorrect_email));
                                        txtEmail.requestFocus();
                                    } else if (task.getException().getMessage().equals(getString(R.string.error_message_password))) {
                                        txtPassword.setError(getString(R.string.error_incorrect_password));
                                        txtPassword.requestFocus();
                                    } else {
                                        Toast.makeText(AuthActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    openScreenMain(false);
                                }

                                // [START_EXCLUDE]
                                hideProgressDialog();
                                // [END_EXCLUDE]
                            }
                        });
                // [END sign_in_with_email]
            }
        }
    }

    // Sair da conta do usuário no aplicativo.
    private void signOut() {
        // Firebase sign out
        auth.signOut();

        // Google sign out
        googleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        openScreenMain(false);
                    }
                });
    }

    private void revokeAccess() {
        // Firebase sign out
        auth.signOut();

        // Google revoke access
        googleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        openScreenMain(false);
                    }
                });
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