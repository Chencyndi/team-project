package movieapp.use_case.watchlist;

import movieapp.entity.Movie;

public class AddToWatchlistInputData {
    private final String username;
    private final Movie movie;

    public AddToWatchlistInputData(String username, Movie movie) {
        this.username = username;
        this.movie = movie;
    }

    public String getUsername() {
        return username;
    }

    public Movie getMovie() {
        return movie;
    }
}
