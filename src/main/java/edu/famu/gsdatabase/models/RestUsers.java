package edu.famu.gsdatabase.models;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import edu.famu.gsdatabase.util.Utility;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
public class RestUsers extends AUsers{
  //  private DocumentReference followers;
   // private DocumentReference following;
    private @Nullable List<DocumentReference> comments;
  //  private  List<DocumentReference> bookmarks;
    private List<DocumentReference> uploadedContent;
    public RestUsers(String userId, String username, String bio, String email, String password, String firstName,
                     String lastName,String creatorStatus, boolean admin, boolean creator, boolean moderator, long totalUpload,
                     String department, long totalModeratorInteractions , List<DocumentReference> Comments,
                     List<DocumentReference> UploadedContent , List<String> followers, List<String> following,List<String> bookmarks){
        super(userId, username, bio, email, password, firstName, lastName,creatorStatus, admin, creator, moderator, totalUpload,
                department, totalModeratorInteractions, followers,following, bookmarks);

        //this.followers = followers;
       // this.following = following;
        this.comments = Comments;
        this.uploadedContent = UploadedContent;
    }

 /*   public void setFollowers(String user) {
        this.followers = Utility.retrieveDocumentReference("Followers", user);
       /*  this.followers = new ArrayList<>();
        for(String user : followers){
            this.followers.add(Utility.retrieveDocumentReference("User", user));
        }*
    }

    public void setFollowing(String user) {
        this.following = Utility.retrieveDocumentReference("Following", user);
       this.following = new ArrayList<>();
        for(String user : following){
            this.following.add(Utility.retrieveDocumentReference("User", user));

    }*/
    public void setComments(List<String> comments) {
        this.comments = new ArrayList<>();
        for(String comment : comments){
            this.comments.add(Utility.retrieveDocumentReference("Comments", comment));
        }
    }

    /*public void setBookmarks(List<String> bookmarks) {
        this.bookmarks = new ArrayList<>();
        for (String bookmark : bookmarks) {
            if (bookmark != null && !bookmark.isEmpty()) { // Null/empty check
                this.bookmarks.add(Utility.retrieveDocumentReference("GameContent", bookmark));
            }
        }
    }*/


    public void setUploadedContent(List<String> UploadedContent) {
        this.uploadedContent = new ArrayList<>();
        for(String content : UploadedContent){
            this.uploadedContent.add(Utility.retrieveDocumentReference("GameContent", content));
        }
    }
}
