package movieapp.interface_adapter.rating;

public class RatingState {
    private String message = "";
    private boolean success = false;
    private Integer currentRating = null;
    private String averageRatingLabel = "N/A";

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public Integer getCurrentRating() { return currentRating; }
    public void setCurrentRating(Integer rating) { this.currentRating = rating; }

    public String getAverageRatingLabel() { return averageRatingLabel; }
    public void setAverageRatingLabel(String label) { this.averageRatingLabel = label; }
}