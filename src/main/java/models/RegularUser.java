package models;

public class RegularUser extends User {

    public RegularUser(String userId, String email, String username, String password) {
        super(userId, email, username, password, "RegularUser");
    }

    public void interactWithCommunity(String message) {
        System.out.println("Interacting with the community: " + message);
    }

    @Override
    public void performRoleSpecificTask() {
        System.out.println("Regular User: Searching and bookmarking game information.");
    }
}
