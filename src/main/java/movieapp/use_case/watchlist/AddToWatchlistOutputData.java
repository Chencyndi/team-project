package movieapp.use_case.watchlist;

public class AddToWatchlistOutputData {
    private final String movieTitle;
    private final boolean success;
    private final String message;

    public AddToWatchlistOutputData(String movieTitle, boolean success, String message) {
        this.movieTitle = movieTitle;
        this.success = success;
        this.message = message;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
