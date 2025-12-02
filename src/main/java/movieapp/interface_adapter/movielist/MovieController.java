package movieapp.interface_adapter.movielist;

import movieapp.use_case.movielist.FetchMoviesInputBoundary;
import movieapp.use_case.movielist.FetchMoviesInputData;
import movieapp.use_case.movielist.MovieCategory;

/**
 * Controller for fetching movies.
 * Interface Adapters layer.
 */
public class MovieController {

    private final FetchMoviesInputBoundary fetchMoviesInteractor;
    private static final int DEFAULT_TARGET_COUNT = 100;

    public MovieController(FetchMoviesInputBoundary fetchMoviesInteractor) {
        this.fetchMoviesInteractor = fetchMoviesInteractor;
    }

    /**
     * Triggered when the UI requests the 100 most popular movies.
     * Creates the input data and forwards it to the interactor.
     */
    public void onPopularMoviesRequested() {
        FetchMoviesInputData inputData =
                new FetchMoviesInputData(DEFAULT_TARGET_COUNT, MovieCategory.POPULAR);
        fetchMoviesInteractor.fetchMovies(inputData);
    }

    /**
     * Triggered when the UI requests the 100 most recently released movies.
     * Builds the appropriate input data and delegates to the interactor.
     */
    public void onRecentMoviesRequested() {
        FetchMoviesInputData inputData =
                new FetchMoviesInputData(DEFAULT_TARGET_COUNT, MovieCategory.RECENT);
        fetchMoviesInteractor.fetchMovies(inputData);
    }

    /**
     * Generic refresh operation used when the view wants to reload
     * any category of movie list. Keeps UI logic thin by always delegating
     * to the use-case interactor with the correct input data.
     */
    public void onRefreshRequested(MovieCategory category) {
        FetchMoviesInputData inputData =
                new FetchMoviesInputData(DEFAULT_TARGET_COUNT, category);
        fetchMoviesInteractor.fetchMovies(inputData);
    }
}