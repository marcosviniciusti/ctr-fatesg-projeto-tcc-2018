//package br.com.marcosviniciusti.projetotcc.teste;
//
//import android.animation.Animator;
//import android.animation.AnimatorListenerAdapter;
//import android.annotation.TargetApi;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.design.widget.Snackbar;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.google.android.gms.auth.api.signin.GoogleSignIn;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.common.SignInButton;
//import com.google.android.gms.common.api.ApiException;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.auth.AuthCredential;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.GoogleAuthProvider;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import br.com.marcosviniciusti.projetotcc.R;
//import br.com.marcosviniciusti.projetotcc.activities.BaseActivity;
//
//
///**
// * Uma tela de bAuth que oferece bAuth via email / senha.
// */
//public class AuthActivity extends BaseActivity {
//
//    // Referencias da UI.
//    private View containerScrollView;
//    private View widgetProgressBar;
//    private EditText txtName;
//    private EditText txtEmail;
//    private EditText txtPassword;
//    private Button btnCreateButton;
//    private Button btnSignInButton;
//    private SignInButton btnSignInGoogle;
//
//    // Atributos da classe.
//    private static final String TAG = "AuthActivity";
//
//    // Atributos Firebase.
//    private static final int RC_SIGN_IN = 9001;
//    private FirebaseAuth fAuth;
//    private GoogleSignInClient googleSignInClient;
//    private Auth auth;
//    FirebaseDatabase database;
//    private DatabaseReference ctrRef;
//    private DatabaseReference authRef;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        Log.d(TAG, "Método onCreate");
//        try {
//            super.onCreate(savedInstanceState);
//            bind(); // Inicializa os atributos e referencias.
//            creatEvent(); // Cria eventos para os componentes da tela.
//        } catch (Exception error) {
//            // Caso, haja algum erro, será imprimido no log.
//            Log.e(TAG, "ERRO no método onCreate: "+error.getMessage(), error);
//        }
//    }
//
//    // Inicializa os atributos e referencias.
//    private void bind() {
//        Log.d(TAG, "Método bind");
//        try {
//            // Referencia a tela XML.
//            setContentView(R.layout.activity_auth);
//
//            // Referencia os componentes da interface.
//            inicializeComponentsInterface();
//
//            inicializeFirebase();
//
//
//        } catch (Exception error) {
//            // Caso, haja algum erro, será imprimido no log.
//            Log.e(TAG, "ERRO no método bind: "+error.getMessage(), error);
//        }
//    }
//
//    private void inicializeFirebase() {
//        // Instacia atributo da biblioteca do Firebase.
//        fAuth = FirebaseAuth.getInstance();
//        // Configura o login pelo Google.
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//        googleSignInClient = GoogleSignIn.getClient(this, gso);
//        auth = new Auth();
//
//        // Inicializa a aplicação Firebase.
//        FirebaseApp.initializeApp(AuthActivity.this);
//
//        // Referencia a instância do banco de dados do Firebase.
//        database = FirebaseDatabase.getInstance();
//
//        // Permiti salvar, alterar arquivos em nuvem e também dentro do app.
//        database.setPersistenceEnabled(true);
//
//
//        // Referencia nós do banco de dados.
//        ctrRef = database.getInstance().getReference().child("CTR");
//        authRef = ctrRef.child("auth");
//    }
//
//    private void inicializeComponentsInterface() {
//        containerScrollView = findViewById(R.id.containerScrollView);
//        widgetProgressBar = findViewById(R.id.widgetProgressBar);
//        txtName = (EditText) findViewById(R.id.txtNome);
//        txtEmail = (EditText) findViewById(R.id.txtEmail);
//        txtPassword = (EditText) findViewById(R.id.txtSenha);
//        btnCreateButton = (Button) findViewById(R.id.btnCriarConta);
//        btnSignInButton = (Button) findViewById(R.id.btnEntrar);
//        btnSignInGoogle = (SignInButton) findViewById(R.id.btnEntrarPeloGoogle);
//    }
//
//    // Cria eventos;
//    private void creatEvent() {
//        Log.d(TAG, "Método creatEvent");
//        try {
//            // Cria o evento de ação de clicar no botão
//            btnCreateButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    btnCreateCount(); // tenta criar uma conta no sistema.
//                }
//            });
//
//            btnSignInButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    btnSignIn(view); // tenta conectar no sistema.
//                }
//            });
//
//            btnSignInGoogle.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    btnSignIn(view); // tenta conectar no sistema.
//                }
//            });
//        } catch (Exception error) {
//            Log.e(TAG, "ERRO no método createEvent: "+error.getMessage(), error);
//        }
//    }
//
//    @Override
//    public void onStart() {
//        Log.d(TAG, "Método onStart");
//        try {
//            super.onStart();
//            // Verifica se existe alguma autentificação e atualiza a interface de acordo.
//            checkAuth(fAuth.getCurrentUser());
//        } catch (Exception error) {
//            Log.e(TAG,"ERRO no método onStart: "+error.getMessage(), error);
//        }
//    }
//
//    // Verifica se existe alguma autentificação e atualiza a interface de acordo.
//    public void checkAuth(FirebaseUser user) {
//        Log.d(TAG, "Método validarAutenticacao");
//        hideProgressDialog();
//        try {
//            if (user != null) {
//                if (user.getEmail()!= null && !user.getEmail().isEmpty()) {
//                    openScreenMain();
//                } else {
//                    showProgress(false);
//                    txtEmail.setText(null);
//                    txtPassword.setText(null);
//                }
//            }
//        } catch (Exception error) {
//            Log.e(TAG, "ERRO no método validarAutenticacao: "+error.getMessage(), error);
//        }
//    }
//
//    // Abri uma nova tela.
//    private void openScreenMain() {
//        Log.d(TAG, "Método openScreenMain");
//        try {
//            showProgress(true); // Apresenta pop-ups de carregamento.
//            startActivity(createIntent(this, MainActivity.class)); // Inicializa outra janela.
//            showProgress(false);
//        } catch (Exception error) {
//            Log.e(TAG, "ERRO no método openScreenMain: "+error.getMessage(), error);
//        }
//    }
//
//    // Cria uma intenção para enviar para outra janela.
//    private Intent createIntent(Context context, Class aClass) {
//        Log.d(TAG, "Método createIntent");
//        try {
//            Intent intent = new Intent(context, aClass);
//            return intent;
//        } catch (Exception error) {
//            Log.e(TAG, "ERRO no método createIntent: "+error.getMessage(), error);
//        }
//        return null;
//    }
//
//    // Cria uma conta de usuário do aplicativo.
//    private void btnCreateCount() {
//        Log.d(TAG, "Método btnCreateCount");
//        try {
//
//            if (!hasName()) {
//                txtName.setVisibility(View.VISIBLE);
//                txtName.setError("Insira seu nome!");
//                return;
//            }
//
//            final String email = txtEmail.getText().toString();
//            String password = txtPassword.getText().toString();
//            final String name = txtName.getText().toString();
//            Log.d(TAG, "createAccount:" + email);
//
//            if(!isFormValid()) {
//                return;
//            }
//
//            showProgressDialog();
//
//            fAuth.createUserWithEmailAndPassword(email, password)
//                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                // Sign in success, update UI with the signed-in user's information
//                                Log.d(TAG, "createUserWithEmail:success");
//
//                                try {
//                                    auth.setId(fAuth.getCurrentUser().getUid());
//                                    auth.setEmail(email);
//                                    auth.setName(name);
//                                    authRef.child(auth.getId()).setValue(auth);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//
//                                checkAuth(fAuth.getCurrentUser());
//                            } else {
//                                // If sign in fails, display a message to the user.
//                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                                Toast.makeText(AuthActivity.this, "Authentication failed.",
//                                        Toast.LENGTH_SHORT).show();
//                                checkAuth(null);
//                            }
//
//                            // [START_EXCLUDE]
//                            hideProgressDialog();
//                            // [END_EXCLUDE]
//                        }
//                    });
//        } catch (Exception error) {
//            Log.e(TAG, "ERRO no método btnCreateCount: "+error.getMessage(), error);
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.d(TAG, "Método onActivityResult");
//        super.onActivityResult(requestCode, resultCode, data);
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                firebaseAuthWithGoogle(account);
//            } catch (ApiException e) {
//                // Google Sign In failed, update UI appropriately
//                Log.w(TAG, "Google sign in failed", e);
//                checkAuth(null);
//            }
//        }
//    }
//
//    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
//        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
//
//        showProgressDialog();
//
//        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
//        fAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");
//                            FirebaseUser user = fAuth.getCurrentUser();
//                            try {
//                                auth.setId(user.getUid());
//                                auth.setEmail(user.getEmail());
//                                auth.setName(user.getDisplayName());
//                                authRef.child(auth.getId()).setValue(auth);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            checkAuth(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Snackbar.make(findViewById(R.id.activity_auth), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                            checkAuth(null);
//                        }
//
//                        hideProgressDialog();
//                    }
//                });
//    }
//
//    // Entra na do usuário inserido no formulário.
//    private void btnSignIn(View view) {
//        Log.d(TAG, "Método btnSignIn");
//        int i = view.getId();
//        if (i == R.id.btnEntrarPeloGoogle) {
//            Intent signInIntent = googleSignInClient.getSignInIntent();
//            startActivityForResult(signInIntent, RC_SIGN_IN);
//        } else {
//            String email = txtEmail.getText().toString();
//            String password = txtPassword.getText().toString();
//            Log.d(TAG, "signIn:" + email);
//            if (!isFormValid()) {
//                return;
//            }
//
//            showProgressDialog();
//
//            fAuth.signInWithEmailAndPassword(email, password)
//                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                // Sign in success, update UI with the signed-in user's information
//                                Log.d(TAG, "signInWithEmail:success");
//                                FirebaseUser user = fAuth.getCurrentUser();
//                                checkAuth(user);
//                            } else {
//                                // If sign in fails, display a message to the user.
//                                Log.w(TAG, "signInWithEmail:failure: " + task.getException().getMessage(), task.getException());
//                                String message = "The password is invalid or the user does not have a password.";
//                                if (task.getException().getMessage().equals(message)) {
//                                    txtEmail.setError(getString(R.string.error_incorrect_password));
//                                    txtPassword.requestFocus();
//                                } else {
//                                    Toast.makeText(AuthActivity.this, "Authentication failed.",
//                                            Toast.LENGTH_SHORT).show();
//                                }
//                                checkAuth(null);
//                            }
//                            hideProgressDialog();
//                        }
//                    });
//        }
//    }
//
//    // Sair da conta do usuário no aplicativo.
//    private void signOut() {
//        // Firebase sign out
//        fAuth.signOut();
//
//        // Google sign out
//        googleSignInClient.signOut().addOnCompleteListener(this,
//                new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        checkAuth(null);
//                    }
//                });
//    }
//
//    private boolean isFormValid() {
//        return (isEmailValid(txtEmail.getText().toString())
//                && isPasswordValid(txtPassword.getText().toString()));
//    }
//
//    private boolean hasName() {
//        return !txtName.getText().toString().isEmpty();
//    }
//
//    private boolean isEmailValid(String email) {
//        //TODO: Substitua isso pela sua própria lógica
//        boolean valid;
//        if (TextUtils.isEmpty(email)) {
//            txtEmail.setError(getString(R.string.error_field_required));
//            txtEmail.requestFocus();
//            valid = false;
//        } else if (!email.contains("@")) {
//            txtEmail.setError(getString(R.string.error_invalid_email));
//            txtEmail.requestFocus();
//            valid = false;
//        } else {
//            txtEmail.setError(null);
//            valid = true;
//        }
//
//        return valid;
//    }
//
//    private boolean isPasswordValid(String password) {
//        //TODO: Substitua isso pela sua própria lógica
//        boolean valid;
//        if (TextUtils.isEmpty(password)) {
//            txtPassword.setError(getString(R.string.error_field_required));
//            txtPassword.requestFocus();
//            valid = false;
//        } else if (password.length() < 6) {
//            txtPassword.setError(getString(R.string.error_invalid_password));
//            txtPassword.requestFocus();
//            valid = false;
//        } else {
//            txtPassword.setError(null);
//            valid = true;
//        }
//
//        return valid;
//    }
//
//    /**
//     * Mostra a interface do usuário do progresso e oculta o formulário de Login.
//     */
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
//    private void showProgress(final boolean show) {
//        // No Honeycomb MR2, temos as APIs ViewPropertyAnimator, que permitem animações
//        // muito fáceis. Se disponível, use essas APIs para ativar o spinner de progresso.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
//
//            containerScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
//            containerScrollView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    containerScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
//                }
//            });
//
//            widgetProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
//            widgetProgressBar.animate().setDuration(shortAnimTime).alpha(
//                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    widgetProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
//                }
//            });
//        } else {
//            // As APIs do ViewPropertyAnimator não estão disponíveis, portanto, basta mostrar e
//            // ocultar os componentes relevantes da interface do usuário.
//            widgetProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
//            containerScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
//        }
//    }
//}