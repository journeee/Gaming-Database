package models;

import lombok.Getter;

@Getter
public class Bookmark {
    // Getters and Setters
    private int bookmarkId;
    private int userId; // Foreign key
    private int contentId; // Foreign key
    private String createdAt;

    public void setBookmarkId(int bookmarkId) {
        this.bookmarkId = bookmarkId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
