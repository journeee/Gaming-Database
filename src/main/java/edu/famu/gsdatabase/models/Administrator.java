package edu.famu.gsdatabase.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Administrator extends BaseUser {
    public Administrator(String userId, String username, String email, boolean isActive, String password, String identifier) {
        super(userId, username, email, isActive, password, identifier);
    }

    @Override
    public void performRoleSpecificTask() {
        System.out.println("Admin is managing users and system settings.");
    }
}