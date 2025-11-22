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

    public void onPopularMoviesRequested() {
        FetchMoviesInputData inputData =
                new FetchMoviesInputData(DEFAULT_TARGET_COUNT, MovieCategory.POPULAR);
        fetchMoviesInteractor.fetchMovies(inputData);
    }

    public void onRecentMoviesRequested() {
        FetchMoviesInputData inputData =
                new FetchMoviesInputData(DEFAULT_TARGET_COUNT, MovieCategory.RECENT);
        fetchMoviesInteractor.fetchMovies(inputData);
    }

    /**
     * Generic refresh handler.
     */
    public void onRefreshRequested(MovieCategory category) {
        FetchMoviesInputData inputData =
                new FetchMoviesInputData(DEFAULT_TARGET_COUNT, category);
        fetchMoviesInteractor.fetchMovies(inputData);
    }
}