package br.com.marcosviniciusti.projetotcc.business;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import br.com.marcosviniciusti.projetotcc.R;
import br.com.marcosviniciusti.projetotcc.persistence.PAuth;

public class BAuth {

    private String TAG = "BAuth";
    private Activity activity;
    private PAuth persistencia;

    public BAuth(String default_web_client_id, Activity activity) {
        this.activity = activity;
        this.persistencia = new PAuth(default_web_client_id, activity);
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

    public Task<AuthResult> createCount(String email, String password) {
        try {
            if (isFormValid(email, password)) {
                Task<AuthResult> task = this.persistencia
                        .createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "ERRO no método createCount: " + task.getException()
                                            .getMessage(), task.getException());
                                    Toast.makeText(activity, "Falha na Autenticação.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                return task;
            }
        } catch (Exception error) {
            Log.e(TAG, "ERRO no método createCount: "+error.getMessage(), error);
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
            if (isFormValid(email, password)) {
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
                                        TextView txtEmail = activity.findViewById(R.id.txtEmail);
                                        txtEmail.setError(activity.getString(R.string.error_incorrect_email));
                                        txtEmail.requestFocus();
                                    } else if (task.getException().getMessage().equals(
                                            activity.getString(R.string.error_message_password))) {
                                        TextView txtPassword = activity.findViewById(R.id.txtPassword);
                                        txtPassword.setError(activity.getString(R.string.error_incorrect_password));
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

    public boolean isFormValid(String email, String password) {
        if (isEmailValid(email) && isPasswordValid(password)) {
            return true;
        }
        return false;
    }

    private boolean isEmailValid(String email) {
        //TODO: Substitua isso pela sua própria lógica
        if (TextUtils.isEmpty(email)) {
            TextView txtEmail = ((TextView) this.activity.findViewById(R.id.txtEmail));
            txtEmail.setError(activity.getString(R.string.error_field_required));
            txtEmail.requestFocus();
            return false;
        } else if (!email.contains("@")) {
            TextView txtEmail = ((TextView) this.activity.findViewById(R.id.txtEmail));
            txtEmail.setError(activity.getString(R.string.error_invalid_email));
            txtEmail.requestFocus();
            return false;
        }
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Substitua isso pela sua própria lógica
        if (TextUtils.isEmpty(password)) {
            TextView txtPassword = ((TextView) this.activity.findViewById(R.id.txtPassword));
            txtPassword.setError(activity.getString(R.string.error_field_required));
            txtPassword.requestFocus();
            return false;
        } else if (password.length() < 6) {
            TextView txtPassword = ((TextView) this.activity.findViewById(R.id.txtPassword));
            txtPassword.setError(activity.getString(R.string.error_invalid_password));
            txtPassword.requestFocus();
            return false;
        }
        return true;
    }
}