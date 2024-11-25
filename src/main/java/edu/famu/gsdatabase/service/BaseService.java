package edu.famu.gsdatabase.service;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import edu.famu.gsdatabase.models.Game;
import edu.famu.gsdatabase.models.User;

import java.util.concurrent.ExecutionException;

public abstract class BaseService<T> {

    protected Firestore firestore;

    public BaseService() {
        // Lazy initialization of Firestore client
        if (FirestoreClient.getFirestore() == null) {
            throw new IllegalStateException("Firestore client is not initialized. Check Firebase initialization.");
        }
        this.firestore = FirestoreClient.getFirestore();
    }

    public abstract Game getById(String id) throws ExecutionException, InterruptedException;

    public abstract void save(User user) throws ExecutionException, InterruptedException;

    public abstract void save(Game game) throws ExecutionException, InterruptedException;
}
