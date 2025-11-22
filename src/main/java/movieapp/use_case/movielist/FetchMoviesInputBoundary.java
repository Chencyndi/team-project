package movieapp.use_case.movielist;

/**
 * Input Boundary for fetching movies.
 * Application Business Rules layer.
 */
public interface FetchMoviesInputBoundary {

    /**
     * Fetch movies according to the requested category and count.
     */
    void fetchMovies(FetchMoviesInputData inputData);
}