package movieapp.use_case.watchlist;

/**
 * Interactor for the Remove from Watchlist use case.
 * Contains the business logic for removing a movie from the watchlist.
 */
public class RemoveFromWatchlistInteractor implements RemoveFromWatchlistInputBoundary {

    private final WatchlistDataAccessInterface watchlistDataAccess;
    private final RemoveFromWatchlistOutputBoundary presenter;

    public RemoveFromWatchlistInteractor(
            WatchlistDataAccessInterface watchlistDataAccess,
            RemoveFromWatchlistOutputBoundary presenter) {
        this.watchlistDataAccess = watchlistDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(RemoveFromWatchlistInputData inputData) {
        final String username = inputData.getUsername();
        final int movieId = inputData.getMovieId();
        final String movieTitle = inputData.getMovieTitle();

        // Check if movie is in watchlist
        if (!watchlistDataAccess.isInWatchlist(username, movieId)) {
            presenter.prepareFailView("Movie is not in your watchlist.");
            return;
        }

        // Try to remove movie from watchlist
        final boolean success = watchlistDataAccess.removeMovieFromWatchlist(username, movieId);

        if (success) {
            final RemoveFromWatchlistOutputData outputData = new RemoveFromWatchlistOutputData(
                    movieTitle,
                    true,
                    "Successfully removed from watchlist"
            );
            presenter.prepareSuccessView(outputData);
        } else {
            presenter.prepareFailView("Failed to remove movie from watchlist. Please try again.");
        }
    }
}