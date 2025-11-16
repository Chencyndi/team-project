package Entity;

public class User {
    private final String username;
    private final String password;
    private final MovieList movieList;

    public User(String username, String password, MovieList watchlist) {
        this.username = username;
        this.password = password;
        this.movieList = watchlist;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public MovieList getMovieList() {
        return movieList;
    }
}
