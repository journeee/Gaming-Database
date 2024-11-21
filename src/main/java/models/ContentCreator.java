package models;

public class ContentCreator extends User {
    private int totalPosts;       // Number of posts the creator has made
    private int totalFollowers;  // Number of followers the creator has
    private String[] tags;       // Tags related to the creator's content focus

    // Constructor
    public ContentCreator(
            String userId,
            String email,
            String username,
            String password,
            String role,
            int totalPosts,
            int totalFollowers,
            String[] tags
    ) {
        super(userId, email, username, password, role);
        this.totalPosts = totalPosts;
        this.totalFollowers = totalFollowers;
        this.tags = tags;
    }

    // Getters and Setters
    public int getTotalPosts() {
        return totalPosts;
    }

    public void setTotalPosts(int totalPosts) {
        this.totalPosts = totalPosts;
    }

    public int getTotalFollowers() {
        return totalFollowers;
    }

    public void setTotalFollowers(int totalFollowers) {
        this.totalFollowers = totalFollowers;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    // Additional Methods
    public void addPost() {
        this.totalPosts++;
    }

    public void addFollower() {
        this.totalFollowers++;
    }

    public void removeFollower() {
        if (this.totalFollowers > 0) {
            this.totalFollowers--;
        }
    }

    public void displayTags() {
        System.out.println("Tags: ");
        if (tags != null) {
            for (String tag : tags) {
                System.out.println("- " + tag);
            }
        } else {
            System.out.println("No tags available.");
        }
    }
}
