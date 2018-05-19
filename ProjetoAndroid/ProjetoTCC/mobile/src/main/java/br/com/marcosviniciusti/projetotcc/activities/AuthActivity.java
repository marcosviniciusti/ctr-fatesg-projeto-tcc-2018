package br.com.marcosviniciusti.projetotcc.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
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

<<<<<<< HEAD:ProjetoAndroid/ProjetoTCC/mobile/src/main/java/br/com/marcosviniciusti/projetotcc/activities/AuthActivity.java
import br.com.marcosviniciusti.projetotcc.R;
import br.com.marcosviniciusti.projetotcc.business.BLogin;
=======
import br.com.brainsflow.projetoctr.R;
import br.com.brainsflow.projetoctr.business.BLogin;
>>>>>>> 15df24ea2ca18f9dd57a2da169a893b3ed7dd566:ProjetoAndroid/ProjetoCTR/mobile/src/main/java/br/com/brainsflow/projetoctr/activities/AuthActivity.java

;

/**
 * Uma tela de bLogin que oferece bLogin via email / senha.
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
    private static final int RC_SIGN_IN = 9001;
    private BLogin bLogin;

    // Atributos da biblioteca do firebase.
    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth); // Referencia a tela XML.
        bind(); // Inicializa os atributos e referencias.
        creatEvent(); // Cria eventos.
    }

    // Inicializa os atributos.
    public void bind() {
        // Instacia atributo da biblioteca do Firebase.
        auth = FirebaseAuth.getInstance();
        // Configura o login pelo Google
        // [START]
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        //  [END]
        bLogin = new BLogin(); // Inicializa variável de negócios do login.
        // Inicializa os componentes da interface.
        //  [START]
        containerScrollView = findViewById(R.id.containerScrollView);
        widgetProgressBar = findViewById(R.id.widgetProgressBar);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnCreateButton = (Button) findViewById(R.id.btnCreateAccount);
        btnSignInButton = (Button) findViewById(R.id.btnSignIn);
        btnSignInGoogle = (SignInButton) findViewById(R.id.btnSignInGoogle);
        //  [END]
    }

    // Cria eventos;
    public void creatEvent() {
        // Cria o evento de ação de clicar no botão
        btnCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCount(); // tenta conectar no sistema.
            }
        });

        btnSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(view); // tenta conectar no sistema.
            }
        });

        // Cria o evento de ação de clicar no botão
        btnSignInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(view); // tenta conectar no sistema.
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Imprime no log o resultado da existencia de uma autentificação.
        Log.d(TAG, "METHOD: hasAuth(): "+bLogin.hasAuth(auth));
        checkAuth(bLogin.hasAuth(auth));
    }

    // Verifica se existe alguma autentificação e atualiza a interface de acordo.
    private void checkAuth(boolean user) {
        hideProgressDialog(); // Oculta pop-ups de carregamento.
        if (user) {
            showProgress(true); // Apresenta pop-ups de carregamento.
            startActivity(createIntent()); // Inicializa outra janela.
//            findViewById(R.id.verify_email_button).setEnabled(!user.isEmailVerified());
        } else {
            showProgress(false); // Oculta pop-ups de carregamento.
            txtEmail.setText(null);
            txtPassword.setText(null);
        }
    }

    // Cria uma intenção para enviar para outra janela.
    private Intent createIntent() {
//        Intent intent = new Intent(AuthActivity.this, RascunhoActivity.class);
//        Intent intent = new Intent(AuthActivity.this, MainScreenActivity.class);
        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
        return intent;
    }

    // Cria uma conta de usuário do aplicativo.
    private void createCount() {
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        Log.d(TAG, "METHOD: createCount - email: "+email+" password: "+password);

        String[] result = bLogin.isFormValid(email, password, this);
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
                                checkAuth(bLogin.hasAuth(auth));
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(AuthActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                checkAuth(false);
                            }

                            // [START_EXCLUDE]
                            hideProgressDialog();
                            // [END_EXCLUDE]
                        }
                    });
        }
    }

    // Envia E-mail de verificação.
    private void sendEmailVerification() {

        final FirebaseUser user = auth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        if (task.isSuccessful()) {
                            Toast.makeText(AuthActivity.this,
                                    "Verificação de e-mail enviado para: "+user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification: Falha - "+task.getException()
                                    .getMessage(), task.getException());
                            Toast.makeText(AuthActivity.this,
                                    "Falha no envio da verificação de e-mail.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
    }

    // [START onactivityresult]
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
                checkAuth(false);
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
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
                            checkAuth(bLogin.hasAuth(auth));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
<<<<<<< HEAD:ProjetoAndroid/ProjetoTCC/mobile/src/main/java/br/com/marcosviniciusti/projetotcc/activities/AuthActivity.java
                            Snackbar.make(findViewById(R.id.drawer_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
=======
                            Snackbar.make(findViewById(R.id.activity_rascunho), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
>>>>>>> 15df24ea2ca18f9dd57a2da169a893b3ed7dd566:ProjetoAndroid/ProjetoCTR/mobile/src/main/java/br/com/brainsflow/projetoctr/activities/AuthActivity.java
                            checkAuth(false);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]

    // Entra na do usuário inserido no formulário.
    private void signIn(View view) {
        int i = view.getId();
        if (i == R.id.btnSignInGoogle) {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } else {
            String email = txtEmail.getText().toString();
            String password = txtPassword.getText().toString();
            Log.d(TAG, "signIn:" + email);

            String[] result = bLogin.isFormValid(email, password, this);
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
                                    checkAuth(bLogin.hasAuth(auth));
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
                                    checkAuth(false);
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
                        checkAuth(false);
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
                        checkAuth(false);
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