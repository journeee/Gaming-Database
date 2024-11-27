package edu.famu.gsdatabase.controllers;

import edu.famu.gsdatabase.models.BaseUser;
import edu.famu.gsdatabase.models.User;
import edu.famu.gsdatabase.service.UserService;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
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
            BaseUser user = userService.authenticateByIdentifier(identifier, password);
            if (user != null) {
                // Include the role and token in the response
                return ResponseEntity.ok(new ApiResponse(true, "Sign-in successful", Map.of(
                        "role", user.getClass().getSimpleName(),
                        "username", user.getUsername(),
                        "token", "mocked-jwt-token" // Replace with actual token generation logic
                )));
            } else {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid identifier or password"));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
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
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        try {
            // Ensure all fields are populated
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Password is required."));
            }

            userService.createUser(user);
            return ResponseEntity.ok(new ApiResponse(true, "Registration successful for user: " + user.getUsername()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error during registration: " + e.getMessage()));
        }
    }

    /**
     * Endpoint to promote a user to admin (Admin functionality).
     *
     * @param userId The ID of the user to promote.
     * @return A response indicating success or failure.
     */
    @PatchMapping("/promote/{userId}")
    public ResponseEntity<?> promoteUser(@PathVariable String userId) {
        try {
            userService.promoteToAdmin(userId);
            return ResponseEntity.ok(new ApiResponse(true, "User promoted to Admin successfully"));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error promoting user to Admin: " + e.getMessage()));
        }
    }

    /**
     * Helper class to structure API responses.
     */
    @Getter
    public static class ApiResponse {
        private final boolean success;
        private final String message;
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
