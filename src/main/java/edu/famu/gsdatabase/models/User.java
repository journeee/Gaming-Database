package edu.famu.gsdatabase.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "role",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Administrator.class, name = "Admin"),
        @JsonSubTypes.Type(value = Moderator.class, name = "Moderator"),
        @JsonSubTypes.Type(value = ContentCreator.class, name = "ContentCreator"),
        @JsonSubTypes.Type(value = RegularUser.class, name = "RegularUser")
})
@EqualsAndHashCode(callSuper = false) // Explicitly state no superclass call
public abstract class User {
    private String userId;
    private String username;
    private String email;
    private boolean isActive;

    @JsonIgnore
    private String password;

    public User() {}

    public User(String userId, String username, String email, boolean isActive, String password) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.isActive = isActive;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public abstract void performRoleSpecificTask();
}
