package edu.famu.gsdatabase.models;

import com.google.cloud.Timestamp;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Comments extends AComments{

    private Users Creator;
    private Gamecontent ParentContent;

    public Comments(String commentId, String content, String status, Timestamp uploadDate, Users Creator,Gamecontent ParentContent ){
        super(commentId,content,status, uploadDate);
        this.Creator = Creator;
        this.ParentContent=  ParentContent;
    }
}
