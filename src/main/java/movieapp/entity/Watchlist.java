package movieapp.entity;

import java.util.ArrayList;
import java.util.List;

public class Watchlist {
    private final List<Movie> list;

    public Watchlist(List<Movie> list) {
        this.list = list;
    }

    public Watchlist() {
        this.list = new ArrayList<>();
    }

    public List<Movie> getList() {
        return list;
    }

    public void addMovie(Movie movie) {
        if (!list.contains(movie) && !containsMovieById(movie.getId())) {
            list.add(movie);
        }
    }

    public void removeMovie(Movie movie) {
        list.remove(movie);
    }

    public boolean containsMovie(Movie movie) {
        return list.contains(movie);
    }

    public boolean containsMovieById(int movieId) {
        return list.stream().anyMatch(m -> m.getId() == movieId);
    }

    public int getSize() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}
