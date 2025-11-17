package movieapp.use_case.movielist;

import movieapp.entity.Movie;

import java.util.List;

/**
 * Use Case Interactor for Use Case 5
 * Contains the business logic for fetching and processing movies
 * Application Business Rules Layer
 */
public class FetchMoviesInteractor implements FetchMoviesInputBoundary {
    private final MovieDataSource dataSource;
    private final FetchMoviesOutputBoundary outputBoundary;

    /**
     * Create interactor with dependencies
     * @param dataSource for accessing movie data
     * @param outputBoundary for presenting results
     */
    public FetchMoviesInteractor(MovieDataSource dataSource, FetchMoviesOutputBoundary outputBoundary) {
        this.dataSource = dataSource;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void fetchPopularMovies(FetchMoviesInputData inputData) {
        outputBoundary.presentLoading(true);

        try {
            // Business logic: fetch movies from data source
            List<Movie> movies = dataSource.fetchPopularMovies(inputData.getTargetCount());

            // Business logic: count skipped movies (those with missing data)
            int skippedCount = inputData.getTargetCount() - movies.size();

            // Create output data
            FetchMoviesOutputData outputData = new FetchMoviesOutputData(
                    movies,
                    movies.size(),
                    skippedCount,
                    "100 Most Popular Movies"
            );

            outputBoundary.presentLoading(false);
            outputBoundary.presentMovies(outputData);

        } catch (Exception e) {
            outputBoundary.presentLoading(false);
            outputBoundary.presentError("Failed to fetch popular movies: " + e.getMessage());
        }
    }

    @Override
    public void fetchRecentMovies(FetchMoviesInputData inputData) {
        outputBoundary.presentLoading(true);

        try {
            // Business logic: fetch movies from data source
            List<Movie> movies = dataSource.fetchRecentMovies(inputData.getTargetCount());

            // Business logic: count skipped movies
            int skippedCount = inputData.getTargetCount() - movies.size();

            // Create output data
            FetchMoviesOutputData outputData = new FetchMoviesOutputData(
                    movies,
                    movies.size(),
                    skippedCount,
                    "100 Recently Released Movies"
            );

            outputBoundary.presentLoading(false);
            outputBoundary.presentMovies(outputData);

        } catch (Exception e) {
            outputBoundary.presentLoading(false);
            outputBoundary.presentError("Failed to fetch recent movies: " + e.getMessage());
        }
    }
}