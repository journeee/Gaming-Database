package models;

import lombok.Getter;

@Getter
public abstract class User {
    private String userId;
    private String email;
    private String username;
    private String password;
    private String role;

    // Profile Fields
    private String bio;
    private String profilePictureUrl;

    // Notification Preferences
    private boolean notifyOnContentUpdates;
    private boolean notifyOnComments;
    private boolean notifyOnDiscussions;

    public User(String userId, String email, String username, String password, String role) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public abstract void performRoleSpecificTask();

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public void setNotifyOnContentUpdates(boolean notifyOnContentUpdates) {
        this.notifyOnContentUpdates = notifyOnContentUpdates;
    }

    public void setNotifyOnComments(boolean notifyOnComments) {
        this.notifyOnComments = notifyOnComments;
    }

    public void setNotifyOnDiscussions(boolean notifyOnDiscussions) {
        this.notifyOnDiscussions = notifyOnDiscussions;
    }
}
