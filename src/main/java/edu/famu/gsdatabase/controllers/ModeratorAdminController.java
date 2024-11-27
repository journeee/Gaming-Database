package edu.famu.gsdatabase.controllers;

import edu.famu.gsdatabase.models.BaseUser;
import edu.famu.gsdatabase.service.GameContentService;
import edu.famu.gsdatabase.service.UserService;
import edu.famu.gsdatabase.util.ApiResponseFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/moderator-admin")
public class ModeratorAdminController {

    private final GameContentService gameContentService;
    private final UserService userService;

    public ModeratorAdminController(GameContentService gameContentService, UserService userService) {
        this.gameContentService = gameContentService;
        this.userService = userService;
    }

    // Moderator Functionalities

    /**
     * Review game content (Moderator functionality).
     *
     * @param contentId The ID of the content to review.
     * @return A response indicating the success or failure of the operation.
     */
    @PostMapping("/review/{contentId}")
    public ResponseEntity<ApiResponseFormat<String>> reviewContent(@PathVariable String contentId) {
        try {
            String result = gameContentService.reviewContent(contentId);
            return ResponseEntity.ok(new ApiResponseFormat<>(true, "Content reviewed successfully", result, null));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseFormat<>(false, "Error reviewing content", null, e.getMessage()));
        }
    }

    /**
     * Flag game content for inappropriate behavior (Moderator functionality).
     *
     * @param contentId The ID of the content to flag.
     * @return A response indicating the success or failure of the operation.
     */
    @PostMapping("/flag/{contentId}")
    public ResponseEntity<ApiResponseFormat<String>> flagContent(@PathVariable String contentId) {
        try {
            String result = gameContentService.flagContent(contentId);
            return ResponseEntity.ok(new ApiResponseFormat<>(true, "Content flagged successfully", result, null));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseFormat<>(false, "Error flagging content", null, e.getMessage()));
        }
    }

    /**
     * Remove flagged game content (Moderator functionality).
     *
     * @param contentId The ID of the content to remove.
     * @return A response indicating the success or failure of the operation.
     */
    @DeleteMapping("/remove/{contentId}")
    public ResponseEntity<ApiResponseFormat<String>> removeContent(@PathVariable String contentId) {
        try {
            String result = gameContentService.removeContent(contentId).toString();
            return ResponseEntity.ok(new ApiResponseFormat<>(true, "Content removed successfully", result, null));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseFormat<>(false, "Error removing content", null, e.getMessage()));
        }
    }

    /**
     * Flag a user for review (Moderator functionality).
     *
     * @param userId The ID of the user to flag.
     * @return A response indicating the success or failure of the operation.
     */
    @PostMapping("/flag/user/{userId}")
    public ResponseEntity<ApiResponseFormat<String>> flagUser(@PathVariable String userId) {
        try {
            String result = userService.flagUser(userId);
            return ResponseEntity.ok(new ApiResponseFormat<>(true, "User flagged successfully", result, null));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseFormat<>(false, "Error flagging user", null, e.getMessage()));
        }
    }

    // Admin Functionalities

    /**
     * Get all users (Admin functionality).
     *
     * @return List of all users or an appropriate error message.
     */
    @GetMapping("/users")
    public ResponseEntity<ApiResponseFormat<List<BaseUser>>> getAllUsers() {
        try {
            List<BaseUser> users = userService.getAllUsers();
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

    /**
     * Activate or deactivate a user (Admin functionality).
     *
     * @param userId   The ID of the user to update.
     * @param isActive True to activate, false to deactivate.
     * @return A success or error message.
     */
    @PatchMapping("/user/{userId}/status")
    public ResponseEntity<ApiResponseFormat<String>> updateUserStatus(@PathVariable String userId, @RequestParam boolean isActive) {
        try {
            userService.activateDeactivateUser(userId, isActive);
            return ResponseEntity.ok(new ApiResponseFormat<>(true, "User status updated successfully", null, null));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseFormat<>(false, "Error updating user status", null, e.getMessage()));
        }
    }

    /**
     * Delete a user (Admin functionality).
     *
     * @param userId The ID of the user to delete.
     * @return A success or error message.
     */
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<ApiResponseFormat<String>> deleteUser(@PathVariable String userId) {
        try {
            userService.deleteById(userId);
            return ResponseEntity.ok(new ApiResponseFormat<>(true, "User deleted successfully", null, null));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseFormat<>(false, "Error deleting user", null, e.getMessage()));
        }
    }

    /**
     * List all inactive users (Admin functionality).
     *
     * @return List of inactive users or an appropriate error message.
     */
    @GetMapping("/users/inactive")
    public ResponseEntity<ApiResponseFormat<List<BaseUser>>> getInactiveUsers() {
        try {
            List<BaseUser> inactiveUsers = userService.getInactiveUsers();
            if (inactiveUsers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ApiResponseFormat<>(true, "No inactive users found", null, null));
            }
            return ResponseEntity.ok(new ApiResponseFormat<>(true, "Inactive users retrieved successfully", inactiveUsers, null));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseFormat<>(false, "Error retrieving inactive users", null, e.getMessage()));
        }
    }

    /**
     * Promote a user to Admin (Admin functionality).
     *
     * @param userId The ID of the user to promote.
     * @return A success or error message.
     */
    @PatchMapping("/user/{userId}/promote")
    public ResponseEntity<ApiResponseFormat<String>> promoteUserToAdmin(@PathVariable String userId) {
        try {
            userService.promoteToAdmin(userId);
            return ResponseEntity.ok(new ApiResponseFormat<>(true, "User promoted to Admin successfully", null, null));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseFormat<>(false, "Error promoting user to Admin", null, e.getMessage()));
        }
    }
}
