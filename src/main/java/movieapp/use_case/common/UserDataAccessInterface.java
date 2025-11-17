package movieapp.use_case.common;

import movieapp.entity.User;

import java.util.List;

public interface UserDataAccessInterface {
    boolean existsByUsername(String username);
    void save(User user);
    User findByUsername(String username);
    List<Integer> getWatchlist(String username);
    void addWatchlist(String movieID, String username);
    void removeWatchlist(Integer movieID, String username);
    Integer getMovieRating(String username, Integer movieID);
    void addRating(Integer movieID, String username, Integer rating);
    void removeRating(Integer movieID, String username);

}
