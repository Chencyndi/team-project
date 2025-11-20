package movieapp.data_access;

import movieapp.entity.Movie;
import movieapp.entity.Watchlist;
import movieapp.use_case.watchlist.WatchlistDataAccessInterface;
import java.util.HashMap;
import java.util.Map;

public class InMemoryWatchlistDAO implements WatchlistDataAccessInterface {

    private final Map<String, Watchlist> watchlists = new HashMap<>();
    private String currentUsername;

    @Override
    public boolean addMovieToWatchlist(String username, Movie movie) {
        Watchlist watchlist = watchlists.computeIfAbsent(username, k -> new Watchlist());
        watchlist.addMovie(movie);
        return true;
    }

    @Override
    public boolean removeMovieFromWatchlist(String username, int movieId) {
        Watchlist watchlist = watchlists.get(username);
        if (watchlist == null) return false;

        Movie movieToRemove = watchlist.getList().stream()
                .filter(m -> m.getId() == movieId)
                .findFirst()
                .orElse(null);

        if (movieToRemove != null) {
            watchlist.removeMovie(movieToRemove);
            return true;
        }
        return false;
    }

    @Override
    public Watchlist getWatchlist(String username) {
        return watchlists.getOrDefault(username, new Watchlist());
    }

    @Override
    public boolean isInWatchlist(String username, int movieId) {
        Watchlist watchlist = watchlists.get(username);
        return watchlist != null && watchlist.containsMovieById(movieId);
    }

    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }

    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }
}