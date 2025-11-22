package movieapp.use_case.movielist;

import movieapp.entity.Movie;

import java.util.List;

/**
 * Use Case Interactor for fetching movies.
 * Application Business Rules layer.
 */
public class FetchMoviesInteractor implements FetchMoviesInputBoundary {

    private final MovieDataSource dataSource;
    private final FetchMoviesOutputBoundary outputBoundary;

    public FetchMoviesInteractor(MovieDataSource dataSource,
                                 FetchMoviesOutputBoundary outputBoundary) {
        this.dataSource = dataSource;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void fetchMovies(FetchMoviesInputData inputData) {
        outputBoundary.presentLoading(true);

        try {
            List<Movie> movies;

            switch (inputData.getCategory()) {
                case POPULAR -> movies = dataSource.fetchPopularMovies(inputData.getTargetCount());
                case RECENT -> movies = dataSource.fetchRecentMovies(inputData.getTargetCount());
                default -> throw new IllegalStateException("Unsupported category: " + inputData.getCategory());
            }

            int skippedCount = Math.max(0, inputData.getTargetCount() - movies.size());

            String listName = switch (inputData.getCategory()) {
                case POPULAR -> "100 Most Popular Movies";
                case RECENT -> "100 Recently Released Movies";
            };

            FetchMoviesOutputData outputData = new FetchMoviesOutputData(
                    movies,
                    movies.size(),
                    skippedCount,
                    listName
            );

            outputBoundary.presentLoading(false);
            outputBoundary.presentMovies(outputData);

        } catch (Exception e) {
            outputBoundary.presentLoading(false);
            outputBoundary.presentError("Failed to fetch movies: " + e.getMessage());
        }
    }
}