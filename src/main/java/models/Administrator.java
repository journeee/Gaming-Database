package models;

public class Administrator extends User {

    public Administrator(String userId, String email, String username, String password) {
        super(userId, email, username, password, "Administrator");
    }

    public void manageUserAccount(User user, String action) {
        // Action can be "activate", "deactivate", or "delete"
        System.out.println("Performing action: " + action + " on user: " + user.getUsername());
    }

    public void manageSystemSettings() {
        System.out.println("Managing system settings.");
    }

    public void viewActivityReports() {
        System.out.println("Viewing activity reports.");
    }

    public void viewSystemLogs() {
        System.out.println("Viewing system logs.");
    }
}
