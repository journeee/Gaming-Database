package models;

public class Moderator extends User {

    public Moderator(String userId, String email, String username, String password) {
        super(userId, email, username, password, "Moderator");
    }

    @Override
    public void performRoleSpecificTask() {
        System.out.println("Moderator: Reviewing and flagging inappropriate content.");
    }
}
