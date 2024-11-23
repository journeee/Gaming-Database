package models;


public class ContentCreator extends User {

    public ContentCreator(String userId, String email, String username, String password) {
        super(userId, email, username, password, "ContentCreator");
    }

    @Override
    public void performRoleSpecificTask() {
        System.out.println("Content Creator: Uploading and moderating game-related content.");
    }
}
