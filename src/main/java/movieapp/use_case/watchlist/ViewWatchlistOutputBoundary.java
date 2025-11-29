package movieapp.use_case.watchlist;

public interface ViewWatchlistOutputBoundary {
    void prepareSuccessView(ViewWatchlistOutputData outputData);
    void prepareFailView(String errorMessage);
}