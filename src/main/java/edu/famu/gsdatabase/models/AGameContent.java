package edu.famu.gsdatabase.models;

import com.google.cloud.firestore.annotation.DocumentId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AGameContent {
    @DocumentId
    protected @Nullable String contentId;
    protected String description;
    protected boolean exclusive; // Added variable
    protected double rating;
    protected String status;
    protected List<String> tags;
    protected String title;
    protected String type;
    protected long views;
    protected String creator;




    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
