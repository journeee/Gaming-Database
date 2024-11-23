package edu.famu.gsdatabase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.InputStream;

@SpringBootApplication
public class GsDatabaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(GsDatabaseApplication.class, args);

        try {
            // Load the service account key from the resources folder
            InputStream serviceAccount = GsDatabaseApplication.class.getClassLoader().getResourceAsStream("serviceAccountKey.json");

            if (serviceAccount == null) {
                throw new IllegalStateException("Service account key file not found in resources folder.");
            }

            // Initialize Firebase with project-specific details
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://gaming-database-2a402.firebaseio.com") // Your project-specific Realtime Database URL
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase for 'Gaming Database' initialized successfully.");
            }
        } catch (Exception e) {
            System.err.println("Error initializing Firebase: " + e.getMessage());
        }
    }
}
