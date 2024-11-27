package edu.famu.gsdatabase.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import edu.famu.gsdatabase.models.GameContent;
import edu.famu.gsdatabase.models.Bookmark;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

        ApiFuture<QuerySnapshot> future = gameContentCollection.get();
        List<GameContent> contentList = new ArrayList<>();
        for (DocumentSnapshot document : future.get().getDocuments()) {
            GameContent content = document.toObject(GameContent.class);
            if (content != null) {
                contentList.add(content);
            }
        }
        return contentList;
    }

    /**
     * Retrieve game content by ID.
     *
     * @param contentId The ID of the content to retrieve.
     * @return The GameContent object or null if not found.
     * @throws ExecutionException   If an error occurs while interacting with Firestore.
     * @throws InterruptedException If the thread is interrupted.
     */
    public GameContent getContentById(String contentId) throws ExecutionException, InterruptedException {
        DocumentReference contentDoc = firestore.collection("GameContent").document(contentId);
        ApiFuture<DocumentSnapshot> future = contentDoc.get();
        DocumentSnapshot document = future.get();
        return document.exists() ? document.toObject(GameContent.class) : null;
    }

    /**
     * Update game content.
     *
     * @param contentId    The ID of the content to update.
     * @param updateFields The fields to update.
     * @return The WriteResult of the update operation.
     * @throws ExecutionException   If an error occurs while interacting with Firestore.
     * @throws InterruptedException If the thread is interrupted.
     */
    public WriteResult updateContent(String contentId, Map<String, Object> updateFields) throws ExecutionException, InterruptedException {
        String[] allowed = {"title", "description", "tags"};

        List<String> allowedFields = List.of(allowed);
        Map<String, Object> formattedValues = updateFields.entrySet().stream()
                .filter(entry -> allowedFields.contains(entry.getKey()))
                .collect(java.util.stream.Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        DocumentReference contentDoc = firestore.collection("GameContent").document(contentId);
        ApiFuture<WriteResult> result = contentDoc.update(formattedValues);
        return result.get();
    }

    /**
     * Remove game content.
     *
     * @param contentId The ID of the content to remove.
     * @return The WriteResult of the remove operation.
     * @throws ExecutionException   If an error occurs while interacting with Firestore.
     * @throws InterruptedException If the thread is interrupted.
     */
    public WriteResult removeContent(String contentId) throws ExecutionException, InterruptedException {
        DocumentReference contentRef = firestore.collection("GameContent").document(contentId);
        return contentRef.delete().get();
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

    /**
     * Flag content for review (Moderator functionality).
     *
     * @param contentId The ID of the content to flag.
     * @return A success message indicating the content has been flagged.
     * @throws ExecutionException   If an error occurs while interacting with Firestore.
     * @throws InterruptedException If the thread is interrupted.
     */
    public String flagContent(String contentId) throws ExecutionException, InterruptedException {
        DocumentReference contentDoc = firestore.collection("GameContent").document(contentId);
        ApiFuture<WriteResult> writeResult = contentDoc.update("flagged", true);
        writeResult.get(); // Wait for operation to complete
        return "Content flagged successfully";
    }

    /**
     * Review flagged content (Moderator functionality).
     *
     * @param contentId The ID of the content to review.
     * @return A success message indicating the content has been reviewed.
     * @throws ExecutionException   If an error occurs while interacting with Firestore.
     * @throws InterruptedException If the thread is interrupted.
     */
    public String reviewContent(String contentId) throws ExecutionException, InterruptedException {
        DocumentReference contentDoc = firestore.collection("GameContent").document(contentId);
        ApiFuture<WriteResult> writeResult = contentDoc.update("reviewed", true);
        writeResult.get(); // Wait for operation to complete
        return "Content reviewed successfully";
    }
}
