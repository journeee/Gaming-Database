package models;

public class RegularUser extends User {

    public RegularUser(String userId, String email, String username, String password) {
        super(userId, email, username, password, "RegularUser");
    }

    public void searchGameInformation(String query) {
        System.out.println("Searching for: " + query);
    }

    public void bookmarkContent(String contentId) {
        System.out.println("Bookmarking content: " + contentId);
    }

    public void interactWithCommunity(String message) {
        System.out.println("Interacting with the community: " + message);
    }
}
