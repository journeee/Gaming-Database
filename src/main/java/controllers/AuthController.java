package controllers;



import models.User;
import org.springframework.web.bind.annotation.*;
import service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signin")
    public String signIn(@RequestParam String username, @RequestParam String password) {
        // Authentication logic
        return "Sign-in successful for user: " + username;
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        // Registration logic
        return "Registration successful for user: " + user.getUsername();
    }
}

