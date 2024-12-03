package edu.famu.gsdatabase.models;

import com.google.cloud.firestore.DocumentReference;
import edu.famu.gsdatabase.util.utility;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
class RestUser extends BaseUser {
    private List<DocumentReference> savedGames;
    private List<DocumentReference> savedBookmarks;

    public RestUser(String userId, String username, String email, boolean isActive, String password,String identifier,
                        List<DocumentReference> savedGames, List<DocumentReference> savedBookmarks) {
        super(userId, username, email, isActive, password, identifier);
        this.savedGames = savedGames;
        this.savedBookmarks = savedBookmarks;
    }

    public void setSavedGames(List<String> savedGames) {
        this.savedGames = new ArrayList<>();
        for (String game : savedGames) {
            this.savedGames.add(utility.retrieveDocumentReference("Game", game));
        }
    }

    public void setSavedBookmarks(List<String> savedBookmarks) {
        this.savedBookmarks = new ArrayList<>();
        for (String bookmark : savedBookmarks) {
            this.savedBookmarks.add(utility.retrieveDocumentReference("Bookmark", bookmark));
        }
    }

    @Override
    public void performRoleSpecificTask() {
        // Implementation for rest user-specific tasks
    }
}