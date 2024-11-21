package service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.java.Log;
import models.User;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {
    private Firestore db = FirestoreClient.getFirestore();

    //used to log information in the console while testing
    private final Log logger = (Log) LogFactory.getLog(this.getClass());


    public ArrayList<User> getUsers() throws ExecutionException, InterruptedException {


        Query query = db.collection("User");
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        ArrayList<User> users = documents.size() > 0 ? new ArrayList<>() : null;
        for(QueryDocumentSnapshot doc : documents)
        {
            users.add(doc.toObject(User.class));
        }

        return users;
    }

    public User getUser(String userId) throws ExecutionException, InterruptedException {
        User user = null;

        DocumentReference doc = db.collection("User").document(userId);
        ApiFuture<DocumentSnapshot> future = doc.get();
        user = future.get().toObject(User.class);

        return user;
    }}
