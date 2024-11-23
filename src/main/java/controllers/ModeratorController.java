package controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.UserService;

@RestController
@RequestMapping("/api/moderator")
public class ModeratorController {
    private final UserService userService;

    public ModeratorController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/flag")
    public String flagContent(@RequestParam String contentId) {
        return "Content flagged: " + contentId;
    }
}

