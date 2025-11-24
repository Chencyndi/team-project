package movieapp.use_case.watchlist;

public interface RemoveFromWatchlistOutputBoundary {
    void prepareSuccessView(RemoveFromWatchlistOutputData outputData);
    void prepareFailView(String errorMessage);
}