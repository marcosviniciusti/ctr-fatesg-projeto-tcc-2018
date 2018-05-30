package br.com.marcosviniciusti.projetotcc.persistence;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import br.com.marcosviniciusti.projetotcc.R;
import br.com.marcosviniciusti.projetotcc.activities.AuthActivity;
import br.com.marcosviniciusti.projetotcc.activities.MainActivity;

public class PAuth {

    // Atributos da biblioteca do firebase.
    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;

    // Atributos da classe.
    private static final int RC_SIGN_IN = 9001;
    private AuthActivity activity;
    private String TAG;

    public PAuth(String default_web_client_id, AuthActivity activity, String TAG) {
        this.activity = activity;
        this.TAG = TAG;
        // Instacia atributo da biblioteca do Firebase.
        auth = FirebaseAuth.getInstance();
        // Configura o login pelo Google.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(default_web_client_id)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(activity, gso);
    }

    // Verifica se existe alguma autentificação e atualiza a interface de acordo.
    private boolean hasAuth() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            return true;
        } else {
            return false;
        }
    }

    // Cria uma conta de usuário do aplicativo.
    private void createUserWithEmailAndPassword(String email, String password) {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Resultado retornado do lançamento do Intent do GoogleSignInApi.getSignInIntent (...).
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In foi bem-sucedido, autenticado com o Firebase.
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Falha no Sign In do Google, atualize a interface do usuário adequadamente.
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            abrirTelaPrincipal(bAutenticacao.hasAuth(auth));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.drawer_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            abrirTelaPrincipal(false);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    // Entra na do usuário inserido no formulário.
    private void signIn(View view) {
        int i = view.getId();
        if (i == R.id.btnSignInGoogle) {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } else {
            // [START sign_in_with_email]
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                abrirTelaPrincipal(bAutenticacao.hasAuth(auth));
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
                                abrirTelaPrincipal(false);
                            }

                            // [START_EXCLUDE]
                            hideProgressDialog();
                            // [END_EXCLUDE]
                        }
                    });
            // [END sign_in_with_email]
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
                        abrirTelaPrincipal(false);
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
                        abrirTelaPrincipal(false);
                    }
                });
    }
}
