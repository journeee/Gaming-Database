package util;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

public class utility {
    public class Utility {

        // Method to retrieve DocumentReference based on the provided ID
        public static DocumentReference retrieveDocumentReference(String collection, String id) {
            Firestore db = FirestoreClient.getFirestore();
            return db.collection(collection).document(id);
        }

    }
}
