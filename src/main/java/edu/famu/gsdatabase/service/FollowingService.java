package edu.famu.gsdatabase.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;
import edu.famu.gsdatabase.models.Following;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class FollowingService {
    private Firestore firestore;

    public FollowingService() {this.firestore = FirestoreClient.getFirestore();
    }

    public Following documentSnapshotToFollowing(DocumentSnapshot document)throws ExecutionException, InterruptedException {
        if(document.exists()){
            return document.toObject(Following.class);
        }

        return null;
    }

    public List<Following> getAllFollowing() throws ExecutionException, InterruptedException {
        CollectionReference followingCollection = firestore.collection("Following");
        ApiFuture<QuerySnapshot> future = followingCollection.get();
        List<Following> followingList = new ArrayList<>();
        for (DocumentSnapshot document : future.get().getDocuments()) {
            Following following = documentSnapshotToFollowing(document);
            if (following != null) {
                followingList.add(following);
            }
        }
        return followingList;
    }

    public Following getFollowingByUser(String username) throws ExecutionException, InterruptedException {
        CollectionReference userCollection = firestore.collection("Following");
        Query query = userCollection.whereEqualTo("user", username);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        if (!documents.isEmpty()) {
            DocumentSnapshot document = documents.get(0); // Assuming usernames are unique
            return documentSnapshotToFollowing(document);
        }
        return null;
    }

    public String createFollowing(Following following) throws ExecutionException, InterruptedException {
        CollectionReference folowingCollection = firestore.collection("Following");
        ApiFuture<DocumentReference> future = folowingCollection.add(following);
        DocumentReference docRef = future.get();
        return docRef.getId();
    }


    public void updateFollowing(String user, Following following) throws ExecutionException, InterruptedException {
        CollectionReference followingCollection = firestore.collection("Following");
         Query query = followingCollection.whereEqualTo("user", user);
         ApiFuture<QuerySnapshot> future = query.get();
         List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        if (!documents.isEmpty()) {
        DocumentReference documentReference = documents.get(0).getReference();
        documentReference.set(following);
        }
        else {
        createFollowing(following);
    }
    }
}
