package edu.famu.gsdatabase.controllers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import edu.famu.gsdatabase.models.BaseUser;
import edu.famu.gsdatabase.service.UserService;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.http.*;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final SCryptPasswordEncoder passwordEncoder;
    private static final Logger LOGGER = Logger.getLogger(AuthController.class.getName());

    public AuthController(UserService userService) {
        this.userService = userService;
        this.passwordEncoder = new SCryptPasswordEncoder(16384, 8, 1, 32, 64); // Use SCrypt for hashing
    }

    /**
     * Endpoint for signing in a user.
     *
     * @return A response indicating success or failure.
     */

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestParam String email, @RequestParam String password) {
        try {
            LOGGER.info("Sign-in attempt with email: " + email);

            // Use Firebase Authentication REST API to authenticate
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=AIzaSyCMb_cDXh1FSMVHaw8nTOCkqVvyRK_MVHw";

            // Create the request body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("email", email);
            requestBody.put("password", password);
            requestBody.put("returnSecureToken", true);

            // Set headers for the request
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Make the request
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);

            // Extract the ID token
            String idToken = (String) response.getBody().get("idToken");

            // Verify ID token using Firebase Admin SDK
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String uid = decodedToken.getUid();

            // Fetch the user from Firestore or database using the UID
            BaseUser user = userService.getById(uid);
            if (user != null) {
                return ResponseEntity.ok(new ApiResponse(true, "User authenticated successfully", user));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse(false, "User not found", null));
            }

        } catch (Exception e) {
            LOGGER.warning("Authentication failed for email: " + email + ". Reason: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, "Invalid email/password or authentication failed", null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody BaseUser user) {
        try {
            LOGGER.info("Registration attempt for user: " + (user.getUsername() != null ? user.getUsername() : "unknown"));

            // Ensure all fields are populated
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                LOGGER.warning("Password is required but is null or empty for user: " + user.getUsername());
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Password is required."));
            }

            // Assign a default role if not provided
            if (user.getRole() == null || user.getRole().isEmpty()) {
                user.setRole("RegularUser");
                LOGGER.info("Role not provided. Defaulting to RegularUser for user: " + user.getUsername());
            }

            // Normalize identifier to lowercase for consistency
            String email = user.getEmail().toLowerCase();
            String password = user.getPassword();

            FirebaseAuth auth = FirebaseAuth.getInstance();

            // Create a user in Firebase Authentication
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password)
                    .setDisplayName(user.getUsername());

            UserRecord userRecord = auth.createUser(request);

            // Set Firebase userId as userId in your system
            user.setUserId(userRecord.getUid());
            user.setIdentifier(email);

            // Create the user in your Firestore collection
            userService.createUser(user);
            LOGGER.info("Registration successful for user: " + user.getUsername() + " with role: " + user.getRole());

            return ResponseEntity.ok(new ApiResponse(true, "Registration successful for user: " + user.getUsername(), Map.of(
                    "role", user.getRole(),
                    "dashboardUrl", getDashboardUrl(user)
            )));
        } catch (IllegalArgumentException e) {
            LOGGER.severe("Illegal argument exception during registration: " + e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            LOGGER.severe("Unexpected error during registration: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error during registration: " + e.getMessage()));
        }
    }

    /**
     * Helper method to generate a JWT token for a user.
     *
     * @param user The user for whom to generate the token.
     * @return A JWT token as a String.
     */
    private String generateJwtToken(BaseUser user) {
        LOGGER.info("Generating JWT token for user: " + user.getUsername());
        return "generated-jwt-token-for-" + user.getUsername();
    }

    /**
     * Helper method to get the dashboard URL based on user role.
     *
     * @param user The user for whom to get the dashboard URL.
     * @return A URL as a String.
     */
    private String getDashboardUrl(BaseUser user) {
        LOGGER.info("Getting dashboard URL for user: " + user.getUsername() + " with role: " + user.getRole());
        return switch (user.getRole()) {
            case "Admin" -> "/dashboard/admin";
            case "Moderator" -> "/dashboard/moderator";
            case "ContentCreator" -> "/dashboard/content-creator";
            default -> "/dashboard/user";
        };
    }

    @Getter
    public static class ApiResponse {
        private boolean success;
        private String message;
        private Object data;

        public ApiResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public ApiResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }
    }
}
