package br.com.marcosviniciusti.projetotcc.negocio;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import br.com.marcosviniciusti.projetotcc.R;
import br.com.marcosviniciusti.projetotcc.persistencia.PFirebaseAuth;

public class NFirebaseAuth {

    private String TAG = "NFirebaseAuth";
    private Activity activity;
    private PFirebaseAuth persistencia;

    public NFirebaseAuth(String default_web_client_id, Activity activity) {
        this.activity = activity;
        this.persistencia = new PFirebaseAuth(default_web_client_id, activity);
    }

    public boolean hasAuth() {
        try {
            FirebaseUser user = this.persistencia.getUser();
            if (user != null) {
                if (user.getEmail() != null || !user.getEmail().isEmpty()) {
                    return true;
                } else {
                    return false;
                }
            } else  return false;
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método hasAuth: "+error.getMessage(), error);
        }
        return  false;
    }

    public Task<AuthResult> criarConta(String email, String password) {
        try {
            if (validarFormulario(email, password)) {
                Task<AuthResult> task = this.persistencia
                        .createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "ERRO no método criarConta: " + task.getException()
                                            .getMessage(), task.getException());
                                    Toast.makeText(activity, "Falha na Autenticação.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                return task;
            }
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método criarConta: "+error.getMessage(), error);
        }
        return null;
    }

    public Task<AuthResult> onActivityResult(Intent data) {
        try {
            return this.persistencia
                    .onActivityResult(data)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                // Se sign in falhar, exibi uma mensagem para o usuário.
                                Log.w(TAG, "ERRO no método onActivityResult: " + task.getException()
                                        .getMessage(), task.getException());
                                Toast.makeText(activity, "Falha na Autenticação.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        } catch (Exception error) {
            Log.e(TAG, "ERRO no método onActivityResult: "+error.getMessage(), error);
        }
        return null;
    }

    public Task<AuthResult> signIn(String email, String password) {
        try {
            if (validarFormulario(email, password)) {
                return this.persistencia.signIn(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    // Se o login falhar, exibe uma mensagem para o usuário.
                                    Log.w(TAG, "ERRO no método sigIn: "+task.getException()
                                            .getMessage(), task.getException());

                                    if (task.getException().getMessage().equals(
                                            activity.getString(R.string.error_message_email))) {
                                        TextView txtEmail = activity.findViewById(R.id.edtEmail);
                                        txtEmail.setError(activity.getString(R.string.error_email_inexistente));
                                        txtEmail.requestFocus();
                                    } else if (task.getException().getMessage().equals(
                                            activity.getString(R.string.error_message_password))) {
                                        TextView txtPassword = activity.findViewById(R.id.edtSenha);
                                        txtPassword.setError(activity.getString(R.string.error_senha_incorreta));
                                        txtPassword.requestFocus();
                                    } else {
                                        Toast.makeText(activity, "Falha na Autenticação.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método signIn: "+error.getMessage(), error);
        }
        return null;
    }

    public void signInGoogle() {
        try {
            this.persistencia.signInGoogle();
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método signInGoogle: "+error.getMessage(), error);
        }
    }

    public Task<Void> signOut() {
        try {
            return this.persistencia.signOut();
        } catch (Exception error) {
            Log.e(TAG, "ERRO no mérodo signOut: "+error.getMessage(), error);
        }
        return null;
    }

    public FirebaseUser getUser() {
        return persistencia.getUser();
    }

    public int getRcSignIn() {
        return this.persistencia.getRcSignIn();
    }

    public boolean validarNome(String name) {
        if (name == null || name.isEmpty()) {
            EditText txtName = ((EditText) this.activity.findViewById(R.id.edtNome));
            txtName.setVisibility(View.VISIBLE);
            txtName.setError("Insira seu nome!");
            txtName.requestFocus();
            return false;
        }
        return true;
    }

    public boolean validarFormulario(String email, String password) {
        if (isEmailValid(email) && isPasswordValid(password)) {
            return true;
        }
        return false;
    }

    private boolean isEmailValid(String email) {
        //TODO: Substitua isso pela sua própria lógica
        if (TextUtils.isEmpty(email)) {
            EditText txtEmail = ((EditText) this.activity.findViewById(R.id.edtEmail));
            txtEmail.setError(activity.getString(R.string.error_campo_obrigatorio));
            txtEmail.requestFocus();
            return false;
        } else if (!email.contains("@")) {
            EditText txtEmail = ((EditText) this.activity.findViewById(R.id.edtEmail));
            txtEmail.setError(activity.getString(R.string.error_email_invalido));
            txtEmail.requestFocus();
            return false;
        }
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Substitua isso pela sua própria lógica
        if (TextUtils.isEmpty(password)) {
            EditText txtPassword = ((EditText) this.activity.findViewById(R.id.edtSenha));
            txtPassword.setError(activity.getString(R.string.error_campo_obrigatorio));
            txtPassword.requestFocus();
            return false;
        } else if (password.length() < 6) {
            EditText txtPassword = ((EditText) this.activity.findViewById(R.id.edtSenha));
            txtPassword.setError(activity.getString(R.string.error_senha_invalida));
            txtPassword.requestFocus();
            return false;
        }
        return true;
    }
}