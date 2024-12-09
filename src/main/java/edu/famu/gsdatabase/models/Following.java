package edu.famu.gsdatabase.models;

import com.google.cloud.firestore.annotation.DocumentId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Following{
    @DocumentId
    protected String followingId;
    protected String user;
    protected List<String> following;
}
