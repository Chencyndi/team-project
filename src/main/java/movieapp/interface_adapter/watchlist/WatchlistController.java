package movieapp.interface_adapter.watchlist;

import movieapp.entity.Movie;
import movieapp.use_case.watchlist.*;

public class WatchlistController {

    private final AddToWatchlistInputBoundary addToWatchlistInteractor;
    private final RemoveFromWatchlistInputBoundary removeFromWatchlistInteractor;
    private final ViewWatchlistInputBoundary viewWatchlistInteractor;

    public WatchlistController(
            AddToWatchlistInputBoundary addToWatchlistInteractor,
            RemoveFromWatchlistInputBoundary removeFromWatchlistInteractor,
            ViewWatchlistInputBoundary viewWatchlistInteractor) {
        this.addToWatchlistInteractor = addToWatchlistInteractor;
        this.removeFromWatchlistInteractor = removeFromWatchlistInteractor;
        this.viewWatchlistInteractor = viewWatchlistInteractor;
    }

    public void addToWatchlist(String username, Movie movie) {
        final AddToWatchlistInputData inputData = new AddToWatchlistInputData(username, movie);
        addToWatchlistInteractor.execute(inputData);
    }

    public void removeFromWatchlist(String username, int movieId, String movieTitle) {
        final RemoveFromWatchlistInputData inputData =
                new RemoveFromWatchlistInputData(username, movieId, movieTitle);
        removeFromWatchlistInteractor.execute(inputData);
    }

    public void viewWatchlist(String username) {
        final ViewWatchlistInputData inputData = new ViewWatchlistInputData(username);
        viewWatchlistInteractor.execute(inputData);
    }
}