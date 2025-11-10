package Entity;

public class User {
    private final String username;
    private final String password;
    private final Watchlist watchlist;

    public User(String username, String password, Watchlist watchlist) {
        this.username = username;
        this.password = password;
        this.watchlist = watchlist;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public Watchlist getWatchlist() {
        return watchlist;
    }
}
