package movieapp.interface_adapter.rating;

import movieapp.use_case.rating.RatingOutputBoundary;
import movieapp.use_case.rating.RatingOutputData;

public class RatingPresenter implements RatingOutputBoundary {
    private final RatingViewModel viewModel;

    public RatingPresenter(RatingViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(RatingOutputData outputData) {
        RatingState state = viewModel.getState();
        state.setSuccess(true);
        state.setMessage(outputData.getMessage());
        state.setCurrentRating(outputData.getNewRating());

        if (outputData.getAverageRating() != null) {
            state.setAverageRatingLabel(outputData.getAverageRating() + "/10");
        } else {
            state.setAverageRatingLabel("N/A");
        }

        viewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        RatingState state = viewModel.getState();
        state.setSuccess(false);
        state.setMessage(error);
        viewModel.firePropertyChange();
    }
}