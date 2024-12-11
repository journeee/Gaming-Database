package edu.famu.gsdatabase.service;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import edu.famu.gsdatabase.models.*;
import org.springframework.stereotype.Service;

import java.util.*;

import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


@Service
public class UsersService {
    private Firestore firestore;

    public UsersService() {
        this.firestore = FirestoreClient.getFirestore();
    }

    public Users documentSnapshotToUser(DocumentSnapshot document) throws ExecutionException, InterruptedException {
        if (document.exists()) {
            Followers followers = null;
            DocumentReference followerRef = (DocumentReference) document.get("followersId");
            if (followerRef != null) {
                DocumentSnapshot followerSnapshot = followerRef.get().get();
                if (followerSnapshot.exists()) {
                    FollowersService service = new FollowersService();
                    followers = followerSnapshot.toObject(Followers.class);
                }
            }

            Following following = null;
            DocumentReference followingRef = (DocumentReference) document.get("followingId");
            if (followingRef != null) {
                DocumentSnapshot followingSnapshot = followingRef.get().get();
                if (followingSnapshot.exists()) {
                    following = followingSnapshot.toObject(Following.class);
                }
            }

            List<Gamecontent> bookmarks = new ArrayList<>();
            try {
                // Get bookmarks using the setBookmarks logic from RestUsers.java
                List<String> bookmarkIds = (List<String>) document.get("bookmarks");
                if (bookmarkIds != null) {
                    bookmarks = bookmarkIds.stream()
                            .filter(bookmarkId -> bookmarkId != null && !bookmarkId.isEmpty()) // Avoid null or empty bookmark IDs
                            .map(bookmarkId -> {
                                try {
                                    DocumentReference contentRef = firestore.collection("GameContent").document(bookmarkId);
                                    DocumentSnapshot contentSnapshot = contentRef.get().get();
                                    if (contentSnapshot.exists()) {
                                        GameContentService service = new GameContentService();
                                        return service.documentSnapshotToGameContent(contentSnapshot);
                                    }
                                } catch (Exception e) {
                                    System.out.println("Error fetching bookmark with ID: " + bookmarkId + " - " + e.getMessage());
                                }
                                return null;
                            })
                            .filter(gamecontent -> gamecontent != null) // Filter out null results
                            .collect(Collectors.toList());
                }
            } catch (Exception e) {
                System.out.println("Error fetching bookmarks: " + e.getMessage());
                e.printStackTrace();
            }


            List<Comments> comments = new ArrayList<>();
            Object commentListObject = document.get("comments");
            if (commentListObject instanceof List) {
                List<?> commentList = (List<?>) commentListObject;
                for (Object obj : commentList) {
                    if (obj instanceof DocumentReference) {
                        DocumentReference commentRef = (DocumentReference) obj;
                        DocumentSnapshot commentSnapshot = commentRef.get().get();
                        if (commentSnapshot.exists()) {
                            CommentsService service = new CommentsService();
                            comments.add(service.documentSnapshotToComments(commentSnapshot));
                        }
                    }
                }
            }



            List<Gamecontent> uploadedContent = new ArrayList<>();
            Object contentListObject = document.get("uploadedContent");
            if (contentListObject instanceof List) {
                List<?> contentList = (List<?>) contentListObject;
                for (Object obj : contentList) {
                    if (obj instanceof DocumentReference) {
                        DocumentReference contentRef = (DocumentReference) obj;
                        DocumentSnapshot contentSnapshot = contentRef.get().get();
                        if (contentSnapshot.exists()) {
                            GameContentService service = new GameContentService();
                            uploadedContent.add(service.documentSnapshotToGameContent(contentSnapshot));
                        }
                    }
                }
            }

            return new Users(
                    document.getId(),
                    document.getString("username"),
                    document.getString("bio"),
                    document.getString("email"),
                    document.getString("password"),
                    document.getString("firstName"),
                    document.getString("lastName"),
                    document.getString("status"),
                    document.getBoolean("admin"),
                    document.getBoolean("creator"),
                    document.getBoolean("moderator"),
                    document.getLong("totalUploads"),
                    document.getString("department"),
                    document.getLong("totalModeratorInteractions"),
                    comments.isEmpty() ? null : comments, // Check if comments is empty

                   // followers,
                  //  following,
                  //  comments,
                 //   bookmarks,
                    uploadedContent,
                    (List<String>) document.get("followers"),
                    (List<String>) document.get("following"),
                    (List<String>) document.get("bookmarks")
            );
        }
        return null;
    }

    public List<Users> getAllUsers() throws ExecutionException, InterruptedException {
        CollectionReference userCollection = firestore.collection("User");
        ApiFuture<QuerySnapshot> future = userCollection.get();
        List<Users> userList = new ArrayList<>();
        for (DocumentSnapshot document : future.get().getDocuments()) {
            Users user = documentSnapshotToUser(document);
            if (user != null) {
                userList.add(user);
            }
        }
        return userList;
    }

    public Users getUserById(String userId) throws ExecutionException, InterruptedException {
        CollectionReference userCollection = firestore.collection("User");
        ApiFuture<DocumentSnapshot> future = userCollection.document(userId).get();
        DocumentSnapshot document = future.get();
        return documentSnapshotToUser(document);
    }

    public Users getUserByUsernamePassword(String username, String password) throws ExecutionException, InterruptedException {
        CollectionReference userCollection = firestore.collection("User");
        Query query = userCollection.whereEqualTo("username", username).whereEqualTo("password", password);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        if (!documents.isEmpty()) {
            DocumentSnapshot document = documents.get(0); // Assuming usernames are unique
            return documentSnapshotToUser(document);
        }
        return null;
    }

    public String createUser(Users user) throws ExecutionException, InterruptedException {
        CollectionReference userCollection = firestore.collection("User");
        ApiFuture<DocumentReference> future = userCollection.add(user);
        DocumentReference docref = future.get();
        return docref.getId();
    }

    public WriteResult removeUserByUsername(String username) throws ExecutionException, InterruptedException {
        CollectionReference usersCollection = firestore.collection("User");
        Query query = usersCollection.whereEqualTo("username", username);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        if (!documents.isEmpty()) {
            DocumentReference userRef = documents.get(0).getReference();
            return userRef.delete().get();
        } else {
            throw new IllegalArgumentException("User with username " + username + " not found.");
        }
    }

     public void updateFollowing(String username, String creatorUsername) throws ExecutionException, InterruptedException {
        Users user = getUserByUsername(username);
        if (user != null) {
            List<String> following = user.getFollowing();
            if (!following.contains(creatorUsername)) {
                following.add(creatorUsername);
                firestore.collection("User").document(user.getUserId()).set(user);
            }
        } else {
            throw new RuntimeException("User not found with username: " + username);
        }
    }

    public Users getUserByUsername(String username) throws ExecutionException, InterruptedException {
        CollectionReference userCollection = firestore.collection("User");
        Query query = userCollection.whereEqualTo("username", username);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        if (!documents.isEmpty()) {
            DocumentSnapshot document = documents.get(0); // Assuming usernames are unique
            return document.toObject(Users.class);
        }
        return null;
    }


    public WriteResult unfollowUser(String username, String creatorUsername) throws ExecutionException, InterruptedException {
            Users user = getUserByUsername(username);
            if (user != null) {
                List<String> updatedFollowing = user.getFollowing().stream()
                        .filter(followingUsername -> !followingUsername.equals(creatorUsername))
                        .collect(Collectors.toList());
                user.setFollowing(updatedFollowing);
                DocumentReference userRef = firestore.collection("User").document(username);
                return userRef.set(user).get();
            } else {
                System.out.println("User " + username + " not found.");
            }
            return null;
    }






    public WriteResult updateUser(String userId, Users user) throws ExecutionException, InterruptedException {
        DocumentReference userRef = firestore.collection("User").document(userId);
        return userRef.set(user).get();
    }
    public WriteResult updateUserStatus(String userId, String status) throws ExecutionException, InterruptedException {
        DocumentReference userRef = firestore.collection("User").document(userId);
        ApiFuture<WriteResult> writeResult = userRef.update("status", status);
        return writeResult.get();
    }

    public List<String> getFollowing(String username) throws ExecutionException, InterruptedException {
        AUsers user = getUserByUsername(username);
        return user != null ? user.getFollowing() : Collections.emptyList();
    }

    // Added explicit methods for handling bookmarks
    public List<Gamecontent> getUserBookmarks(String userId) throws ExecutionException, InterruptedException {
        DocumentReference userRef = firestore.collection("User").document(userId);
        DocumentSnapshot document = userRef.get().get();

        if (document.exists()) {
            List<String> bookmarkIds = (List<String>) document.get("bookmarks");
            if (bookmarkIds == null) {
                bookmarkIds = new ArrayList<>(); // Return empty list if bookmarks field is missing
            }
            return bookmarkIds.stream()
                    .filter(bookmarkId -> bookmarkId != null && !bookmarkId.isEmpty()) // Skip empty strings
                    .map(bookmarkId -> {
                        try {
                            DocumentReference contentRef = firestore.collection("GameContent").document(bookmarkId);
                            DocumentSnapshot contentSnapshot = contentRef.get().get();
                            if (contentSnapshot.exists()) {
                                GameContentService service = new GameContentService();
                                return service.documentSnapshotToGameContent(contentSnapshot);
                            }
                        } catch (Exception e) {
                            System.out.println("Error fetching bookmark with ID: " + bookmarkId + " - " + e.getMessage());
                        }
                        return null;
                    })
                    .filter(gamecontent -> gamecontent != null)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public void addBookmark(String userId, String contentId) throws ExecutionException, InterruptedException {
        if (contentId == null || contentId.isEmpty()) {
            return; // Skip empty string contentId
        }

        DocumentReference userRef = firestore.collection("User").document(userId);
        DocumentSnapshot document = userRef.get().get();

        if (document.exists()) {
            List<String> bookmarks = (List<String>) document.get("bookmarks");
            if (bookmarks == null) {
                bookmarks = new ArrayList<>();
            }
            if (!bookmarks.contains(contentId)) {
                bookmarks.add(contentId);
                userRef.update("bookmarks", bookmarks).get();
            }
        }
    }


    public void removeBookmark(String userId, String contentId) throws ExecutionException, InterruptedException {
        DocumentReference userRef = firestore.collection("User").document(userId);
        DocumentSnapshot document = userRef.get().get();

        if (document.exists()) {
            List<String> bookmarks = (List<String>) document.get("bookmarks");
            if (bookmarks != null && bookmarks.contains(contentId)) {
                bookmarks.remove(contentId);
                userRef.update("bookmarks", bookmarks).get();
            }
        }
    }

    public Map<String, Object> getEngagementMetrics() throws ExecutionException, InterruptedException {
        // Calculate engagement
        CollectionReference userCollection = firestore.collection("User");
        ApiFuture<QuerySnapshot> future = userCollection.get();

        long activeUsers = 0;
        long bannedUsers = 0;

        for (DocumentSnapshot document : future.get().getDocuments()) {
            String status = document.getString("status");
            if ("active".equalsIgnoreCase(status)) {
                activeUsers++;
            } else if ("banned".equalsIgnoreCase(status)) {
                bannedUsers++;
            }
        }

        // Return engagement metrics
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalUsers", future.get().getDocuments().size());
        metrics.put("activeUsers", activeUsers);
        metrics.put("bannedUsers", bannedUsers);

        return metrics;
    }

    public Map<String, Object> getContentInteractionMetrics() throws ExecutionException, InterruptedException {
        // Calculate interactions
        CollectionReference contentCollection = firestore.collection("GameContent");
        ApiFuture<QuerySnapshot> future = contentCollection.get();

        long totalViews = 0;
        long totalLikes = 0; // Placeholder if "likes" are added in the future
        long totalComments = 0;

        for (DocumentSnapshot document : future.get().getDocuments()) {
            totalViews += document.getLong("views") != null ? document.getLong("views") : 0;
            // Extend here to calculate likes/comments if stored
        }

        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalContent", future.get().getDocuments().size());
        metrics.put("totalViews", totalViews);
        metrics.put("totalComments", totalComments);

        return metrics;
    }

    public Map<String, Object> getSubscriptionTrends() {
        // Placeholder: Implement subscription-specific trends if data exists
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("subscriptionsActive", 0); // Replace with real calculation
        metrics.put("subscriptionsCanceled", 0); // Replace with real calculation
        return metrics;
    }

    public Map<String, Object> generateAdminReport() throws ExecutionException, InterruptedException {
        Map<String, Object> report = new HashMap<>();

        // Fetch all users
        List<Users> users = getAllUsers();

        // User engagement metrics
        long totalUsers = users.size();
        long activeUsers = users.stream().filter(user -> "active".equalsIgnoreCase(user.getStatus())).count();
        long bannedUsers = users.stream().filter(user -> "banned".equalsIgnoreCase(user.getStatus())).count();
        long creators = users.stream().filter(Users::isCreator).count();
        long moderators = users.stream().filter(Users::isModerator).count();
        long admins = users.stream().filter(Users::isAdmin).count();

        // Content interaction metrics
        CollectionReference contentCollection = firestore.collection("GameContent");
        ApiFuture<QuerySnapshot> contentFuture = contentCollection.get();
        List<QueryDocumentSnapshot> contentDocs = contentFuture.get().getDocuments();

        long totalPosts = contentDocs.size();
        long totalViews = contentDocs.stream()
                .mapToLong(doc -> doc.getLong("views") != null ? doc.getLong("views") : 0)
                .sum();
        long flaggedContent = contentDocs.stream()
                .filter(doc -> "Flagged".equalsIgnoreCase(doc.getString("status")))
                .count();

        // Subscription trends
        long premiumUsers = users.stream().filter(user -> "premium".equalsIgnoreCase(user.getDepartment())).count();
        double regularSubscribersPercentage = totalUsers > 0
                ? ((double) (totalUsers - premiumUsers) / totalUsers) * 100
                : 0;

        // Adding metrics to report
        report.put("totalUsers", totalUsers);
        report.put("activeUsers", activeUsers);
        report.put("bannedUsers", bannedUsers);
        report.put("creators", creators);
        report.put("moderators", moderators);
        report.put("admins", admins);
        report.put("totalPosts", totalPosts);
        report.put("totalViews", totalViews);
        report.put("flaggedContent", flaggedContent);
        report.put("premiumSubscribers", premiumUsers);
        report.put("regularSubscribersPercentage", regularSubscribersPercentage);

        return report;
    }

}

