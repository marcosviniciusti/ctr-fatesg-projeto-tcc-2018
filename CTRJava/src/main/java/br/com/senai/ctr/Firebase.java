package br.com.senai.ctr;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Firebase {

    public static void inicializarFirebase() {
        try {
            FileInputStream serviceAccount =
//                    new FileInputStream("firebase/irduino-ec7c4-firebase-adminsdk-k61mq-84974b6885.json");
                    new FileInputStream("firebase/projetotcc-989c0-firebase-adminsdk-zqhuq-56fc7391d5.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .setDatabaseUrl("https://irduino-ec7c4.firebaseio.com")
                    .setDatabaseUrl("https://projetotcc-989c0.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseReference getReference() {
        return FirebaseDatabase.getInstance().getReference("CTR");
    }

}
