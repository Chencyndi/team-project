package movieapp.interface_adapter.rating;

import movieapp.use_case.rating.RatingInputBoundary;
import movieapp.use_case.rating.RatingInputData;
import movieapp.use_case.rating.RatingOutputData;

public class RatingController {
    private final RatingInputBoundary interactor;

    public RatingController(RatingInputBoundary interactor) {
        this.interactor = interactor;
    }

    public RatingOutputData execute(String username, Integer movieID, Integer rating) {
        RatingInputData inputData = new RatingInputData(username, movieID, rating);
        return interactor.execute(inputData);
    }

    public RatingOutputData removeRating(String username, Integer movieID) {
        return interactor.removeRating(username, movieID);
    }
}
