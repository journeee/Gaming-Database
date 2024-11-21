package models;

public class ContentCreator extends RegularUser {

    public ContentCreator(String userId, String email, String username, String password) {
        super(userId, email, username, password);
    }

    public void uploadContent(String content) {
        System.out.println("Uploading content: " + content);
    }

    public void moderateOwnContent(String contentId) {
        System.out.println("Moderating content: " + contentId);
    }

    public void createExclusiveContent(String premiumContent) {
        System.out.println("Creating exclusive content: " + premiumContent);
    }
}
