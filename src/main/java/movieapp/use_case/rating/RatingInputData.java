package movieapp.use_case.rating;

public class RatingInputData {
    private final String username;
    private final Integer movieID;
    private final Integer rating;

    public RatingInputData(String username, Integer movieID, Integer rating) {
        this.username = username;
        this.movieID = movieID;
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public Integer getMovieID() {
        return movieID;
    }

    public Integer getRating() {
        return rating;
    }
}

