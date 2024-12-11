package edu.famu.gsdatabase.models;

import edu.famu.gsdatabase.util.Utility;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class RestGameContent extends AGameContent {
    //private DocumentReference creator;

    public RestGameContent(String contentId, String description, boolean exclusive, double rating, String status, List<String> tags, String title, String type, long views, String creator) {

    }

   public void setCreator(String user){
        this.creator = String.valueOf(Utility.retrieveDocumentReference("User", user));
    }

}
