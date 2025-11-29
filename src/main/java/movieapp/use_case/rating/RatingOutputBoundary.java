package movieapp.use_case.rating;

public interface RatingOutputBoundary {
    void prepareSuccessView(RatingOutputData outputData);

    void prepareFailView(String error);
}
