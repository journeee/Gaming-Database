package model;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class Game extends BaseGame {
    private ArrayList<Category> categories; // Multiple genres or categories

    public Game(@Nullable String gameId, String title, String developer, String publisher, @Nullable Timestamp releaseDate,
                String description, boolean inStock, ArrayList<Category> categories) {
        super(gameId, title, developer, publisher, releaseDate, description, inStock);
        this.categories = categories;
    }
}
