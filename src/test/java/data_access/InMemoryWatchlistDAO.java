package data_access;

import movieapp.entity.Movie;
import movieapp.entity.Watchlist;
import movieapp.use_case.watchlist.WatchlistDataAccessInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryWatchlistDAO implements WatchlistDataAccessInterface {

    private final Map<String, List<Movie>> watchlists = new HashMap<>();
    private String currentUsername;

    @Override
    public boolean addMovieToWatchlist(String username, Movie movie) {
        List<Movie> list = watchlists.computeIfAbsent(username, k -> new ArrayList<>());

        boolean exists = list.stream().anyMatch(m -> m.getId() == movie.getId());
        if (!exists) {
            list.add(movie);
        }
        return true;
    }

    @Override
    public boolean removeMovieFromWatchlist(String username, int movieId) {
        List<Movie> list = watchlists.get(username);
        if (list != null) {
            return list.removeIf(movie -> movie.getId() == movieId);
        }
        return false;
    }

    @Override
    public Watchlist getWatchlist(String username) {
        List<Movie> list = watchlists.getOrDefault(username, new ArrayList<>());
        return new Watchlist(new ArrayList<>(list));
    }

    @Override
    public boolean isInWatchlist(String username, int movieId) {
        List<Movie> list = watchlists.get(username);
        if (list != null) {
            return list.stream().anyMatch(movie -> movie.getId() == movieId);
        }
        return false;
    }

    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }

    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }

    public void clear() {
        watchlists.clear();
        currentUsername = null;
    }
}