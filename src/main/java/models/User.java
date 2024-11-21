package models;

public abstract class User {
    private String userId;
    private String email;
    private String username;
    private String password;
    private String role;

    // Profile Management Fields
    private String bio;
    private String profilePictureUrl;

    // Notification Preferences
    private boolean notifyOnContentUpdates;
    private boolean notifyOnComments;
    private boolean notifyOnDiscussions;

    // Constructor
    public User(String userId, String email, String username, String password, String role) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public boolean isNotifyOnContentUpdates() {
        return notifyOnContentUpdates;
    }

    public void setNotifyOnContentUpdates(boolean notifyOnContentUpdates) {
        this.notifyOnContentUpdates = notifyOnContentUpdates;
    }

    public boolean isNotifyOnComments() {
        return notifyOnComments;
    }

    public void setNotifyOnComments(boolean notifyOnComments) {
        this.notifyOnComments = notifyOnComments;
    }

    public boolean isNotifyOnDiscussions() {
        return notifyOnDiscussions;
    }

    public void setNotifyOnDiscussions(boolean notifyOnDiscussions) {
        this.notifyOnDiscussions = notifyOnDiscussions;
    }
}
