package edu.famu.gsdatabase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class GsDatabaseApplication implements CommandLineRunner {

    public static void main(String[] args) {
        initializeFirebase(); // Initialize Firebase before Spring Boot runs
        SpringApplication.run(GsDatabaseApplication.class, args);
    }

    private static void initializeFirebase() {
        try {
            // Load service account key from resources
            InputStream serviceAccount = GsDatabaseApplication.class
                    .getClassLoader()
                    .getResourceAsStream("serviceAccountKey.json");

            if (serviceAccount == null) {
                throw new IllegalStateException("Service account key file not found in resources.");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://gaming-database-2a402.firebaseio.com")
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase initialized successfully.");
            }
        } catch (Exception e) {
            System.err.println("Error initializing Firebase: " + e.getMessage());
            throw new IllegalStateException("Firebase initialization failed.", e);
        }
    }

    @Override
    public void run(String... args) {
        // Call the FirestoreSeeder methods to insert sample data on startup
      /*  FirestoreSeeder.seedUsers();
        FirestoreSeeder.seedGameContent();
        FirestoreSeeder.seedBookmarks();
        FirestoreSeeder.seedGames();
        FirestoreSeeder.seedRoles();
        FirestoreSeeder.seedPosts();
        FirestoreSeeder.seedComments();
        FirestoreSeeder.seedModeratorActions();*/
    }

    public static class FirestoreSeeder {

        /**
         * Seeds user data into Firestore.
         */
        public static void seedUsers() {
            Firestore db = FirestoreClient.getFirestore();
            CollectionReference usersCollection = db.collection("users");

            Map<String, Object> regularUser = new HashMap<>();
            regularUser.put("userId", "user123");
            regularUser.put("email", "user123@example.com");
            regularUser.put("username", "user123");
            regularUser.put("password", "securepassword");
            regularUser.put("role", "RegularUser");
            regularUser.put("isActive", true);

            Map<String, Object> contentCreator = new HashMap<>();
            contentCreator.put("userId", "creator001");
            contentCreator.put("email", "creator@example.com");
            contentCreator.put("username", "content_creator");
            contentCreator.put("password", "creatorpassword");
            contentCreator.put("role", "ContentCreator");
            contentCreator.put("isActive", true);

            Map<String, Object> admin = new HashMap<>();
            admin.put("userId", "admin001");
            admin.put("email", "admin@example.com");
            admin.put("username", "admin");
            admin.put("password", "adminpassword");
            admin.put("role", "Admin");
            admin.put("isActive", true);

            Map<String, Object> moderator = new HashMap<>();
            moderator.put("userId", "moderator001");
            moderator.put("email", "moderator@example.com");
            moderator.put("username", "moderator");
            moderator.put("password", "moderatorpassword");
            moderator.put("role", "Moderator");
            moderator.put("isActive", true);

            // Add data to Firestore
            try {
                usersCollection.document("user123").set(regularUser).get();
                usersCollection.document("creator001").set(contentCreator).get();
                usersCollection.document("admin001").set(admin).get();
                usersCollection.document("moderator001").set(moderator).get();

                System.out.println("Sample users (including Moderator) added successfully.");
            } catch (Exception e) {
                System.err.println("Error adding users: " + e.getMessage());
            }
        }

        /**
         * Seeds game data into Firestore.
         */
        public static void seedGames() {
            Firestore db = FirestoreClient.getFirestore();
            CollectionReference gamesCollection = db.collection("Games");

            Map<String, Object> game1 = new HashMap<>();
            game1.put("gameId", "game001");
            game1.put("title", "Adventure Quest");
            game1.put("genre", "Adventure");
            game1.put("releaseDate", "2022-11-15");

            Map<String, Object> game2 = new HashMap<>();
            game2.put("gameId", "game002");
            game2.put("title", "Battle Stars");
            game2.put("genre", "Action");
            game2.put("releaseDate", "2021-05-10");

            try {
                gamesCollection.document("game001").set(game1).get();
                gamesCollection.document("game002").set(game2).get();
                System.out.println("Sample games added successfully.");
            } catch (Exception e) {
                System.err.println("Error adding games: " + e.getMessage());
            }
        }

        /**
         * Seeds game content data into Firestore.
         */
        public static void seedGameContent() {
            Firestore db = FirestoreClient.getFirestore();
            CollectionReference contentCollection = db.collection("GameContent");

            Map<String, Object> content1 = new HashMap<>();
            content1.put("contentId", "content123");
            content1.put("userId", "creator001"); // References Content Creator
            content1.put("gameId", "game001"); // References a Game
            content1.put("title", "Ultimate Strategy Guide");
            content1.put("description", "Detailed strategies for Adventure Quest.");
            content1.put("isPremium", false);

            try {
                contentCollection.document("content123").set(content1).get();
                System.out.println("Sample game content added successfully.");
            } catch (Exception e) {
                System.err.println("Error adding game content: " + e.getMessage());
            }
        }

        /**
         * Seeds post data into Firestore.
         */
        public static void seedPosts() {
            Firestore db = FirestoreClient.getFirestore();
            CollectionReference postsCollection = db.collection("Posts");

            Map<String, Object> post1 = new HashMap<>();
            post1.put("postId", "post001");
            post1.put("userId", "creator001"); // References Content Creator
            post1.put("gameId", "game001"); // References a Game
            post1.put("content", "Check out the latest updates for Adventure Quest!");

            try {
                postsCollection.document("post001").set(post1).get();
                System.out.println("Sample posts added successfully.");
            } catch (Exception e) {
                System.err.println("Error adding posts: " + e.getMessage());
            }
        }
        /**
         * Seeds roles data into Firestore.
         */
        public static void seedRoles() {
            Firestore db = FirestoreClient.getFirestore();
            CollectionReference rolesCollection = db.collection("Roles");

            Map<String, Object> regularUserRole = new HashMap<>();
            regularUserRole.put("role", "RegularUser");
            regularUserRole.put("permissions", "View content, Bookmark content");

            Map<String, Object> contentCreatorRole = new HashMap<>();
            contentCreatorRole.put("role", "ContentCreator");
            contentCreatorRole.put("permissions", "Upload content, View content");

            Map<String, Object> adminRole = new HashMap<>();
            adminRole.put("role", "Admin");
            adminRole.put("permissions", "Manage users, View reports");

            Map<String, Object> moderatorRole = new HashMap<>();
            moderatorRole.put("role", "Moderator");
            moderatorRole.put("permissions", "Review content, Flag/remove inappropriate content");

            // Add roles to Firestore
            try {
                rolesCollection.document("RegularUser").set(regularUserRole).get();
                rolesCollection.document("ContentCreator").set(contentCreatorRole).get();
                rolesCollection.document("Admin").set(adminRole).get();
                rolesCollection.document("Moderator").set(moderatorRole).get();

                System.out.println("Sample roles (including Moderator) added successfully.");
            } catch (Exception e) {
                System.err.println("Error adding roles: " + e.getMessage());
            }
        }
        @Bean
        public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
            return args -> {
                System.out.println("Mapped endpoints:");
                RequestMappingHandlerMapping requestMappingHandlerMapping = ctx.getBean(RequestMappingHandlerMapping.class);
                requestMappingHandlerMapping.getHandlerMethods().forEach((key, value) -> System.out.println(key + " : " + value));
            };
        }

        /**
         * Seeds moderator actions into Firestore.
         */
        public static void seedModeratorActions() {
            Firestore db = FirestoreClient.getFirestore();
            CollectionReference moderationActionsCollection = db.collection("ModerationActions");

            Map<String, Object> flagAction = new HashMap<>();
            flagAction.put("actionId", "modAction001");
            flagAction.put("moderatorId", "moderator001");
            flagAction.put("contentId", "content123");
            flagAction.put("action", "Flagged for inappropriate content");
            flagAction.put("timestamp", "2024-11-23T15:30:00");

            try {
                moderationActionsCollection.document("modAction001").set(flagAction).get();
                System.out.println("Sample moderator actions added successfully.");
            } catch (Exception e) {
                System.err.println("Error adding moderator actions: " + e.getMessage());
            }
        }

        /*
         * Seeds comment data into Firestore.
         */
        public static void seedComments() {
            Firestore db = FirestoreClient.getFirestore();
            CollectionReference commentsCollection = db.collection("Comments");

            Map<String, Object> comment1 = new HashMap<>();
            comment1.put("commentId", "comment001");
            comment1.put("postId", "post001"); // References a Post
            comment1.put("userId", "user123"); // References a User
            comment1.put("content", "Great update! Can't wait to try it.");
            comment1.put("timestamp", "2024-11-23T15:30:00");

            try {
                commentsCollection.document("comment001").set(comment1).get();
                System.out.println("Sample comments added successfully.");
            } catch (Exception e) {
                System.err.println("Error adding comments: " + e.getMessage());
            }
        }

        /**
         * Seeds bookmark data into Firestore.
         */
        public static void seedBookmarks() {
            Firestore db = FirestoreClient.getFirestore();
            CollectionReference bookmarksCollection = db.collection("Bookmarks");

            Map<String, Object> bookmark1 = new HashMap<>();
            bookmark1.put("userId", "user123");
            bookmark1.put("contentId", "content123"); // References Game Content

            try {
                bookmarksCollection.document("bookmark1").set(bookmark1).get();
                System.out.println("Sample bookmarks added successfully.");
            } catch (Exception e) {
                System.err.println("Error adding bookmarks: " + e.getMessage());
            }
        }
    }
}