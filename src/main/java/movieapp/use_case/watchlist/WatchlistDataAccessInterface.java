package movieapp.use_case.watchlist;

import movieapp.entity.Movie;
import movieapp.entity.Watchlist;

public interface WatchlistDataAccessInterface {

    /**
     * Adds a movie to the user's watchlist.
     * @param username the user's username
     * @param movie the movie to add
     * @return true if successful, false otherwise
     */
    boolean addMovieToWatchlist(String username, Movie movie);

    /**
     * Removes a movie from the user's watchlist.
     * @param username the user's username
     * @param movieId the ID of the movie to remove
     * @return true if successful, false otherwise
     */
    boolean removeMovieFromWatchlist(String username, int movieId);

    /**
     * Gets the user's complete watchlist.
     * @param username the user's username
     * @return the user's watchlist
     */
    Watchlist getWatchlist(String username);

    /**
     * Checks if a movie is in the user's watchlist.
     * @param username the user's username
     * @param movieId the movie's ID
     * @return true if the movie is in the watchlist, false otherwise
     */
    boolean isInWatchlist(String username, int movieId);

    /**
     * Gets the current logged-in user's username.
     * @return the current user's username, or null if no user is logged in
     */
    String getCurrentUsername();
}