package movieapp.use_case.watchlist;

import movieapp.entity.Watchlist;

/**
 * Interactor for the View Watchlist use case.
 * Handles the business logic for retrieving and displaying a user's watchlist.
 */
public class ViewWatchlistInteractor implements ViewWatchlistInputBoundary {

    private final WatchlistDataAccessInterface watchlistDataAccess;
    private final ViewWatchlistOutputBoundary presenter;

    public ViewWatchlistInteractor(
            WatchlistDataAccessInterface watchlistDataAccess,
            ViewWatchlistOutputBoundary presenter) {
        this.watchlistDataAccess = watchlistDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(ViewWatchlistInputData inputData) {
        try {
            final Watchlist watchlist = watchlistDataAccess.getWatchlist(inputData.getUsername());
            final ViewWatchlistOutputData outputData = new ViewWatchlistOutputData(
                    watchlist.getList(), // Using getList() to match your entity
                    true
            );
            presenter.prepareSuccessView(outputData);
        } catch (Exception e) {
            presenter.prepareFailView("Failed to load watchlist: " + e.getMessage());
        }
    }
}