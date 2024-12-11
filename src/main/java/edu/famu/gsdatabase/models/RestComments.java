package edu.famu.gsdatabase.models;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import edu.famu.gsdatabase.util.Utility;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
class RestComments extends AComments {
    private DocumentReference Creator;
    private DocumentReference ParentContent;

    // Constructor
    public RestComments(String commentId, String content, String status, Timestamp uploadDate,
                        String creatorId, String parentContentId) {
        super(commentId, content, status, uploadDate);
        this.Creator = Utility.retrieveDocumentReference("User", creatorId);
        this.ParentContent = Utility.retrieveDocumentReference("GameContent", parentContentId);
    }

    // Set Creator
    public void setCreator(String user) {
        this.Creator = Utility.retrieveDocumentReference("User", user);
    }

    // Set Parent Content
    public void setParentContent(String gameContent) {
        this.ParentContent = Utility.retrieveDocumentReference("GameContent", gameContent);
    }
}
