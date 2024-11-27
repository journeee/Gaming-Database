package edu.famu.gsdatabase.controllers;

import edu.famu.gsdatabase.service.GameContentService;
import edu.famu.gsdatabase.service.UserService;
import edu.famu.gsdatabase.util.ApiResponseFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/moderator")
public class ContentModeratorController {

    private final GameContentService gameContentService;
    private final UserService userService;

    public ContentModeratorController(GameContentService gameContentService, UserService userService) {
        this.gameContentService = gameContentService;
        this.userService = userService;
    }

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
}
