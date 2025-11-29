package movieapp.interface_adapter.rating;

public class RatingState {
    private String message = "";
    private boolean success = false;
    private Integer currentRating = null;

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public Integer getCurrentRating() { return currentRating; }
    public void setCurrentRating(Integer rating) { this.currentRating = rating; }
}