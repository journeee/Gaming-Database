package controllers;


import org.springframework.web.bind.annotation.*;
import service.UserService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String listUsers() {
        return "Listing all users.";
    }

    @PutMapping("/user/{id}/activate")
    public String activateUser(@PathVariable String id, @RequestParam boolean activate) throws Exception {
        userService.activateDeactivateUser(id, activate);
        return "User activation status updated.";
    }
}
