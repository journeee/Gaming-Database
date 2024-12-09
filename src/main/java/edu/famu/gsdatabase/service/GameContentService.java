package edu.famu.gsdatabase.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import edu.famu.gsdatabase.models.Gamecontent;
import edu.famu.gsdatabase.models.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class GameContentService {
    private Firestore firestore;

    @Autowired
    private LogsService logsService;
    
    public GameContentService() {
        this.firestore = FirestoreClient.getFirestore();
    }

    public Gamecontent documentSnapshotToGameContent(DocumentSnapshot document) throws ExecutionException, InterruptedException {
        if (document.exists())
        { 
            // Users creator = new Users();
            // Object userRefObj = document.get("creator");

            // if (userRefObj instanceof DocumentReference) {
            //     DocumentReference userRef = (DocumentReference) userRefObj;
            //     DocumentSnapshot userSnapshot = userRef.get().get();
            //     if (userSnapshot.exists()) {
            //         creator.setUsername(userSnapshot.getString("username")); // set other User fields if needed
            //     }
            // } else if (userRefObj instanceof String) {
            //     creator.setUsername((String) userRefObj);
            // }

            return document.toObject(Gamecontent.class);
            /* return new Gamecontent(
                document.getId(),
                document.getString("description"),
                document.getDouble("rating").floatValue(),
                document.getString("status"),
                (List<String>) document.get("tags"),
                document.getString("title"),
                document.getString("type"),
                document.getLong("views"),
                document.getString("creator")
            ); */
        }
        return null;
    }

    public List<Gamecontent> getAllGameContent() throws ExecutionException, InterruptedException {
        CollectionReference contentCollection = firestore.collection("GameContent");
        ApiFuture<QuerySnapshot> future = contentCollection.get();
        List<Gamecontent> gamecontentList = new ArrayList<>();
        for (DocumentSnapshot document : future.get().getDocuments()) {
            Gamecontent gameContent = documentSnapshotToGameContent(document);
            if (gameContent != null) {
                gamecontentList.add(gameContent);
            }
        }
        return gamecontentList;
    }

    public WriteResult updateGameContent(String id, Map<String, Object> updateFields, String moderatorUsername) throws ExecutionException, InterruptedException {
        String[] allowed = {"status", "title", "exclusive"};
        List<String> allowedFields = List.of(allowed);
        Map<String, Object> formattedValues = updateFields.entrySet().stream()
                .filter(entry -> allowedFields.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        DocumentReference contentDoc = firestore.collection("GameContent").document(id);
        ApiFuture<WriteResult> result = contentDoc.update(formattedValues);
        logActivity(moderatorUsername, "Updated Content");
        return result.get();
    }

    public String createGameContent(Gamecontent gamecontent) throws ExecutionException, InterruptedException {
        CollectionReference contentCollection = firestore.collection("GameContent");
        ApiFuture<DocumentReference> future = contentCollection.add(gamecontent);
        DocumentReference docRef = future.get();
        logActivity(gamecontent.getCreator(),"Uploaded Content");
        return docRef.getId();
    }

    private void logActivity(String username, String activity) {
        Logs log = new Logs();
        log.setUser(username);
        log.setActivity(activity);
        try {
            logsService.createLog(log);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public WriteResult removeContent(String id) throws ExecutionException, InterruptedException {
        DocumentReference userRef = firestore.collection("GameContent").document(id);
        userRef.delete();
        return userRef.delete().get();
    }

    public Optional<Gamecontent> getGameContentById(String id) {
        try {
            DocumentSnapshot document = firestore.collection("GameContent").document(id).get().get();
            return document.exists() ? Optional.ofNullable(document.toObject(Gamecontent.class)) : Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
