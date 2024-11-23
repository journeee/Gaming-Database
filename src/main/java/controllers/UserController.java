package controllers;

import models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.UserService;
import util.ApiResponseFormat;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Helper method to check roles
    private boolean isAdmin(String role) {
        return "Admin".equalsIgnoreCase(role);
    }

    // Get all users (Admin functionality)
    @GetMapping("/")
    public ResponseEntity<ApiResponseFormat<List<User>>> getAllUsers(@RequestHeader("Role") String role) {
        if (!isAdmin(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponseFormat<>(false, "Access denied: Admins only", null, null));
        }

        try {
            List<User> users = userService.getAllUsers();
            if (users.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ApiResponseFormat<>(true, "No users found", null, null));
            }
            return ResponseEntity.ok(new ApiResponseFormat<>(true, "Users retrieved successfully", users, null));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseFormat<>(false, "Error retrieving users", null, e.getMessage()));
        }
    }

    // Get a specific user by ID (Accessible to all roles)
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponseFormat<User>> getUserById(@PathVariable String userId) {
        try {
            User user = userService.getById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseFormat<>(false, "User not found", null, null));
            }
            return ResponseEntity.ok(new ApiResponseFormat<>(true, "User retrieved successfully", user, null));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseFormat<>(false, "Error retrieving user", null, e.getMessage()));
        }
    }

    // Create a new user (Accessible to all roles)
    @PostMapping("/")
    public ResponseEntity<ApiResponseFormat<String>> createUser(@RequestBody User user) {
        try {
            String userId = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseFormat<>(true, "User created successfully", userId, null));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseFormat<>(false, "Error creating user", null, e.getMessage()));
        }
    }

    // Update user information (Admin functionality)
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponseFormat<String>> updateUser(@PathVariable String userId, @RequestBody User user,
                                                                @RequestHeader("Role") String role) {
        if (!isAdmin(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponseFormat<>(false, "Access denied: Admins only", null, null));
        }

        try {
            userService.updateUser(userId, user);
            return ResponseEntity.ok(new ApiResponseFormat<>(true, "User updated successfully", null, null));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseFormat<>(false, "Error updating user", null, e.getMessage()));
        }
    }

    // Activate or deactivate a user (Admin functionality)
    @PutMapping("/{userId}/status")
    public ResponseEntity<ApiResponseFormat<String>> updateUserStatus(@PathVariable String userId, @RequestBody String status,
                                                                      @RequestHeader("Role") String role) {
        if (!isAdmin(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponseFormat<>(false, "Access denied: Admins only", null, null));
        }

        try {
            boolean isActive = Boolean.parseBoolean(status);
            userService.activateDeactivateUser(userId, isActive);
            return ResponseEntity.ok(new ApiResponseFormat<>(true, "User status updated successfully", null, null));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseFormat<>(false, "Error updating user status", null, e.getMessage()));
        }
    }

    // Delete a user (Admin functionality)
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseFormat<String>> deleteUser(@PathVariable String userId, @RequestHeader("Role") String role) {
        if (!isAdmin(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponseFormat<>(false, "Access denied: Admins only", null, null));
        }

        try {
            userService.deleteById(userId);
            return ResponseEntity.ok(new ApiResponseFormat<>(true, "User deleted successfully", null, null));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseFormat<>(false, "Error deleting user", null, e.getMessage()));
        }
    }
}
