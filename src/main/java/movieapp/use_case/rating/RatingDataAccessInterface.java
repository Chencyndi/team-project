package movieapp.use_case.rating;

public interface RatingDataAccessInterface {

    void addRating(Integer movieID, String username, Integer rating);

    void removeRating(Integer movieID, String username);

    Integer getMovieRating(String username, Integer movieID);
}