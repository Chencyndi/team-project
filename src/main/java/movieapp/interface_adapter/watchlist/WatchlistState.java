package movieapp.interface_adapter.watchlist;

import movieapp.entity.Movie;
import java.util.ArrayList;
import java.util.List;

/**
 * State class for the Watchlist view.
 * Contains all data needed to display the watchlist.
 */
public class WatchlistState {
    private List<Movie> movies = new ArrayList<>();
    private String message;
    private String error;

    public WatchlistState() {
    }

    public WatchlistState(WatchlistState copy) {
        this.movies = new ArrayList<>(copy.movies);
        this.message = copy.message;
        this.error = copy.error;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}