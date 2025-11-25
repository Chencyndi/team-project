package movieapp.use_case.rating;

import movieapp.use_case.common.UserDataAccessInterface;

public class RatingInteractor implements RatingInputBoundary {
    private final UserDataAccessInterface dataAccess;
    private final RatingOutputBoundary presenter;

    public RatingInteractor(UserDataAccessInterface dataAccess, RatingOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public RatingOutputData execute(RatingInputData inputData) {
        if (inputData.getRating() < 1 || inputData.getRating() > 10) {
            RatingOutputData output = new RatingOutputData(false, "Rating must be between 1 and 10", null);
            presenter.prepareFailView(output.getMessage());
            return output;
        }
        try {
            dataAccess.addRating(inputData.getMovieID(), inputData.getUsername(), inputData.getRating());

            RatingOutputData output = new RatingOutputData(true, "Rating submitted successfully", inputData.getRating());
            presenter.prepareSuccessView(output);
            return output;
        } catch (Exception e) {
            RatingOutputData output = new RatingOutputData(false, "Failed to submit rating: " + e.getMessage(), null);
            presenter.prepareFailView(output.getMessage());
            return output;
        }
    }

    @Override
    public RatingOutputData removeRating(String username, Integer movieID) {
        try {
            dataAccess.removeRating(movieID, username);
            RatingOutputData output = new RatingOutputData(true, "Rating removed successfully", null);
            presenter.prepareSuccessView(output);
            return output;
        } catch (Exception e) {
            RatingOutputData output = new RatingOutputData(false, "Failed to remove rating: " + e.getMessage(), null);
            presenter.prepareFailView(output.getMessage());
            return output;
        }
    }
}
