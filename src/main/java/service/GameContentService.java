package service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import models.Bookmark;
import models.GameContent;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class GameContentService {
    private Firestore firestore;

    public GameContentService() {
        this.firestore = FirestoreClient.getFirestore();
    }

    // Upload new game-related content
    public String uploadContent(String userId, String title, String description, boolean isPremium) throws ExecutionException, InterruptedException {
        CollectionReference gameContent = firestore.collection("GameContent");
        DocumentReference docRef = gameContent.document();
        ApiFuture<WriteResult> writeResult = docRef.set(new GameContent());
        writeResult.get();
        return docRef.getId();
    }

    // Bookmark game content
    public String bookmarkContent(String userId, String contentId) throws ExecutionException, InterruptedException {
        DocumentReference bookmarkRef = firestore.collection("Bookmarks").document();
        ApiFuture<WriteResult> writeResult = bookmarkRef.set(new Bookmark());
        writeResult.get();
        return bookmarkRef.getId();
    }
}
