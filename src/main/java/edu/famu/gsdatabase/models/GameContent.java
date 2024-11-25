package edu.famu.gsdatabase.models;

import lombok.Getter;

public class GameContent {
    // Getters and Setters
    @Getter
    private String contentId;
    @Getter
    private String userId;
    @Getter
    private String title;
    @Getter
    private String description;
    private boolean isPremium;

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }
}
