package edu.famu.gsdatabase.models;

public class RegularUser extends User {

    public RegularUser() {
        super();
    }

    public RegularUser(String userId, String username, String email, boolean isActive, String password) {
        super(userId, username, email, isActive, password);
    }

    @Override
    public void performRoleSpecificTask() {
        System.out.println("Regular User is browsing and bookmarking content.");
    }
}
