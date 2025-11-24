package movieapp.use_case.search;

import movieapp.entity.Movie;
import java.util.List;

public class SearchOutputData {
    private final boolean success;
    private final String message;
    private final List<Movie> movies;

    public SearchOutputData(boolean success, String message, List<Movie> movies) {
        this.success = success;
        this.message = message;
        this.movies = movies;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public List<Movie> getMovies() { return movies; }
}