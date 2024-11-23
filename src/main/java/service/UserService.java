package service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import models.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class UserService extends BaseService<User> {

    @Override
    public User getById(String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot snapshot = firestore.collection("users").document(id).get().get();
        return snapshot.exists() ? snapshot.toObject(User.class) : null;
    }

    @Override
    public void save(User user) throws ExecutionException, InterruptedException {
        firestore.collection("users").document(user.getUserId()).set(user).get();
    }

    public void activateDeactivateUser(String userId, boolean activate) throws ExecutionException, InterruptedException {
        firestore.collection("users").document(userId).update("isActive", activate).get();
    }

    public void deleteById(String userId) throws ExecutionException, InterruptedException {
        firestore.collection("users").document(userId).delete().get();
    }

    public List<User> getAllUsers() throws ExecutionException, InterruptedException {
        CollectionReference userCollection = firestore.collection("users");
        ApiFuture<QuerySnapshot> future = userCollection.get();
        List<User> users = new ArrayList<>();
        for (DocumentSnapshot document : future.get().getDocuments()) {
            User user = document.toObject(User.class);
            if (user != null) {
                users.add(user);
            }
        }
        return users;
    }

    public String createUser(User user) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("users").add(user).get();
        return docRef.getId();
    }

    public void updateUser(String userId, User user) throws ExecutionException, InterruptedException {
        firestore.collection("users").document(userId).set(user).get();
    }
}
