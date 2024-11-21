package model;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.firebase.database.annotations.Nullable;
import com.google.protobuf.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseGame {
    @DocumentId
    protected @Nullable String gameId;
    protected String title;
    protected String developer;
    protected String publisher;
    protected @Nullable Timestamp releaseDate;
    protected String description;
    protected boolean inStock;
}
