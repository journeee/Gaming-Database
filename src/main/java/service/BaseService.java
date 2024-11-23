package service;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

import java.util.concurrent.ExecutionException;

public abstract class BaseService<T> {
    protected Firestore firestore;

    public BaseService() {
        this.firestore = FirestoreClient.getFirestore();
    }

    public abstract T getById(String id) throws ExecutionException, InterruptedException;

    public abstract void save(T entity) throws ExecutionException, InterruptedException;
}
