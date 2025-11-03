package com.synko.config;

import com.google.auth.oauth2.GoogleCredencials;
import com.google.firebase.FirebaseApp;
import  com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.InputStream;

@Configuration
public class firebaseConfig {

    @Value("${firebase.credentials.path}")
    private String credentialsPath;

    @Value("${firebase.credentials.url}")
    private String credentialsUrl;

    @PostConstruct
    public void init () {
        try {
            InputStream serviceAccount = getClass().getResourceAsStream(credentialsPath);
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDataUser(databaseUrl)
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializaApp(options);
                System.err.println("Firebase inicializado com sucesso");
            }
        } catch (Exception e) {
            System.err.println("Erro ao inicializar o Firebase" + e.getMessage());
        }
    }

}
