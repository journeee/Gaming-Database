package edu.famu.gsdatabase.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.firebase.auth.UserRecord;
import edu.famu.gsdatabase.models.BaseUser;
import edu.famu.gsdatabase.models.Bookmark;
import edu.famu.gsdatabase.models.GameContent;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
public class User extends BaseUser {
    private List<GameContent> savedGames;
    private List<Bookmark> savedBookmarks;

    public User(String userId, String username, String email, boolean isActive, String identifier, String password,
                    List<GameContent> savedGames, List<Bookmark> savedBookmarks) {
        super(userId, username, email, isActive, password, identifier);
        this.savedGames = savedGames;
        this.savedBookmarks = savedBookmarks;
    }


    @Override
    public void performRoleSpecificTask() {
        // Implementation for game user-specific tasks
    }
}