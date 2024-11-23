package controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.ContentModeratorService;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("api/moderator")
public class ContentModeratorController {
    private final ContentModeratorService moderatorService;

    public ContentModeratorController(ContentModeratorService moderatorService) {
        this.moderatorService = moderatorService;
    }

    @PostMapping("/review/{contentId}")
    public ResponseEntity<String> reviewContent(@PathVariable String contentId) {
        try {
            String result = moderatorService.reviewContent(contentId);
            return ResponseEntity.ok(result);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(500).body("Error reviewing content.");
        }
    }

    @PostMapping("/flag/{contentId}")
    public ResponseEntity<String> flagContent(@PathVariable String contentId) {
        try {
            String result = moderatorService.flagContent(contentId);
            return ResponseEntity.ok(result);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(500).body("Error flagging content.");
        }
    }

    @DeleteMapping("/remove/{contentId}")
    public ResponseEntity<String> removeContent(@PathVariable String contentId) {
        try {
            String result = moderatorService.removeContent(contentId);
            return ResponseEntity.ok(result);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(500).body("Error removing content.");
        }
    }
}
