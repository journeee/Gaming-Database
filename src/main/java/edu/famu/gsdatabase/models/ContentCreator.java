package edu.famu.gsdatabase.models;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContentCreator extends BaseUser {
    public ContentCreator(String userId, String username, String email, boolean isActive, String password,String identifier) {
        super(userId, username, email, isActive, password, identifier);
    }

    @Override
    public void performRoleSpecificTask() {
        System.out.println("Content Creator is uploading and managing game-related content.");
    }
}