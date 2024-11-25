package edu.famu.gsdatabase.models;

public class Moderator extends User {

    public Moderator() {
        super();
    }

    public Moderator(String userId, String username, String email, boolean isActive, String password) {
        super(userId, username, email, isActive, password);
    }

    @Override
    public void performRoleSpecificTask() {
        System.out.println("Moderator is reviewing and flagging inappropriate content.");
    }
}
