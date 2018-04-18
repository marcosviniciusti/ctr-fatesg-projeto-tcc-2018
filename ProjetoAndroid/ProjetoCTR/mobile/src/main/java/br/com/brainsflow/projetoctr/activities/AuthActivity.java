package br.com.brainsflow.projetoctr.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import br.com.brainsflow.projetoctr.R;

/**
 * Uma tela de login que oferece login via email / senha.
 */
public class AuthActivity extends BaseActivity {

    private static final String TAG = "AuthActivity";
    private static final String TAG_E = "EmailPassword";
    private static final String TAG_G = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleSignInClient pGoogleSignInClient;
    // [END declare_auth]

    // UI references.
    private View containerScrollView;
    private View widgetProgressBar;
    private TextView textStatusLabel;
    private TextView textDetailLabel;
    private EditText textFieldEmail;
    private EditText textFieldPassword;
    private Button buttonCreateButton;
    private Button buttonSignInButton;
    private SignInButton buttonSignInGoogle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        // Define o formulário de login.
        bind(); // Inicializa os atributos.
        creatEvent(); // Cria eventos.
    }

    // inicializa os atributos.
    public void bind() {
        mAuth = FirebaseAuth.getInstance();
        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]
        pGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        containerScrollView = findViewById(R.id.containerScrollView);
        widgetProgressBar = findViewById(R.id.widgetProgressBar);
        textStatusLabel = findViewById(R.id.textStatusLabel);
        textDetailLabel = findViewById(R.id.textDetailLabel);
        textFieldEmail = (EditText) findViewById(R.id.textFieldEmail);
        textFieldPassword = (EditText) findViewById(R.id.textFieldPassword);
        buttonCreateButton = (Button) findViewById(R.id.buttonCreateButton);
        buttonSignInButton = (Button) findViewById(R.id.buttonSignInButton);
        buttonSignInGoogle = (SignInButton) findViewById(R.id.buttonSignInGoogle);
    }

    // cria eventos;
    public void creatEvent() {

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");

                }
            }
        };

        // Cria o evento de ação de clicar no botão
        buttonCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCount(); // tenta conectar no sistema.
            }
        });

        buttonSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(view); // tenta conectar no sistema.
            }
        });

        // Cria o evento de ação de clicar no botão
        buttonSignInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(view); // tenta conectar no sistema.
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser user = mAuth.getCurrentUser();
        checkAuth(user);
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    //
    private void checkAuth(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            showProgress(true);
            startActivity(createIntent());

//            findViewById(R.id.verify_email_button).setEnabled(!user.isEmailVerified());
        } else {
            showProgress(false);
            textFieldEmail.setText(null);
            textFieldPassword.setText(null);
        }
    }

    private Intent createIntent() {
        Intent intent = new Intent(AuthActivity.this, RascunhoActivity.class);
        return intent;
    }

    private void createCount() {
        String email = textFieldEmail.getText().toString();
        String password = textFieldPassword.getText().toString();
        Log.d(TAG, "createAccount:" + email);

        if(!isFormValid()) {
            return;
        }

         showProgressDialog();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            checkAuth(mAuth.getCurrentUser());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(AuthActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            checkAuth(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    /*private void sendEmailVerification() {
        // Disable button
        findViewById(R.id.verify_email_button).setEnabled(false);

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        findViewById(R.id.verify_email_button).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(AuthActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(AuthActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }*/

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
                checkAuth(null);
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
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            checkAuth(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.activity_auth), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            checkAuth(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]

    private void signIn(View view) {
        int i = view.getId();
        if (i == R.id.buttonSignInGoogle) {
            Intent signInIntent = pGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } else {
            String email = textFieldEmail.getText().toString();
            String password = textFieldPassword.getText().toString();
            Log.d(TAG, "signIn:" + email);
            if (!isFormValid()) {
                return;
            }

        showProgressDialog();

            // [START sign_in_with_email]
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                checkAuth(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure: " + task.getException().getMessage(), task.getException());
                                String message = "The password is invalid or the user does not have a password.";
                                if (task.getException().getMessage().equals(message)) {
                                    textFieldPassword.setError(getString(R.string.error_incorrect_password));
                                    textFieldPassword.requestFocus();
                                } else {
                                    Toast.makeText(AuthActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                                checkAuth(null);
                            }

                            // [START_EXCLUDE]
                            if (!task.isSuccessful()) {
                                textStatusLabel.setText(R.string.error_auth_failed);
                            }
                        hideProgressDialog();
                            // [END_EXCLUDE]
                        }
                    });
            // [END sign_in_with_email]
        }
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        pGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        checkAuth(null);
                    }
                });
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        pGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        checkAuth(null);
                    }
                });
    }

    private boolean isFormValid() {
        return (isEmailValid(textFieldEmail.getText().toString())
                && isPasswordValid(textFieldPassword.getText().toString()));
    }

    private boolean isEmailValid(String email) {
        //TODO: Substitua isso pela sua própria lógica
        boolean valid;
        if (TextUtils.isEmpty(email)) {
            textFieldEmail.setError(getString(R.string.error_field_required));
            textFieldEmail.requestFocus();
            valid = false;
        } else if (!email.contains("@")) {
            textFieldEmail.setError(getString(R.string.error_invalid_email));
            textFieldEmail.requestFocus();
            valid = false;
        } else {
            textFieldEmail.setError(null);
            valid = true;
        }

        return valid;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Substitua isso pela sua própria lógica
        boolean valid;
        if (TextUtils.isEmpty(password)) {
            textFieldPassword.setError(getString(R.string.error_field_required));
            textFieldPassword.requestFocus();
            valid = false;
        } else if (password.length() < 6) {
            textFieldPassword.setError(getString(R.string.error_invalid_password));
            textFieldPassword.requestFocus();
            valid = false;
        } else {
            textFieldPassword.setError(null);
            valid = true;
        }

        return valid;
    }

    /**
     * Mostra a interface do usuário do progresso e oculta o formulário de login.
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