package edu.famu.gsdatabase.models;

import com.google.cloud.firestore.annotation.DocumentId;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

import java.util.List;
@Data
@NoArgsConstructor
public class Followers{
    @DocumentId
    protected String followersId;
    protected String user;
    protected List<String> followers;

}
