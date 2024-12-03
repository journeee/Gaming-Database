package edu.famu.gsdatabase.controllers;

import edu.famu.gsdatabase.models.BaseUser;
import edu.famu.gsdatabase.service.UserService;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
     * @param identifier The username or email of the user.
     * @param password   The password of the user.
     * @return A response indicating success or failure.
     */
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestParam String identifier, @RequestParam String password) {
        try {
            LOGGER.info("Sign-in attempt with identifier: " + identifier);

            if (identifier == null || identifier.isEmpty()) {
                LOGGER.warning("Identifier is null or empty.");
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Identifier must not be null or empty."));
            }

            if (password == null || password.isEmpty()) {
                LOGGER.warning("Password is null or empty.");
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Password must not be null or empty."));
            }

            // Normalize identifier to ensure case-insensitive matching
            identifier = identifier.toLowerCase();

            BaseUser user = userService.authenticateByIdentifier(identifier, password);
            if (user == null) {
                LOGGER.warning("User not found with identifier: " + identifier);
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid identifier or password"));
            }

            LOGGER.info("Sign-in successful for user: " + user.getUsername() + " with role: " + user.getRole());

            // Include the role, token, and dashboard URL in the response
            String dashboardUrl = getDashboardUrl(user);
            return ResponseEntity.ok(new ApiResponse(true, "Sign-in successful", Map.of(
                    "role", user.getClass().getSimpleName(),
                    "username", user.getUsername(),
                    "token", generateJwtToken(user),
                    "dashboardUrl", dashboardUrl
            )));
        } catch (IllegalArgumentException e) {
            LOGGER.severe("Illegal argument exception during sign-in: " + e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            LOGGER.severe("Unexpected error during sign-in: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error during sign-in: " + e.getMessage()));
        }
    }

    /**
     * Endpoint for registering a new user.
     *
     * @param user The user object containing the registration details.
     * @return A response indicating success or failure.
     */
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

            // Set userId if not provided (e.g., using UUID)
            if (user.getUserId() == null || user.getUserId().isEmpty()) {
                user.setUserId(java.util.UUID.randomUUID().toString());
                LOGGER.info("Generated userId for user: " + user.getUsername() + " - userId: " + user.getUserId());
            }

            // Normalize identifier to lowercase for consistency
            user.setIdentifier(user.getUsername() != null ? user.getUsername().toLowerCase() : user.getEmail().toLowerCase());

            // Hash the password before storing it
            user.setPassword(passwordEncoder.encode(user.getPassword()));
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
