package movieapp.use_case.watchlist;

public interface AddToWatchlistOutputBoundary {
    void prepareSuccessView(AddToWatchlistOutputData outputData);
    void prepareFailView(String errorMessage);
}
