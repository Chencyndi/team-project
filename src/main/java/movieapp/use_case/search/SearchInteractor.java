package movieapp.use_case.search;
// SearchMoviesInteractor.java

import movieapp.interface_adapter.search.MovieRepository;
import movieapp.entity.Movie;

import java.util.List;

public class SearchInteractor implements SearchInputBoundary {
    private final MovieRepository movieRepository;
    private final SearchOutputBoundary outputBoundary;

    public SearchInteractor(MovieRepository movieRepository, SearchOutputBoundary outputBoundary) {
        this.movieRepository = movieRepository;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(SearchInputData inputData) {
        try {
            // Validate input
            if (inputData.getSearchQuery().isEmpty()) {
                outputBoundary.prepareFailView("Please enter a search term");
                return;
            }

            // Search movies
            List<Movie> movies = movieRepository.searchMovies(inputData.getSearchQuery());

            // Get top 3 movies
            List<Movie> topMovies = movies.subList(0, Math.min(3, movies.size()));

            SearchOutputData outputData = new SearchOutputData(true,
                    "Found " + topMovies.size() + " movies", topMovies);
            outputBoundary.prepareSuccessView(outputData, inputData.getSearchQuery());

        } catch (IllegalArgumentException e) {
            outputBoundary.prepareFailView("Error: " + e.getMessage());
        }
    }
}