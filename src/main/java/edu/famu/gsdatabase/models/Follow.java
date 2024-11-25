package edu.famu.gsdatabase.models;

import lombok.Getter;

@Getter
public class Follow {
    // Getters and Setters
    private int followId;
    private int followerUserId; // Foreign key
    private int followingUserId; // Foreign key
    private String role; // Role of the followed user
    private String skills;
    private String equipment;

    public void setFollowId(int followId) {
        this.followId = followId;
    }

    public void setFollowerUserId(int followerUserId) {
        this.followerUserId = followerUserId;
    }

    public void setFollowingUserId(int followingUserId) {
        this.followingUserId = followingUserId;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }
}

