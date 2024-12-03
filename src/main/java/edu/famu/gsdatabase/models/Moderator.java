package edu.famu.gsdatabase.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Moderator extends BaseUser {
    public Moderator(String userId, String username, String email, boolean isActive, String identifier, String password) {
        super(userId, username, email, isActive, password, identifier);
    }

    @Override
    public void performRoleSpecificTask() {
        System.out.println("Moderator is reviewing and flagging inappropriate content.");
    }
}