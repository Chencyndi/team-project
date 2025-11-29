package movieapp.data_access;

import movieapp.entity.Movie;
import movieapp.entity.Watchlist;
import movieapp.use_case.watchlist.WatchlistDataAccessInterface;

public class DatabaseWatchlistDAO implements WatchlistDataAccessInterface {

    private final UserDataAccessObject jsonDAO = new UserDataAccessObject();
    private String currentUsername;

    @Override
    public boolean addMovieToWatchlist(String username, Movie movie) {
        jsonDAO.addWatchlist(movie.getId(), username);
        return true;
    }

    @Override
    public boolean removeMovieFromWatchlist(String username, int movieId) {
        jsonDAO.removeWatchlist(movieId, username);
        return true;
    }

    @Override
    public Watchlist getWatchlist(String username) {
        return jsonDAO.getWatchlist(username);
    }

    @Override
    public boolean isInWatchlist(String username, int movieId) {
        Watchlist currentList = jsonDAO.getWatchlist(username);
        if (currentList != null && currentList.getList() != null) {
            for (Movie m : currentList.getList()) {
                if (m.getId() == movieId) return true;
            }
        }
        return false;
    }

    @Override
    public String getCurrentUsername() {
        return this.currentUsername;
    }

    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }
}