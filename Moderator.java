package models;

public class Moderator extends User {

    public Moderator(String userId, String email, String username, String password) {
        super(userId, email, username, password, "Moderator");
    }

    public void reviewContent(String contentId) {
        System.out.println("Reviewing content: " + contentId);
    }

    public void flagOrRemoveContent(String contentId) {
        System.out.println("Flagging or removing content: " + contentId);
    }

    public void monitorCommunity(String discussionId) {
        System.out.println("Monitoring community discussion: " + discussionId);
    }

    public void banOrSuspendUser(String userId) {
        System.out.println("Banning or suspending user: " + userId);
    }
}
