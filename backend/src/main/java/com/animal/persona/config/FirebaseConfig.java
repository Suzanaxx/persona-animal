package com.animal.persona.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        // Najprej poskusi brati iz datoteke v resources/firebase
        InputStream serviceAccountStream = getClass().getClassLoader()
                .getResourceAsStream("firebase/serviceAccountKey.json");

        if (serviceAccountStream != null) {
            System.out.println("Using Firebase credentials from file: firebase-credentials.json");
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccountStream);
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();
            return FirebaseApp.initializeApp(options);
        }

        // Če datoteka ni na voljo, poskusi z okoljsko spremenljivko
        String firebaseCredentials = System.getenv("FIREBASE_CREDENTIALS");
        if (firebaseCredentials == null || firebaseCredentials.trim().isEmpty()) {
            throw new IllegalStateException("Neither firebase-credentials.json nor FIREBASE_CREDENTIALS environment variable is set");
        }

        System.out.println("Using Firebase credentials from environment variable");
        GoogleCredentials credentials = GoogleCredentials.fromStream(
                new ByteArrayInputStream(firebaseCredentials.getBytes(StandardCharsets.UTF_8))
        );
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build();
        return FirebaseApp.initializeApp(options);
    }
}