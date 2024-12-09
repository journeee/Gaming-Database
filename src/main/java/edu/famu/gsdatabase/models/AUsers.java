package edu.famu.gsdatabase.models;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.protobuf.util.Timestamps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.text.ParseException;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AUsers {
    @DocumentId
    protected @Nullable String userId;
    protected String username;
    protected String bio;
    protected String email;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected String status;
    protected boolean admin;
    protected boolean creator;
    protected boolean moderator;
    protected long totalUploads;
    protected String department;
    protected long totalModeratorInteractions;
    protected List<String> followers;
    protected List<String> following;
    protected List<String> bookmarks;

    public String getRole() {
        if (this.admin) {
            return "Admin";
        } else if (this.creator) {
            return "ContentCreator";
        } else if (this.moderator) {
            return "Moderator";
        } else {
            return "RegularUser";
        }
    }

}

