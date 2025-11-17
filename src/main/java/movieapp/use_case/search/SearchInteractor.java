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
    public SearchOutputData execute(SearchInputData inputData) {
        try {
            // Validate input
            if (inputData.getSearchQuery() == null || inputData.getSearchQuery().trim().isEmpty()) {
                return outputBoundary.prepareFailView("Please enter a search term");
            }

            // Search movies
            List<Movie> movies = movieRepository.searchMovies(inputData.getSearchQuery().trim());

            // Get top 3 movies
            List<Movie> topMovies = movies.subList(0, Math.min(3, movies.size()));

            return outputBoundary.prepareSuccessView("Found " + topMovies.size() + " movies", topMovies);

        } catch (Exception e) {
            return outputBoundary.prepareFailView("Error: " + e.getMessage());
        }
    }
}