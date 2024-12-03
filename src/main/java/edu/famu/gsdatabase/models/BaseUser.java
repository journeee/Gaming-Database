package edu.famu.gsdatabase.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "role",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = User.class, name = "User"),
        @JsonSubTypes.Type(value = Administrator.class, name = "Admin"),
        @JsonSubTypes.Type(value = Moderator.class, name = "Moderator"),
        @JsonSubTypes.Type(value = ContentCreator.class, name = "ContentCreator"),
        @JsonSubTypes.Type(value = RegularUser.class, name = "RegularUser")
})
@EqualsAndHashCode(callSuper = false)
@Data
public abstract class BaseUser {
    @Setter protected String userId;
    @Setter protected String username;
    @Setter protected String email;
    @Setter protected boolean isActive;
    @Setter protected String password;
    @Getter
    @Setter protected String role;
    @Getter
    @Setter protected String identifier; // Added identifier field

    public BaseUser() {}

    public BaseUser(String userId, String username, String email, boolean isActive, String password, String identifier) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.isActive = isActive;
        this.password = password;
        this.identifier = identifier;
    }


    public abstract void performRoleSpecificTask();
}