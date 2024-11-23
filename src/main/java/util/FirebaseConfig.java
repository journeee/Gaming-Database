package util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initializeFirebase() {
        try {
            // Load the service account key from the resources folder
            InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("serviceAccountKey.json");

            if (serviceAccount == null) {
                throw new IllegalStateException("Service account key not found in resources folder.");
            }

            // Initialize Firebase
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://<gaming-database-2a402>.firebaseio.com") // Replace with your database URL
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase initialized successfully.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Firebase: " + e.getMessage(), e);
        }
    }
}

