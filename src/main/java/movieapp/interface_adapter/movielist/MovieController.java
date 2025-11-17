package movieapp.interface_adapter.movielist;

import movieapp.use_case.movielist.FetchMoviesInputBoundary;
import movieapp.use_case.movielist.FetchMoviesInputData;

/**
 * Controller for Use Case 5 - Fetching Movies
 * Interface Adapters Layer
 *
 * Handles user actions and converts them to use case requests
 */
public class MovieController {
    private final FetchMoviesInputBoundary fetchMoviesInteractor;

    /**
     * Create controller with use case interactor
     * @param fetchMoviesInteractor the use case that handles movie fetching
     */
    public MovieController(FetchMoviesInputBoundary fetchMoviesInteractor) {
        this.fetchMoviesInteractor = fetchMoviesInteractor;
    }

    /**
     * Handle user request to view popular movies
     * Called when user clicks "Popular Movies" tab or button
     */
    public void onPopularMoviesRequested() {
        FetchMoviesInputData inputData = new FetchMoviesInputData(100, "popular");
        fetchMoviesInteractor.fetchPopularMovies(inputData);
    }

    /**
     * Handle user request to view recently released movies
     * Called when user clicks "Recent Movies" tab or button
     */
    public void onRecentMoviesRequested() {
        FetchMoviesInputData inputData = new FetchMoviesInputData(100, "recent");
        fetchMoviesInteractor.fetchRecentMovies(inputData);
    }

    /**
     * Handle user request to refresh movies
     * Can be used for both popular and recent movies
     */
    public void onRefreshRequested(String movieType) {
        FetchMoviesInputData inputData = new FetchMoviesInputData(100, movieType);

        if ("popular".equals(movieType)) {
            fetchMoviesInteractor.fetchPopularMovies(inputData);
        } else if ("recent".equals(movieType)) {
            fetchMoviesInteractor.fetchRecentMovies(inputData);
        }
    }
}