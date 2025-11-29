package movieapp.interface_adapter.search;

import movieapp.entity.Movie;

import java.util.ArrayList;
import java.util.List;

public class SearchViewModel {

    private String lastQuery = "";
    private String message = "";
    private boolean success = false;
    private List<MovieViewModel> movies = new ArrayList<>();

    public String getLastQuery() {
        return lastQuery;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<MovieViewModel> getMovies() {
        return movies;
    }

    // Methods used by the presenter to update state:

    public void setLastQuery(String lastQuery) {
        this.lastQuery = lastQuery;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMovies(List<MovieViewModel> movies) {
        this.movies = new ArrayList<>(movies);
    }
}
