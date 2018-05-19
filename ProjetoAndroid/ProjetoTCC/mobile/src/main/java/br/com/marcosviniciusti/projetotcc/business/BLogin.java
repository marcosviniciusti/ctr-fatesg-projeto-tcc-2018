package br.com.marcosviniciusti.projetotcc.business;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.marcosviniciusti.projetotcc.R;

public class BLogin extends AppCompatActivity {

    public boolean hasAuth(FirebaseAuth mAuth) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            if (user.getEmail() != null || !user.getEmail().isEmpty()) {
                return true;
            } else {
                return false;
            }
        }
        return  false;
    }

    public String[] isFormValid(String email, String password, Activity activity) {
        return new String[]{isEmailValid(email, activity), isPasswordValid(password, activity)};
    }

    private String isEmailValid(String email, Activity activity) {
        //TODO: Substitua isso pela sua própria lógica
        String erro = "";
        if (TextUtils.isEmpty(email)) {
            erro = (String) activity.getString(R.string.error_field_required);
        } else if (!email.contains("@")) {
            erro = (String) activity.getString(R.string.error_invalid_email);
        }

        return erro;
    }

    private String isPasswordValid(String password, Activity activity) {
        //TODO: Substitua isso pela sua própria lógica
        String erro = "";
        if (TextUtils.isEmpty(password)) {
            erro = (String) activity.getString(R.string.error_field_required);
        } else if (password.length() < 6) {
            erro = (String) activity.getString(R.string.error_invalid_password);
        }

        return erro;
    }
}