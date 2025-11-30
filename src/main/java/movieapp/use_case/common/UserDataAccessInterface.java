package movieapp.use_case.common;

import movieapp.entity.User;
import movieapp.entity.Watchlist;

import java.util.List;

public interface UserDataAccessInterface {
    boolean existsByUsername(String username);
    void addUser(User user);
    User findByUsername(String username);
    Watchlist getWatchlist(String username);
    void addWatchlist(Integer movieID, String username);
    void removeWatchlist(Integer movieID, String username);
    Integer getMovieRating(String username, Integer movieID);
    void addRating(Integer movieID, String username, Integer rating);
    void removeRating(Integer movieID, String username);
}
