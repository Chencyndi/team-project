package movieapp.use_case.movielist;

/**
 * Output Boundary for Use Case 5
 * Defines how results are presented
 * Application Business Rules Layer
 */
public interface FetchMoviesOutputBoundary {
    /**
     * Present successfully fetched movies
     * @param outputData contains the fetched movies and metadata
     */
    void presentMovies(FetchMoviesOutputData outputData);

    /**
     * Present an error that occurred during fetching
     * @param errorMessage description of the error
     */
    void presentError(String errorMessage);

    /**
     * Present loading state
     * @param isLoading whether data is currently being loaded
     */
    void presentLoading(boolean isLoading);
}