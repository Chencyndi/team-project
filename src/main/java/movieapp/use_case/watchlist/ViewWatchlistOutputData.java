package movieapp.use_case.watchlist;

import movieapp.entity.Movie;
import java.util.List;

public class ViewWatchlistOutputData {
    private final List<Movie> movies;
    private final boolean success;

    public ViewWatchlistOutputData(List<Movie> movies, boolean success) {
        this.movies = movies;
        this.success = success;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public boolean isSuccess() {
        return success;
    }
}
