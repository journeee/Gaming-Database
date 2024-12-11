package edu.famu.gsdatabase.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import edu.famu.gsdatabase.models.Followers;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class FollowersService {
    private LogsService logsService;
    private Firestore firestore;

    public FollowersService() {this.firestore = FirestoreClient.getFirestore();
    }



    public Followers documentSnapshotToFollowers(DocumentSnapshot document)throws ExecutionException, InterruptedException {
        if(document.exists()) {
            return document.toObject(Followers.class);
        }
        return null;
    }

    public List<Followers> getAllFollowers() throws ExecutionException, InterruptedException {
        CollectionReference followersCollection = firestore.collection("Followers");
        ApiFuture<QuerySnapshot> future = followersCollection.get();
        List<Followers> followerList = new ArrayList<>();
        for (DocumentSnapshot document : future.get().getDocuments()) {
            Followers followers = documentSnapshotToFollowers(document);
            if (followers != null) {
                followerList.add(followers);
            }
        }
        return followerList;
    }

    public Followers getFollowersByUser(String username) throws ExecutionException, InterruptedException {
        CollectionReference userCollection = firestore.collection("Followers");
        Query query = userCollection.whereEqualTo("user", username);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        if (!documents.isEmpty()) {
            DocumentSnapshot document = documents.get(0); // Assuming usernames are unique
            return documentSnapshotToFollowers(document);
        }
        return null;
    }

    public String createFollower(Followers followers) throws ExecutionException, InterruptedException {
        CollectionReference logCollection = firestore.collection("Followers");
        ApiFuture<DocumentReference> future = logCollection.add(followers);
        DocumentReference docRef = future.get();
        return docRef.getId();
    }
}
