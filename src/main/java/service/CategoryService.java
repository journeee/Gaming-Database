package service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import jdk.jfr.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class CategoryService {
    private Firestore db = FirestoreClient.getFirestore();

    public ArrayList<Category> getCategories() throws ExecutionException, InterruptedException {
        Query query = db.collection("Category").orderBy("title");
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        ArrayList<Category> categories = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            categories.add(doc.toObject(Category.class));
        }
        return categories;
    }
}
}