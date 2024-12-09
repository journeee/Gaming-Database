package edu.famu.gsdatabase.models;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.protobuf.util.Timestamps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.text.ParseException;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AComments {
    @DocumentId
    protected @Nullable String commentId;
    protected String content;
    protected String status;
    protected Timestamp uploadDate;

    public void setUploadDate(String uploadDate)throws ParseException {
        this.uploadDate = Timestamp.fromProto(Timestamps.parse(uploadDate));
    }
}
