package movieapp.use_case.watchlist;

import movieapp.entity.Movie;

public class AddToWatchlistInteractor implements AddToWatchlistInputBoundary {

    private final WatchlistDataAccessInterface watchlistDataAccess;
    private final AddToWatchlistOutputBoundary presenter;

    public AddToWatchlistInteractor(
            WatchlistDataAccessInterface watchlistDataAccess,
            AddToWatchlistOutputBoundary presenter) {
        this.watchlistDataAccess = watchlistDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(AddToWatchlistInputData inputData) {
        final String username = inputData.getUsername();
        final Movie movie = inputData.getMovie();

        // Check if movie is already in watchlist
        if (watchlistDataAccess.isInWatchlist(username, movie.getId())) {
            presenter.prepareFailView("Movie is already in your watchlist.");
            return;
        }

        // Try to add movie to watchlist
        final boolean success = watchlistDataAccess.addMovieToWatchlist(username, movie);

        if (success) {
            final AddToWatchlistOutputData outputData = new AddToWatchlistOutputData(
                    movie.getTitle(),
                    true,
                    "Successfully added to watchlist"
            );
            presenter.prepareSuccessView(outputData);
        } else {
            presenter.prepareFailView("Failed to add movie to watchlist. Please try again.");
        }
    }
}
package movieapp.use_case.watchlist;

import movieapp.entity.Movie;

public class AddToWatchlistInteractor implements AddToWatchlistInputBoundary {

    private final WatchlistDataAccessInterface watchlistDataAccess;
    private final AddToWatchlistOutputBoundary presenter;

    public AddToWatchlistInteractor(
            WatchlistDataAccessInterface watchlistDataAccess,
            AddToWatchlistOutputBoundary presenter) {
        this.watchlistDataAccess = watchlistDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(AddToWatchlistInputData inputData) {
        final String username = inputData.getUsername();
        final Movie movie = inputData.getMovie();

        // Check if movie is already in watchlist
        if (watchlistDataAccess.isInWatchlist(username, movie.getId())) {
            presenter.prepareFailView("Movie is already in your watchlist.");
            return;
        }

        // Try to add movie to watchlist
        final boolean success = watchlistDataAccess.addMovieToWatchlist(username, movie);

        if (success) {
            final AddToWatchlistOutputData outputData = new AddToWatchlistOutputData(
                    movie.getTitle(),
                    true,
                    "Successfully added to watchlist"
            );
            presenter.prepareSuccessView(outputData);
        } else {
            presenter.prepareFailView("Failed to add movie to watchlist. Please try again.");
        }
    }
}