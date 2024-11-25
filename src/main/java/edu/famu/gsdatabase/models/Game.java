package edu.famu.gsdatabase.models;

public class Game {
    private String gameId;       // Unique identifier for the game
    private String title;        // Title of the game
    private String description;  // Description or summary of the game
    private String genre;        // Genre of the game (e.g., RPG, Shooter)
    private String developer;    // Developer of the game
    private String releaseDate;  // Release date of the game
    private boolean isPremium;   // Whether the game content is premium
    private String platform;     // Platforms (e.g., PC, Console)

    // Default Constructor (required by Firestore)
    public Game() {}

    // Constructor
    public Game(String gameId, String title, String description, String genre, String developer,
                String releaseDate, boolean isPremium, String platform) {
        this.gameId = gameId;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.developer = developer;
        this.releaseDate = releaseDate;
        this.isPremium = isPremium;
        this.platform = platform;
    }

    // Getters and Setters
    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameId='" + gameId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", genre='" + genre + '\'' +
                ", developer='" + developer + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", isPremium=" + isPremium +
                ", platform='" + platform + '\'' +
                '}';
    }
}
