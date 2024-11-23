package models;

import lombok.Getter;

@Getter
public class Comments {
    // Getters and Setters
    private int commentId;
    private int userId; // Foreign key
    private int contentId; // Foreign key
    private String commentText;
    private String createdAt;

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}

