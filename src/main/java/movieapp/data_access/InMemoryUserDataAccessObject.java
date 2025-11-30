package movieapp.data_access;

import movieapp.entity.User;
import movieapp.entity.Watchlist;
import movieapp.use_case.common.UserDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * In-memory implementation of the DAO for storing user data. This implementation does
 * NOT persist data between runs of the program.
 */

// for tests
public class InMemoryUserDataAccessObject implements UserDataAccessInterface {

    private final Map<String, User> users = new HashMap<>();

    private String currentUsername;

    @Override
    public boolean existsByUsername(String identifier) {
        return users.containsKey(identifier);
    }

    @Override
    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }

    @Override
    public User findByUsername(String username) {
        return users.get(username);
    }

    @Override
    public Watchlist getWatchlist(String username) {
        return null;
    }

    @Override
    public void addWatchlist(Integer movieID, String username) {

    }

    @Override
    public void removeWatchlist(Integer movieID, String username) {

    }

    @Override
    public Integer getMovieRating(String username, Integer movieID) {
        return 0;
    }

    @Override
    public void addRating(Integer movieID, String username, Integer rating) {

    }

    @Override
    public void removeRating(Integer movieID, String username) {

    }

}
