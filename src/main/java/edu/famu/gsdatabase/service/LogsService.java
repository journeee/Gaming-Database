package edu.famu.gsdatabase.service;

import edu.famu.gsdatabase.models.Logs;
import org.springframework.stereotype.Service;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class LogsService {
    private Firestore firestore;

    public LogsService() {
        this.firestore = FirestoreClient.getFirestore();
    }

    public Logs documentSnapshotToLog(DocumentSnapshot document) throws ExecutionException, InterruptedException {
        if (document.exists()) {
            return document.toObject(Logs.class);
        }
        return null;
    }

    public List<Logs> getAllLogs() throws ExecutionException, InterruptedException {
        CollectionReference logCollection = firestore.collection("Logs");
        ApiFuture<QuerySnapshot> future = logCollection.get();
        List<Logs> logList = new ArrayList<>();
        for (DocumentSnapshot document : future.get().getDocuments()) {
            Logs log = documentSnapshotToLog(document);
            if (log != null) {
                logList.add(log);
            }
        }
       // System.out.println("Retrieved logs: " + logList);
        return logList;
    }

    public String createLog(Logs log) throws ExecutionException, InterruptedException {
        CollectionReference logCollection = firestore.collection("Logs");
        ApiFuture<DocumentReference> future = logCollection.add(log);
        DocumentReference docRef = future.get();
        return docRef.getId();
    }
}

