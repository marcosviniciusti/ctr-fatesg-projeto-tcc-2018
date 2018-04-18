package br.com.brainsflow.projetoctr.activities;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.brainsflow.projetoctr.R;

public class RascunhoActivity extends AppCompatActivity {

    // [START declare_auth]
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleSignInClient pGoogleSignInClient;
    // [END declare_auth]

    // UI references.
    private ImageView commonFoto;
    private TextView textViewName;
    private TextView textViewEmail;
    private Button buttonSignOutButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rascunho);
        bind();
        createEvents();
        loadData();
    }

    private void bind() {
        mAuth = FirebaseAuth.getInstance();
        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]
        pGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Fazemos a referencia do elemento que irá carregar a imagem.
        commonFoto = (ImageView) findViewById(R.id.commonFoto);
        textViewName = (TextView) findViewById(R.id.textStatusLabel);
        textViewEmail = (TextView) findViewById(R.id.textDetailLabel);
        buttonSignOutButton = (Button) findViewById(R.id.buttonSignOutButton);
    }

    private void createEvents() {
        buttonSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    private void signOut() {
        // Firebase sign out
        if (mAuth != null)
            mAuth.signOut();

        // Google sign out
        if (pGoogleSignInClient != null)
            pGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                    }
                });

    }

    @SuppressLint("ResourceType")
    private void loadData() {

        FirebaseUser user = mAuth.getCurrentUser();

        // Carregamos a imagem no elemento ImageView.
        // Aqui pegamos a referencia da imagem em string e a transformamos em uma referencia para int
        int imageResource = R.drawable.firebase_lockup_400;

        // Aqui pega a imagem e trás para tela referenciada
        Drawable res = ContextCompat.getDrawable(getApplicationContext(), imageResource);
        // Carregamos a imagem no elemento.
        commonFoto.setImageURI(user.getPhotoUrl());

        // Carregamos o nome e e-mail do usuário no TextField.
        textViewName.setText(user.getDisplayName());
        textViewEmail.setText(user.getEmail());
    }
}