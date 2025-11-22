package movieapp.use_case.movielist;

/**
 * Output Boundary for fetching movies.
 * Application Business Rules layer.
 */
public interface FetchMoviesOutputBoundary {

    void presentMovies(FetchMoviesOutputData outputData);

    void presentError(String errorMessage);

    void presentLoading(boolean isLoading);
}
