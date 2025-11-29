package movieapp.use_case.rating;

public class RatingOutputData {
    private final boolean success;
    private final String message;
    private final Integer newRating;

    public RatingOutputData(boolean success, String message, Integer newRating) {
        this.success = success;
        this.message = message;
        this.newRating = newRating;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Integer getNewRating() {
        return newRating;
    }
}