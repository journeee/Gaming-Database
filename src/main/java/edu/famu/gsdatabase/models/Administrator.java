package edu.famu.gsdatabase.models;

public class Administrator extends User {

    public Administrator() {
        super();
    }

    public Administrator(String userId, String username, String email, boolean isActive, String password) {
        super(userId, username, email, isActive, password);
    }

    @Override
    public void performRoleSpecificTask() {
        System.out.println("Admin is managing users and system settings.");
    }
}
