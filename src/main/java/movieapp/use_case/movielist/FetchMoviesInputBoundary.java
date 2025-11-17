package movieapp.use_case.movielist;

/**
 * Defines the contract for fetching movies
 * Application Business Rules Layer
 */
public interface FetchMoviesInputBoundary {
    /**
     * Fetch the 100 most popular movies
     * @param inputData contains request parameters
     */
    void fetchPopularMovies(FetchMoviesInputData inputData);

    /**
     * Fetch the 100 most recently released movies
     * @param inputData contains request parameters
     */
    void fetchRecentMovies(FetchMoviesInputData inputData);
}