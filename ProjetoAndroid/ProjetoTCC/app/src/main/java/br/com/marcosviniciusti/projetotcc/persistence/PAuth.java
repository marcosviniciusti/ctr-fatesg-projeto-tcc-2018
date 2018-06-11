package br.com.marcosviniciusti.projetotcc.persistence;

import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class PAuth {

    // Atributos da biblioteca do firebase.
    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;

    // Atributos da classe.
    private String TAG = "PAuth";
    private static final int RC_SIGN_IN = 9001;
    private Activity activity;

    // Construtor
    public PAuth(String default_web_client_id, Activity activity) {
        this.activity = activity;
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
    public boolean hasAuth() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            return true;
        } else {
            return false;
        }
    }

    // Cria uma conta de usuário do aplicativo.
    public Task<AuthResult> createUserWithEmailAndPassword(String email, String password) {
        return auth.createUserWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> onActivityResult(Intent data) throws Exception {
        // Resultado retornado do lançamento do Intent do GoogleSignInApi.getSignInIntent (...).
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        // Google Sign In foi bem-sucedido, autenticado com o Firebase.
        GoogleSignInAccount account = task.getResult(ApiException.class);
        return firebaseAuthWithGoogle(account);
    }

    public Task<AuthResult> firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        return auth.signInWithCredential(credential);
    }

    // Entra na do usuário inserido no formulário.
    public Task<AuthResult> signIn(String email, String password) {
        return auth.signInWithEmailAndPassword(email, password);
    }

    // Entra na do usuário inserido no formulário.
    public void signInGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Sair da conta do usuário no aplicativo.
    public Task<Void> signOut() {
        // Firebase sign out
        auth.signOut();

        // Google sign out
        return googleSignInClient.signOut();
    }

    public Task<Void> revokeAccess() {
        // Firebase sign out
        auth.signOut();

        // Google revoke access
        return googleSignInClient.revokeAccess();
    }

    public FirebaseUser getUser() {
        return auth.getCurrentUser();
    }

    public static int getRcSignIn() {
        return RC_SIGN_IN;
    }
}
