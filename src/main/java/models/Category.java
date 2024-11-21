package model;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.firebase.database.annotations.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @DocumentId
    private @Nullable String categoryId;
    private String title;      // E.g., "Action", "RPG"
    private String description; // E.g., "Role-Playing Games"
    private String slug;
}
