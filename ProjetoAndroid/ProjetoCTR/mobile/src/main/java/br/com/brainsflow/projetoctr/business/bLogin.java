package br.com.brainsflow.projetoctr.business;

import android.text.TextUtils;
import android.widget.TextView;

import br.com.brainsflow.projetoctr.R;

public class bLogin {

    private boolean isFormValid(String email, String password) {
        return isEmailValid(email)
                && isPasswordValid(password);
    }

    private boolean isEmailValid(String email) {
        //TODO: Substitua isso pela sua própria lógica
        boolean valid;
        if (TextUtils.isEmpty(email)) {
            valid = false;
        } else if (!email.contains("@")) {
            valid = false;
        } else {
            valid = true;
        }

        return valid;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Substitua isso pela sua própria lógica
        boolean valid;
        if (TextUtils.isEmpty(password)) {
            valid = false;
        } else if (password.length() < 6) {
            valid = false;
        } else {
            valid = true;
        }

        return valid;
    }
}