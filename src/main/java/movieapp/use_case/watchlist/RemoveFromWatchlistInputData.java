package movieapp.use_case.watchlist;

public class RemoveFromWatchlistInputData {
    private final String username;
    private final int movieId;
    private final String movieTitle; // For display purposes

    public RemoveFromWatchlistInputData(String username, int movieId, String movieTitle) {
        this.username = username;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
    }

    public String getUsername() {
        return username;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }
}
