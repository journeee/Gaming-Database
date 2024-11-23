package models;

public class Administrator extends User {

        public Administrator(String userId, String email, String username, String password) {
            super(userId, email, username, password, "Admin");
        }

        @Override
        public void performRoleSpecificTask() {
            System.out.println("Admin: Managing users and system-wide settings.");
        }
    }
