package edu.famu.gsdatabase.models;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegularUser extends BaseUser {
    public RegularUser(String userId, String username, String email, boolean isActive, String password) {
        super(userId, username, email, isActive, password);
    }

    @Override
    public void performRoleSpecificTask() {
        System.out.println("Regular User is browsing and bookmarking content.");
    }
}