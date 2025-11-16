package movieapp.entity;

import java.util.Objects;

public class User {
    private final String username;
    private final String password;
    private final Watchlist watchlist;

    public User(String username, String password, Watchlist watchlist) {
        if ("".equals(username)) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if ("".equals(password)) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        this.username = username;
        this.password = password;
        this.watchlist = watchlist;
    }
    public User(String username, String password) {
        if ("".equals(username)) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if ("".equals(password)) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        this.username = username;
        this.password = password;
        this.watchlist = null;
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

    public boolean validateCredentials(String password) {
        return this.password.equals(password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
