package edu.famu.gsdatabase.service;


import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class ContentModeratorService {
    private Firestore firestore;

    public ContentModeratorService() {
        this.firestore = FirestoreClient.getFirestore();
    }

    // Review uploaded content for guidelines
    public String reviewContent(String contentId) throws ExecutionException, InterruptedException {
        DocumentReference contentRef = firestore.collection("GameContent").document(contentId);
        DocumentSnapshot document = contentRef.get().get();

        if (document.exists()) {
            return "Content reviewed: " + contentId;
        } else {
            return "Content not found!";
        }
    }

    // Flag inappropriate content
    public String flagContent(String contentId) throws ExecutionException, InterruptedException {
        DocumentReference contentRef = firestore.collection("GameContent").document(contentId);
        ApiFuture<WriteResult> writeResult = contentRef.update("flagged", true);
        writeResult.get();
        return "Content flagged successfully!";
    }

    // Remove inappropriate content
    public String removeContent(String contentId) throws ExecutionException, InterruptedException {
        DocumentReference contentRef = firestore.collection("GameContent").document(contentId);
        ApiFuture<WriteResult> deleteResult = contentRef.delete();
        deleteResult.get();
        return "Content removed successfully!";
    }
}

