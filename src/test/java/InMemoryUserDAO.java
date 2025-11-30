import movieapp.entity.Movie;
import movieapp.entity.User;
import movieapp.entity.Watchlist;
import movieapp.use_case.common.UserDataAccessInterface;
import movieapp.use_case.rating.RatingDataAccessInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryUserDAO implements UserDataAccessInterface, RatingDataAccessInterface {

    private final Map<String, User> users = new HashMap<>();
    private final Map<String, Map<Integer, Integer>> userRatings = new HashMap<>();
    private final Map<String, List<Integer>> userWatchlists = new HashMap<>();

    @Override
    public boolean existsByUsername(String username) {
        return users.containsKey(username);
    }

    @Override
    public void addUser(User user) {
        users.put(user.getUsername(), user);
        userRatings.put(user.getUsername(), new HashMap<>());
        userWatchlists.put(user.getUsername(), new ArrayList<>());
    }

    @Override
    public User findByUsername(String username) {
        return users.get(username);
    }

    @Override
    public Watchlist getWatchlist(String username) {
        List<Integer> ids = userWatchlists.getOrDefault(username, new ArrayList<>());
        List<Movie> movies = new ArrayList<>();

        for (Integer id : ids) {
            Movie dummyMovie = new Movie(id, "Test Movie " + id, "Test overview", "2025-01-01", "", 7.5, 100.0, 1000, new ArrayList<>());
            movies.add(dummyMovie);
        }
        return new Watchlist(movies);
    }

    @Override
    public void addWatchlist(Integer movieID, String username) {
        List<Integer> list = userWatchlists.computeIfAbsent(username, k -> new ArrayList<>());
        if (!list.contains(movieID)) {
            list.add(movieID);
        }
    }

    @Override
    public void removeWatchlist(Integer movieID, String username) {
        if (userWatchlists.containsKey(username)) {
            userWatchlists.get(username).remove(movieID);
        }
    }

    @Override
    public Integer getMovieRating(String username, Integer movieID) {
        if (userRatings.containsKey(username)) {
            return userRatings.get(username).get(movieID);
        }
        return null;
    }

    @Override
    public void addRating(Integer movieID, String username, Integer rating) {
        userRatings.computeIfAbsent(username, k -> new HashMap<>()).put(movieID, rating);
    }

    @Override
    public void removeRating(Integer movieID, String username) {
        if (userRatings.containsKey(username)) {
            userRatings.get(username).remove(movieID);
        }
    }

    public void clear() {
        users.clear();
        userRatings.clear();
        userWatchlists.clear();
    }
}