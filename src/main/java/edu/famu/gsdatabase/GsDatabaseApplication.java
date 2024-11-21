package edu.famu.gsdatabase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileInputStream;

@SpringBootApplication
public class GsDatabaseApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(GsDatabaseApplication.class, args);

        ClassLoader loader = GsDatabaseApplication.class.getClassLoader();
        File file = new File(loader.getResource("serviceAccountKey.json").getFile());
        FileInputStream serviceAccount = new FileInputStream(file.getAbsolutePath());

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }
}
