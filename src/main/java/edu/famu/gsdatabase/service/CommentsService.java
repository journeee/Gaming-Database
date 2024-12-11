package edu.famu.gsdatabase.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import edu.famu.gsdatabase.models.Comments;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class CommentsService {

    private Firestore firestore;

    public CommentsService(){
        this.firestore = FirestoreClient.getFirestore();
    }

    public Comments documentSnapshotToComments(DocumentSnapshot document) throws ExecutionException, InterruptedException{
        if (document.exists()) {
           // return document.toObject(Comments.class);
            return (new Comments(
                    document.getId(),
                    document.getString("content"),
                    document.getString("status"),
                    document.getTimestamp("uploadDate"),
                    null,
                    null

                    ));
        }
        return null;
    }

    public List<Comments> getAllComments() throws ExecutionException, InterruptedException {
        CollectionReference commentCollection = firestore.collection("Comments");
        ApiFuture<QuerySnapshot> future = commentCollection.get();
        List<Comments> commentList = new ArrayList<>();
        for (DocumentSnapshot document : future.get().getDocuments()) {
            Comments gameContent = documentSnapshotToComments(document);
            if (gameContent != null) {
                commentList.add(gameContent);
            }
        }
        return commentList;
    }
}
