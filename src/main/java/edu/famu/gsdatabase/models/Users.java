package edu.famu.gsdatabase.models;

import com.google.cloud.Timestamp;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;
@Data
@NoArgsConstructor
public class Users extends AUsers{


    private @Nullable List<Comments> comments;
   // private List<Gamecontent> bookmarks;
    private List<Gamecontent> uploadedContent;

    public Users(String userId, String username, String bio, String email, String password, String firstName,
                 String lastName,String creatorStatus, boolean admin, boolean creator, boolean moderator, long totalUpload,
                 String department, long totalModeratorInteractions,
                 List<Comments> comments,List<Gamecontent> uploadedContent,
    List<String> followers, List<String> following,List<String> bookmarks){
        super(userId, username, bio, email, password, firstName, lastName,creatorStatus, admin, creator, moderator, totalUpload,
                department, totalModeratorInteractions, followers,following, bookmarks);

       // this.followers = followers;
        //this.following = following;
        this.comments = comments;
        //this.bookmarks = bookmarks;
        this.uploadedContent = uploadedContent;
    }

}
