package edu.famu.gsdatabase.models;

public class ContentCreator extends User {

    public ContentCreator() {
        super();
    }

    public ContentCreator(String userId, String username, String email, boolean isActive, String password) {
        super(userId, username, email, isActive, password);
    }

    @Override
    public void performRoleSpecificTask() {
        System.out.println("Content Creator is uploading and managing game-related content.");
    }
}