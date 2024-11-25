package edu.famu.gsdatabase.controllers;

import edu.famu.gsdatabase.models.GameContent;
import edu.famu.gsdatabase.service.GameContentService;
import edu.famu.gsdatabase.util.ApiResponseFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/content")
public class GameContentController {

    private final GameContentService gameContentService;

    public GameContentController(GameContentService gameContentService) {
        this.gameContentService = gameContentService;
    }

    // Helper method to check if the user is a Content Creator
    private boolean isContentCreator(String role) {
        return "ContentCreator".equalsIgnoreCase(role);
    }

    /**
     * Post new game content (Content Creators only).
     *
     * @param role    The role of the user making the request.
     * @param content The GameContent object to post.
     * @return A response indicating the success or failure of the operation.
     */
    @PostMapping("/")
    public ResponseEntity<ApiResponseFormat<String>> postContent(
            @RequestHeader("Role") String role,
            @RequestBody GameContent content
    ) {
        if (!isContentCreator(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponseFormat<>(false, "Access denied: Only Content Creators can post content", null, null));
        }

        try {
            String contentId = gameContentService.postContent(content);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseFormat<>(true, "Content posted successfully", contentId, null));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseFormat<>(false, "Error posting content", null, e.getMessage()));
        }
    }

    /**
     * Retrieve all game content (Accessible to all roles).
     *
     * @return A response containing all game content.
     */
    @GetMapping("/")
    public ResponseEntity<ApiResponseFormat<List<GameContent>>> getAllContent() {
        try {
            List<GameContent> content = gameContentService.getAllContent();
            if (content.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ApiResponseFormat<>(true, "No content available", null, null));
            }
            return ResponseEntity.ok(new ApiResponseFormat<>(true, "Content retrieved successfully", content, null));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseFormat<>(false, "Error retrieving content", null, e.getMessage()));
        }
    }

    /**
     * Bookmark game content (Accessible to all roles).
     *
     * @param userId    The ID of the user bookmarking the content.
     * @param contentId The ID of the content to bookmark.
     * @return A response indicating the success or failure of the operation.
     */
    @PostMapping("/{contentId}/bookmark")
    public ResponseEntity<ApiResponseFormat<String>> bookmarkContent(
            @RequestHeader("UserId") String userId,
            @PathVariable String contentId
    ) {
        try {
            String bookmarkId = gameContentService.bookmarkContent(userId, contentId);
            return ResponseEntity.ok(new ApiResponseFormat<>(true, "Content bookmarked successfully", bookmarkId, null));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseFormat<>(false, "Error bookmarking content", null, e.getMessage()));
        }
    }
}
