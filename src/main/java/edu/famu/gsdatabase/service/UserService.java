package edu.famu.gsdatabase.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import edu.famu.gsdatabase.models.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

@Service
public class UserService {

    private final Firestore firestore;
    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    public UserService() {
        this.firestore = FirestoreClient.getFirestore();
    }

    /**
     * Create a new user in the Firestore database.
     *
     * @param user The user to create.
     * @return The ID of the newly created user.
     */
    public String createUser(BaseUser user) throws ExecutionException, InterruptedException, FirebaseAuthException {
        LOGGER.info("Creating user with email: " + user.getEmail());

        // Create user in Firebase Authentication
        String userId = FirebaseAuth.getInstance().createUser(new UserRecord.CreateRequest()
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .setDisplayName(user.getusername())
        ).getUid();

        user.setUserId(userId);

        // Save user to Firestore
        ApiFuture<WriteResult> future = firestore.collection("users").document(userId).set(user);
        future.get();  // wait for Firestore to complete write

        LOGGER.info("User created successfully with ID: " + userId);
        return userId;
    }
    /**
     * Fetch a user by their ID.
     *
     * @param userId The user ID.
     * @return The BaseUser object or null if not found.
     */
    public BaseUser getUserById(String userId) throws ExecutionException, InterruptedException {
        DocumentSnapshot snapshot = firestore.collection("users").document(userId).get().get();
        if (!snapshot.exists()) {
            throw new RuntimeException("User record not found in Firestore");
        }

        // Get the role field to determine the type of user to deserialize
        String role = snapshot.getString("role");
        if (role == null) {
            throw new RuntimeException("Role not found for user with ID: " + userId);
        }

        BaseUser user;
        switch (role) {
            case "Admin":
                user = snapshot.toObject(Administrator.class);
                break;
            case "Moderator":
                user = snapshot.toObject(Moderator.class);
                break;
            case "ContentCreator":
                user = snapshot.toObject(ContentCreator.class);
                break;
            default:
                user = snapshot.toObject(User.class);
                break;
        }

        if (user == null) {
            throw new RuntimeException("Failed to deserialize user with ID: " + userId);
        }

        return user;
    }

    /**
     * Fetch all users in the Firestore database.
     *
     * @return List of BaseUser objects.
     */
    public List<QueryDocumentSnapshot> getAllUsers() throws ExecutionException, InterruptedException {
        LOGGER.info("Fetching all users");

        CollectionReference usersCollection = firestore.collection("users");
        ApiFuture<QuerySnapshot> query = usersCollection.get();
        return query.get().getDocuments();
    }

    /**
     * Update the user information.
     *
     * @param userId    The user's ID.
     * @param updatedUser The updated user information.
     */
    public void updateUser(String userId, BaseUser updatedUser) throws ExecutionException, InterruptedException, FirebaseAuthException {
        LOGGER.info("Updating user with ID: " + userId);

        // Update user in Firebase Authentication if necessary
        UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(userId)
                .setEmail(updatedUser.getEmail())
                .setDisplayName(updatedUser.getusername());
        FirebaseAuth.getInstance().updateUser(request);

        // Update user in Firestore
        firestore.collection("users").document(userId).set(updatedUser).get();

        LOGGER.info("User updated successfully with ID: " + userId);
    }

    /**
     * Delete a user by their ID.
     *
     * @param userId The user's ID.
     */
    public void deleteUser(String userId) throws FirebaseAuthException, ExecutionException, InterruptedException {
        LOGGER.info("Deleting user with ID: " + userId);

        // Delete user in Firebase Authentication
        FirebaseAuth.getInstance().deleteUser(userId);

        // Delete user from Firestore
        firestore.collection("users").document(userId).delete().get();

        LOGGER.info("User deleted successfully with ID: " + userId);
    }

    /**
     * Activate or deactivate a user.
     *
     * @param userId   The user's ID.
     * @param isActive Boolean to indicate activation or deactivation.
     * @return
     */
    public boolean activateDeactivateUser(String userId, boolean isActive) throws ExecutionException, InterruptedException {
        LOGGER.info((isActive ? "Activating" : "Deactivating") + " user with ID: " + userId);

        DocumentReference userDocRef = firestore.collection("users").document(userId);
        userDocRef.update("isActive", isActive).get();

        LOGGER.info("User status updated successfully with ID: " + userId);
        return isActive;
    }

    // Additional methods to align with the use cases

    public void followContentCreator(String userId, String creatorId) throws ExecutionException, InterruptedException {
        LOGGER.info("User " + userId + " following content creator " + creatorId);

        DocumentReference userDocRef = firestore.collection("users").document(userId);
        userDocRef.update("followingCreators", FieldValue.arrayUnion(creatorId)).get();

        LOGGER.info("User followed content creator successfully with ID: " + creatorId);
    }

    public void uploadGameContent(String creatorId, GameContent gameContent) throws ExecutionException, InterruptedException {
        LOGGER.info("Content creator " + creatorId + " uploading game content");

        firestore.collection("gameContent").document().set(gameContent).get();

        LOGGER.info("Game content uploaded successfully by creator ID: " + creatorId);
    }

    public void flagInappropriateContent(String contentId, String reason) throws ExecutionException, InterruptedException {
        LOGGER.info("Flagging content with ID: " + contentId + " for reason: " + reason);

        DocumentReference contentDocRef = firestore.collection("gameContent").document(contentId);
        contentDocRef.update("isFlagged", true, "flagReason", reason).get();

        LOGGER.info("Content flagged successfully with ID: " + contentId);
    }

    public void bookmarkGameInformation(String userId, String gameInfoId) throws ExecutionException, InterruptedException {
        LOGGER.info("User " + userId + " bookmarking game information " + gameInfoId);

        DocumentReference userDocRef = firestore.collection("users").document(userId);
        userDocRef.update("bookmarkedGames", FieldValue.arrayUnion(gameInfoId)).get();

        LOGGER.info("Game information bookmarked successfully with ID: " + gameInfoId);
    }


    public String flagUser(String userId, boolean flagged) throws ExecutionException, InterruptedException {
        LOGGER.info("Flagging user with ID: " + userId + " as flagged: " + flagged);

        DocumentReference docRef = firestore.collection("users").document(userId);
        ApiFuture<WriteResult> writeResult = docRef.update("flagged", flagged);
        writeResult.get(); // Wait for Firestore to complete the write operation

        LOGGER.info("User with ID " + userId + " flagged status updated successfully.");

        return flagged ? "User flagged successfully." : "User unflagged successfully.";
    }
}

