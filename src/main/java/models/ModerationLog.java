package models;


import lombok.Getter;

@Getter
public class ModerationLog {
    // Getters and Setters
    private int logId;
    private int moderatorId; // Foreign key
    private int contentId; // Foreign key
    private String action; // e.g., Delete, Edit, Approve
    private String timestamp;

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public void setModeratorId(int moderatorId) {
        this.moderatorId = moderatorId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

