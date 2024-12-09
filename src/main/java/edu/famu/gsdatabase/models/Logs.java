package edu.famu.gsdatabase.models;

import com.google.cloud.firestore.annotation.DocumentId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class Logs {
  @DocumentId
  protected @Nullable String logId;
  protected String user;
  protected String activity;
}
