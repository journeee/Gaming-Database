package edu.famu.gsdatabase.controllers;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import edu.famu.gsdatabase.models.BaseUser;
import edu.famu.gsdatabase.service.UserService;
import edu.famu.gsdatabase.util.ApiResponseFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.google.cloud.firestore.Firestore;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    private Firestore firestore = FirestoreClient.getFirestore();

    private static final Logger LOGGER = Logger.getLogger(AuthController.class.getName());

    /**
     * Register a new user.
     *
     * @param user The user to register.
     * @return The response with user registration status.
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponseFormat<String>> register(@RequestBody BaseUser user) {
        LOGGER.info("Registration attempt for user: " + user.getEmail());

        try {
            // Use UserService to create user
            String userId = userService.createUser(user);
            LOGGER.info("User created successfully with ID: " + userId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseFormat<>(true, "User created successfully", userId, null));
        } catch (FirebaseAuthException e) {
            LOGGER.severe("FirebaseAuthException during registration: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseFormat<>(false, "Firebase registration failed", null, e.getMessage()));
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.severe("Exception during Firestore operation: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseFormat<>(false, "Error saving user to Firestore", null, e.getMessage()));
        }
    }
    /**
     * User sign-in with email and password.
     *
     * @param credentials A map containing the user's email and password.
     * @return The response with the user's details or an error message.
     */
    @PostMapping("/signin")
    public ResponseEntity<ApiResponseFormat<String>> signIn(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        LOGGER.info("Sign-in attempt with email: " + email);

        try {
            // Authenticate user with Firebase
            UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(email);
            if (userRecord == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponseFormat<>(false, "User not found", null, null));
            }

            // Assuming Firebase Auth validates the password and the user record exists
            BaseUser user = userService.getUserById(userRecord.getUid());
            String dashboardUrl = getDashboardUrl(user);

            return ResponseEntity.ok(new ApiResponseFormat<>(true, "Sign-in successful", dashboardUrl, null));
        } catch (FirebaseAuthException e) {
            LOGGER.severe("FirebaseAuthException during sign-in: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponseFormat<>(false, "Authentication failed", null, e.getMessage()));
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.severe("Exception during user retrieval: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseFormat<>(false, "Error retrieving user", null, e.getMessage()));
        }
    }
    /**
     * Get the appropriate dashboard URL for a user based on their role.
     *
     * @param user The user object.
     * @return The URL of the user's dashboard.
     */
    private String getDashboardUrl(BaseUser user) {
        if (user == null) {
            return "/login";
        }
        switch (user.getRole()) {
            case "Admin":
                return "/admin-dashboard";
            case "Moderator":
                return "/moderator-dashboard";
            case "ContentCreator":
                return "/content-creator-dashboard";
            default:
                return "/user-dashboard";
        }
    }


    /**
     * Update user details.
     *
     * @param userId      The ID of the user to update.
     * @param updatedUser The updated user information.
     * @return The response indicating the update status.
     */
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponseFormat<String>> updateUser(@PathVariable String userId, @RequestBody BaseUser updatedUser) {
        try {
            userService.updateUser(userId, updatedUser);
            return ResponseEntity.ok(new ApiResponseFormat<>(true, "User updated successfully", userId, null));
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.severe("Exception during user update: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseFormat<>(false, "Error updating user", null, e.getMessage()));
        } catch (FirebaseAuthException e) {
            LOGGER.severe("FirebaseAuthException during user update: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseFormat<>(false, "Firebase update failed", null, e.getMessage()));
        }
    }

    /**
     * Deactivate or activate a user account.
     *
     * @param userId The ID of the user.
     * @param action The action to take: "deactivate" or "activate".
     * @return The response indicating the action result.
     */
    @PutMapping("/{userId}/status")
    public ResponseEntity<ApiResponseFormat<String>> updateUserStatus(
            @PathVariable String userId,
            @RequestParam String action) {
        try {
            // Convert action parameter to a boolean value
            boolean isActive = "activate".equalsIgnoreCase(action);
            boolean result = userService.activateDeactivateUser(userId, isActive);

            if (result) {
                return ResponseEntity.ok(new ApiResponseFormat<>(true, "User status updated successfully", null, null));
            } else {
                return ResponseEntity.badRequest().body(new ApiResponseFormat<>(false, "Failed to update user status", null, null));
            }
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.severe("Exception during status update: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseFormat<>(false, "Error updating user status", e.getMessage(), null));
        }
    }
}
