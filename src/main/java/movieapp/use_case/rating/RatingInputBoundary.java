package movieapp.use_case.rating;

public interface RatingInputBoundary {
    RatingOutputData execute(RatingInputData inputData);

    RatingOutputData removeRating(String username, Integer movieID);
}
