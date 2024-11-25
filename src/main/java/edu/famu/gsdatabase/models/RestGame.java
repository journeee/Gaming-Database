package edu.famu.gsdatabase.models;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.annotations.Nullable;
import com.google.protobuf.Timestamp;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class RestGame extends BaseGame {
    private ArrayList<DocumentReference> categoryRefs;

    public RestGame(@Nullable String gameId, String title, String developer, String publisher,
                    @Nullable Timestamp releaseDate, String description, boolean inStock, ArrayList<DocumentReference> categoryRefs) {
        super(gameId, title, developer, publisher, releaseDate, description, inStock);
        this.categoryRefs = categoryRefs;
    }

    public void setCategoryRefs(ArrayList<String> categoryIds) {
        Firestore db = FirestoreClient.getFirestore();
        this.categoryRefs = new ArrayList<>();
        for (String categoryId : categoryIds) {
            this.categoryRefs.add(db.collection("Category").document(categoryId));
        }
    }
}
