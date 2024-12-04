package edu.famu.gsdatabase.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import edu.famu.gsdatabase.models.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
     * Authenticate a user by identifier (username or email) and password.
     *
     * @param identifier The username or email of the user.
     * @param password   The password of the user.
     * @return The authenticated BaseUser object, or null if authentication fails.
     */
    public BaseUser authenticateByIdentifier(String identifier, String password) throws ExecutionException, InterruptedException {
        validateInput(identifier, password);
        identifier = identifier.toLowerCase(); // Normalize identifier to lowercase
        LOGGER.info("Authentication attempt for identifier: " + identifier);

        BaseUser user = findUserByIdentifier(identifier);
        if (user == null) {
            LOGGER.warning("User not found with identifier: " + identifier);
            return null;
        }

        LOGGER.info("Attempting to verify password for user: " + user.getUsername());
        // Direct password comparison
        if (password.equals(user.getPassword())) {
            LOGGER.info("Authentication successful for user: " + user.getUsername());
            return user;
        } else {
            LOGGER.warning("Password mismatch for user: " + user.getUsername());
        }

        LOGGER.warning("Authentication failed for identifier: " + identifier);
        return null;
    }

    /**
     * Create a new user.
     *
     * @param user The user object to create.
     * @return The ID of the newly created user.
     */
    public String createUser(BaseUser user) throws ExecutionException, InterruptedException {
        validateUser(user);
        LOGGER.info("Creating user with username: " + user.getUsername());

        setUserIdentifier(user);
        // Save password as plain text (not recommended for production)
        writeToFirestore(user.getUsername(), user);
        return user.getUsername();
    }

    /**
     * Update an existing user's information.
     *
     * @param userId The ID of the user to update.
     * @param user   The updated user object.
     */
    public void updateUser(String userId, BaseUser user) throws ExecutionException, InterruptedException {
        LOGGER.info("Updating user with ID: " + userId);
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(user.getPassword());
        }
        writeToFirestore(userId, user);
    }

    /**
     * Activate or deactivate a user.
     *
     * @param userId  The ID of the user to update.
     * @param isActive True to activate, false to deactivate.
     */
    public void activateDeactivateUser(String userId, boolean isActive) throws ExecutionException, InterruptedException {
        updateField(userId, "isActive", isActive);
    }

    /**
     * Get all inactive users.
     *
     * @return List of all inactive users.
     */
    public List<BaseUser> getInactiveUsers() throws ExecutionException, InterruptedException {
        return getUsersByField("isActive", false);
    }

    /**
     * Flag a user for review.
     *
     * @param userId The ID of the user to flag.
     * @return A message indicating the user has been flagged.
     */
    public String flagUser(String userId) throws ExecutionException, InterruptedException {
        updateField(userId, "flagged", true);
        return "User flagged successfully";
    }

    /**
     * Promote a user to Admin.
     *
     * @param userId The ID of the user to promote.
     */
    public void promoteToAdmin(String userId) throws ExecutionException, InterruptedException {
        updateField(userId, "role", "Admin");
    }

    /**
     * Get all users from Firestore, resolving them to their specific subclasses.
     *
     * @return List of all users.
     */
    public List<BaseUser> getAllUsers() throws ExecutionException, InterruptedException {
        LOGGER.info("Fetching all users.");
        List<BaseUser> users = new ArrayList<>();
        List<QueryDocumentSnapshot> documents = firestore.collection("users").get().get().getDocuments();

        for (QueryDocumentSnapshot document : documents) {
            BaseUser user = resolveUserByRole(document.getString("role"), document);
            if (user != null) {
                users.add(user);
            }
        }

        LOGGER.info("Total users found: " + users.size());
        return users;
    }

    /**
     * Get a user by their ID, resolving them to their specific subclass.
     *
     * @param userId The user ID.
     * @return The BaseUser object or null if not found.
     */
    public BaseUser getById(String userId) throws ExecutionException, InterruptedException {
        LOGGER.info("Fetching user by ID: " + userId);
        DocumentSnapshot snapshot = firestore.collection("users").document(userId).get().get();
        if (!snapshot.exists()) {
            LOGGER.warning("User not found with ID: " + userId);
            return null;
        }
        return resolveUserByRole(snapshot.getString("role"), snapshot);
    }

    /**
     * Delete a user by their ID.
     *
     * @param userId The ID of the user to delete.
     */
    public void deleteById(String userId) throws ExecutionException, InterruptedException {
        LOGGER.info("Deleting user with ID: " + userId);
        ApiFuture<WriteResult> result = firestore.collection("users").document(userId).delete();
        LOGGER.info("User deleted: " + userId + ". Update time: " + result.get().getUpdateTime());
    }

    /**
     * Finds a user by their identifier (username or email).
     *
     * @param identifier The username or email of the user.
     * @return The BaseUser object or null if not found.
     */
    private BaseUser findUserByIdentifier(String identifier) throws ExecutionException, InterruptedException {
        CollectionReference usersCollection = firestore.collection("users");

        // Query Firestore for the user with the provided identifier
        ApiFuture<QuerySnapshot> query = usersCollection.whereEqualTo("username", identifier).get();
        List<QueryDocumentSnapshot> documents = query.get().getDocuments();

        if (documents.isEmpty()) {
            LOGGER.info("No user found by username, trying email.");
            query = usersCollection.whereEqualTo("email", identifier).get();
            documents = query.get().getDocuments();

            if (documents.isEmpty()) {
                LOGGER.warning("User not found with identifier: " + identifier);
                return null; // User not found
            }
        }

        DocumentSnapshot document = documents.get(0);
        return resolveUserByRole(document.getString("role"), document);
    }

    /**
     * Resolves a user to its specific subclass based on the role.
     *
     * @param role     The role of the user.
     * @param snapshot The Firestore snapshot containing user data.
     * @return A specific subclass of BaseUser, or null if the role is unrecognized.
     */
    private BaseUser resolveUserByRole(String role, DocumentSnapshot snapshot) {
        if (role == null) {
            LOGGER.warning("Role is null for document: " + snapshot.getId());
            return null;
        }

        LOGGER.info("Resolving user role: " + role + " for document: " + snapshot.getId());
        return switch (role) {
            case "Admin" -> snapshot.toObject(Administrator.class);
            case "Moderator" -> snapshot.toObject(Moderator.class);
            case "ContentCreator" -> snapshot.toObject(ContentCreator.class);
            case "RegularUser" -> snapshot.toObject(RegularUser.class);
            default -> {
                LOGGER.warning("Unknown role: " + role + " for document: " + snapshot.getId());
                yield null;
            }
        };
    }

    /**
     * Validates input parameters.
     */
    private void validateInput(String identifier, String password) {
        if (identifier == null || password == null) {
            LOGGER.warning("Identifier or password cannot be null");
            throw new IllegalArgumentException("Identifier or password cannot be null");
        }
    }

    /**
     * Validates user object.
     */
    private void validateUser(BaseUser user) {
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            LOGGER.warning("Password is required but was not provided for user: " + user.getUsername());
            throw new IllegalArgumentException("Password is required");
        }
    }

    /**
     * Sets the identifier for a user if it is not already set.
     */
    private void setUserIdentifier(BaseUser user) {
        if (user.getIdentifier() == null || user.getIdentifier().isEmpty()) {
            user.setIdentifier(user.getUsername() != null ? user.getUsername().toLowerCase() : user.getEmail().toLowerCase());
        }
    }

    /**
     * Writes a user to Firestore.
     */
    private void writeToFirestore(String userId, BaseUser user) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("users").document(userId);
        ApiFuture<WriteResult> result = docRef.set(user, SetOptions.merge());
        LOGGER.info("User write request sent for user: " + userId + ". Update time: " + result.get().getUpdateTime());
    }

    /**
     * Updates a specific field in Firestore.
     */
    private void updateField(String userId, String field, Object value) throws ExecutionException, InterruptedException {
        LOGGER.info("Updating field " + field + " for user with ID: " + userId);
        ApiFuture<WriteResult> result = firestore.collection("users").document(userId).update(field, value);
        LOGGER.info("Field " + field + " updated for user: " + userId + ". Update time: " + result.get().getUpdateTime());
    }

    /**
     * Gets users by a specific field value.
     */
    private List<BaseUser> getUsersByField(String field, Object value) throws ExecutionException, InterruptedException {
        LOGGER.info("Fetching users by field: " + field + " with value: " + value);
        List<BaseUser> users = new ArrayList<>();
        List<QueryDocumentSnapshot> documents = firestore.collection("users").whereEqualTo(field, value).get().get().getDocuments();

        for (QueryDocumentSnapshot document : documents) {
            BaseUser user = resolveUserByRole(document.getString("role"), document);
            if (user != null) {
                users.add(user);
            }
        }

        LOGGER.info("Total users found with " + field + " = " + value + ": " + users.size());
        return users;
    }
}
