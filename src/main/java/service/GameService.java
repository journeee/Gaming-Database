package service;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import model.Game;
import models.RestGame;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class GameService {

    private Firestore db = FirestoreClient.getFirestore();

    public ArrayList<Game> getGames() throws ExecutionException, InterruptedException {
        Query query = db.collection("Game");
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        ArrayList<Game> games = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            games.add(getGame(doc));
        }
        return games;
    }

    public Game getGameById(String gameId) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection("Game").document(gameId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        return getGame(future.get());
    }

    private Game getGame(DocumentSnapshot doc) throws ExecutionException, InterruptedException {
        if (!doc.exists()) {
            throw new IllegalArgumentException("Document does not exist.");
        }

        // Extract fields from Firestore document
        String id = doc.getId();
        String title = doc.getString("title");
        String developer = doc.getString("developer");
        String publisher = doc.getString("publisher");
        Timestamp releaseDateTimestamp = doc.getTimestamp("releaseDate");
        Date releaseDate = releaseDateTimestamp != null ? releaseDateTimestamp.toDate() : null;
        String description = doc.getString("description");
        Boolean inStockBoolean = doc.getBoolean("inStock");
        boolean inStock = inStockBoolean != null && inStockBoolean;

        // Handle category references
        ArrayList<model.Category> categories = new ArrayList<>();
        List<DocumentReference> refs = (List<DocumentReference>) doc.get("categoryRefs");
        if (refs != null) {
            for (DocumentReference ref : refs) {
                ApiFuture<DocumentSnapshot> catFuture = ref.get();
                DocumentSnapshot catDoc = catFuture.get();
                if (catDoc.exists()) {
                    categories.add(catDoc.toObject(model.Category.class));
                }
            }
        }

        // Create and return a Game object
        return new Game(id, title, developer, publisher, releaseDate, description, inStock, categories);
    }

    public void createGame(RestGame restGame) throws ExecutionException, InterruptedException {
        ApiFuture<DocumentReference> future = db.collection("Game").add(restGame);
        future.get(); // Blocking call to ensure it's created
    }
}
