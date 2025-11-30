package movieapp.use_case.rating;

import movieapp.use_case.common.UserDataAccessInterface;

public class RatingInteractor implements RatingInputBoundary {
    private final RatingDataAccessInterface dataAccess;
    private final RatingOutputBoundary presenter;

    public RatingInteractor(RatingDataAccessInterface dataAccess, RatingOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public RatingOutputData execute(RatingInputData inputData) {
        if (inputData.getRating() < 1 || inputData.getRating() > 10) {
            presenter.prepareFailView("Rating must be between 1 and 10");
            return new RatingOutputData(false, "Something went wrong", null, null);
        }
        try {
            dataAccess.addRating(inputData.getMovieID(), inputData.getUsername(), inputData.getRating());
            Double avg = dataAccess.getAverageRating(inputData.getMovieID());
            RatingOutputData output = new RatingOutputData(true, "Rating submitted successfully", inputData.getRating(), avg);
            presenter.prepareSuccessView(output);
            return output;
        } catch (Exception e) {
            RatingOutputData output = new RatingOutputData(false, "Failed to submit rating: " + e.getMessage(), null, null);
            presenter.prepareFailView(output.getMessage());
            return output;
        }
    }

    @Override
    public RatingOutputData removeRating(String username, Integer movieID) {
        try {
            dataAccess.removeRating(movieID, username);
            Double avg = dataAccess.getAverageRating(movieID);
            RatingOutputData output = new RatingOutputData(true, "Rating removed successfully", null,avg);
            presenter.prepareSuccessView(output);
            return output;
        } catch (Exception e) {
            RatingOutputData output = new RatingOutputData(false, "Failed to remove rating: " + e.getMessage(), null,null);
            presenter.prepareFailView(output.getMessage());
            return output;
        }
    }
}
