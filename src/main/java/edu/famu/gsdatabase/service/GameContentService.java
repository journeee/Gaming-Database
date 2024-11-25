package edu.famu.gsdatabase.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import edu.famu.gsdatabase.models.GameContent;
import edu.famu.gsdatabase.models.Bookmark;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class GameContentService {

    private final Firestore firestore;

    public GameContentService() {
        this.firestore = FirestoreClient.getFirestore();
    }

    /**
     * Post new game content.
     *
     * @param content The GameContent object to post.
     * @return The ID of the newly posted content.
     * @throws ExecutionException   If an error occurs while interacting with Firestore.
     * @throws InterruptedException If the thread is interrupted.
     */
    public String postContent(GameContent content) throws ExecutionException, InterruptedException {
        CollectionReference gameContentCollection = firestore.collection("GameContent");

        DocumentReference docRef = gameContentCollection.document();
        content.setContentId(docRef.getId());
        ApiFuture<WriteResult> writeResult = docRef.set(content);
        writeResult.get(); // Wait for operation to complete

        return docRef.getId();
    }

    /**
     * Retrieve all game content.
     *
     * @return A list of all GameContent objects.
     * @throws ExecutionException   If an error occurs while interacting with Firestore.
     * @throws InterruptedException If the thread is interrupted.
     */
    public List<GameContent> getAllContent() throws ExecutionException, InterruptedException {
        CollectionReference gameContentCollection = firestore.collection("GameContent");

        List<GameContent> contentList = new ArrayList<>();
        for (DocumentReference doc : gameContentCollection.listDocuments()) {
            GameContent content = doc.get().get().toObject(GameContent.class);
            if (content != null) {
                contentList.add(content);
            }
        }
        return contentList;
    }

    /**
     * Bookmark a game content.
     *
     * @param userId    The ID of the user bookmarking the content.
     * @param contentId The ID of the content to bookmark.
     * @return The ID of the bookmark entry.
     * @throws ExecutionException   If an error occurs while interacting with Firestore.
     * @throws InterruptedException If the thread is interrupted.
     */
    public String bookmarkContent(String userId, String contentId) throws ExecutionException, InterruptedException {
        CollectionReference bookmarksCollection = firestore.collection("Bookmarks");

        Bookmark bookmark = new Bookmark();
        bookmark.setUserId(Integer.parseInt(userId));
        bookmark.setContentId(Integer.parseInt(contentId));

        DocumentReference bookmarkRef = bookmarksCollection.document();
        ApiFuture<WriteResult> writeResult = bookmarkRef.set(bookmark);
        writeResult.get(); // Wait for operation to complete

        return bookmarkRef.getId();
    }
}
