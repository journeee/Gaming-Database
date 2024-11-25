package edu.famu.gsdatabase.controllers;

import edu.famu.gsdatabase.dto.LoginRequest;
import edu.famu.gsdatabase.models.User;
import edu.famu.gsdatabase.service.UserService;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
     * @param loginRequest The login request containing identifier and password.
     * @return A response indicating success or failure.
     */
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody @Valid LoginRequest loginRequest) {
        try {
            User user = userService.authenticateByIdentifier(loginRequest.getIdentifier(), loginRequest.getPassword());
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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error during registration: " + e.getMessage()));
        }
    }

    /**
     * Helper class to structure API responses.
     */
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
