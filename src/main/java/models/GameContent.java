package models;

import lombok.Getter;

public class GameContent {
    @Getter
    private int contentId;
    @Getter
    private int userId; // Foreign key
    @Getter
    private int gameId; // Foreign key
    @Getter
    private String description;
    @Getter
    private String contentType; // e.g., Strategy, Build, Tutorial
    @Getter
    private String uploadDate;
    private boolean isPremium;

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }
}
