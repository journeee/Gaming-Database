package edu.famu.gsdatabase.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import edu.famu.gsdatabase.models.*;
import jakarta.validation.Valid;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {

    private final Firestore firestore;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService() {
        this.firestore = FirestoreClient.getFirestore();
        this.passwordEncoder = new BCryptPasswordEncoder(); // Initialize BCryptPasswordEncoder
    }

    /**
     * Authenticate a user by identifier (username or email) and password.
     *
     * @param identifier The username or email of the user.
     * @param password   The password of the user.
     * @return The authenticated BaseUser object, or null if authentication fails.
     */
    public BaseUser authenticateByIdentifier(String identifier, String password) throws ExecutionException, InterruptedException {
        if (identifier == null || password == null) {
            throw new IllegalArgumentException("Identifier or password cannot be null");
        }

        CollectionReference usersCollection = firestore.collection("users");

        // Query Firestore for the user with the provided identifier
        ApiFuture<QuerySnapshot> query = usersCollection.whereEqualTo("username", identifier).get();
        List<QueryDocumentSnapshot> documents = query.get().getDocuments();

        if (documents.isEmpty()) {
            // Try querying for email if no username match
            query = usersCollection.whereEqualTo("email", identifier).get();
            documents = query.get().getDocuments();

            if (documents.isEmpty()) {
                return null; // User not found
            }
        }

        DocumentSnapshot document = documents.get(0);
        BaseUser user = resolveUserByRole(document.getString("role"), document);

        // Verify the password
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user; // Authentication successful
        }

        return null; // Authentication failed
    }

    /**
     * Create a new user.
     *
     * @param user The user object to create.
     * @return The ID of the newly created user.
     */
    public String createUser(@Valid BaseUser user) throws ExecutionException, InterruptedException {
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash the password
        DocumentReference docRef = firestore.collection("users").document(user.getUsername());
        docRef.set(user).get();
        return user.getUsername();
    }

    /**
     * Update an existing user's information.
     *
     * @param userId The ID of the user to update.
     * @param user   The updated user object.
     */
    public void updateUser(String userId, BaseUser user) throws ExecutionException, InterruptedException {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            // Hash the new password before updating
            String hashedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
        }

        firestore.collection("users").document(userId).set(user, SetOptions.merge()).get();
    }

    /**
     * Activate or deactivate a user.
     *
     * @param userId  The ID of the user to update.
     * @param isActive True to activate, false to deactivate.
     */
    public void activateDeactivateUser(String userId, boolean isActive) throws ExecutionException, InterruptedException {
        firestore.collection("users").document(userId).update("isActive", isActive).get();
    }

    /**
     * Get all inactive users.
     *
     * @return List of all inactive users.
     */
    public List<BaseUser> getInactiveUsers() throws ExecutionException, InterruptedException {
        List<BaseUser> inactiveUsers = new ArrayList<>();
        List<QueryDocumentSnapshot> documents = firestore.collection("users").whereEqualTo("isActive", false).get().get().getDocuments();

        for (QueryDocumentSnapshot document : documents) {
            String role = document.getString("role");
            BaseUser user = resolveUserByRole(role, document);
            if (user != null) {
                inactiveUsers.add(user);
            }
        }

        return inactiveUsers;
    }

    /**
     * Flag a user for review.
     *
     * @param userId The ID of the user to flag.
     * @return A message indicating the user has been flagged.
     */
    public String flagUser(String userId) throws ExecutionException, InterruptedException {
        firestore.collection("users").document(userId).update("flagged", true).get();
        return "User flagged successfully";
    }

    /**
     * Promote a user to Admin.
     *
     * @param userId The ID of the user to promote.
     */
    public void promoteToAdmin(String userId) throws ExecutionException, InterruptedException {
        firestore.collection("users").document(userId).update("role", "Admin").get();
    }

    /**
     * Get all users from Firestore, resolving them to their specific subclasses.
     *
     * @return List of all users.
     */
    public List<BaseUser> getAllUsers() throws ExecutionException, InterruptedException {
        List<BaseUser> users = new ArrayList<>();
        List<QueryDocumentSnapshot> documents = firestore.collection("users").get().get().getDocuments();

        for (QueryDocumentSnapshot document : documents) {
            String role = document.getString("role");
            BaseUser user = resolveUserByRole(role, document);
            if (user != null) {
                users.add(user);
            }
        }

        return users;
    }

    /**
     * Get a user by their ID, resolving them to their specific subclass.
     *
     * @param userId The user ID.
     * @return The BaseUser object or null if not found.
     */
    public BaseUser getById(String userId) throws ExecutionException, InterruptedException {
        DocumentSnapshot snapshot = firestore.collection("users").document(userId).get().get();
        if (!snapshot.exists()) return null;

        String role = snapshot.getString("role");
        return resolveUserByRole(role, snapshot);
    }

    /**
     * Delete a user by their ID.
     *
     * @param userId The ID of the user to delete.
     */
    public void deleteById(String userId) throws ExecutionException, InterruptedException {
        firestore.collection("users").document(userId).delete().get();
    }

    /**
     * Resolves a user to its specific subclass based on the role.
     *
     * @param role     The role of the user.
     * @param snapshot The Firestore snapshot containing user data.
     * @return A specific subclass of BaseUser, or null if the role is unrecognized.
     */
    private BaseUser resolveUserByRole(String role, DocumentSnapshot snapshot) {
        return switch (role) {
            case "Admin" -> snapshot.toObject(Administrator.class);
            case "Moderator" -> snapshot.toObject(Moderator.class);
            case "ContentCreator" -> snapshot.toObject(ContentCreator.class);
            case "RegularUser" -> snapshot.toObject(RegularUser.class);
            default -> null; // Handle unknown role gracefully
        };
    }
}
