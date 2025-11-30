package movieapp.use_case.rating;

public class RatingOutputData {
    private final boolean success;
    private final String message;
    private final Integer newRating;
    private final Double averageRating;

    public RatingOutputData(boolean success, String message, Integer newRating, Double averageRating) {
        this.success = success;
        this.message = message;
        this.newRating = newRating;
        this.averageRating = averageRating;

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

    public Double getAverageRating() { return averageRating; }
}