package movieapp.interface_adapter.movielist;

import movieapp.entity.Movie;
import movieapp.entity.MovieList;
import movieapp.use_case.movielist.FetchMoviesOutputBoundary;
import movieapp.use_case.movielist.FetchMoviesOutputData;
import movieapp.view.MovieListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter for Use Case 5 - Movie Display
 * Interface Adapters Layer
 *
 * Converts entities and output data to view models
 * Formats data for UI display
 */

public class MoviePresenter implements FetchMoviesOutputBoundary {
    private MovieListView view;

    /**
     * Set the view that will display the movies
     */
    public void setView(MovieListView view) {
        this.view = view;
    }

    @Override
    public void presentMovies(FetchMoviesOutputData outputData) {
        if (view == null) return;

        // Convert Movie entities to MovieViewModels
        List<MovieViewModel> viewModels = new ArrayList<>();

        for (Movie movie : outputData.getMovies()) {
            MovieViewModel viewModel = createViewModel(movie);
            viewModels.add(viewModel);
        }

        // Display movies in view
        view.displayMovies(viewModels);

        // Show notification if fewer than 100 movies
        if (outputData.getTotalCount() < 100) {
            String message = String.format("Showing %d of 100 movies due to limited results.",
                    outputData.getTotalCount());
            view.showNotification(message);
        }

        // Show notification if movies were skipped
        if (outputData.getSkippedCount() > 0) {
            String message = String.format("%d items were skipped due to missing data.",
                    outputData.getSkippedCount());
            view.showNotification(message);
        }
    }

    @Override
    public void presentError(String errorMessage) {
        if (view == null) return;
        view.showError(errorMessage);
    }

    @Override
    public void presentLoading(boolean isLoading) {
        if (view == null) return;
        view.showLoading(isLoading);
    }

    /**
     * Present sorted movies
     * Called by SortController after sorting
     */
    public void presentSortedMovies(MovieList movieList) {
        if (view == null) return;

        // Convert entities to view models
        List<MovieViewModel> viewModels = new ArrayList<>();

        for (Movie movie : movieList.getMovieList()) {
            MovieViewModel viewModel = createViewModel(movie);
            viewModels.add(viewModel);
        }

        // Update view with sorted movies
        view.displayMovies(viewModels);

        // Update sort indicator
        view.updateSortIndicator(movieList.getCurrentSort());
    }

    /**
     * Convert Movie entity to MovieViewModel
     * Formats all data as strings ready for display
     */
    private MovieViewModel createViewModel(Movie movie) {
        // Format title (truncate if too long for display)
        String displayTitle = movie.getTitle();
        if (displayTitle.length() > 30) {
            displayTitle = displayTitle.substring(0, 27) + "...";
        }

        // Format release date (handle missing dates)
        String displayDate = (movie.getReleaseDate() != null && !movie.getReleaseDate().isEmpty())
                ? "Released: " + movie.getReleaseDate()
                : "Released: —";

        // Format rating (handle missing ratings)
        String displayRating = movie.getVoteAverage() > 0
                ? String.format("%.1f", movie.getVoteAverage())
                : "—";

        // Format vote count
        String displayVoteCount = String.format("%d votes", movie.getVoteCount());

        // Poster URL (pass through, let view handle missing posters)
        String posterUrl = movie.getPosterUrl();

        // Format overview (truncate if too long)
        String displayOverview = movie.getOverview();
        if (displayOverview != null && displayOverview.length() > 200) {
            displayOverview = displayOverview.substring(0, 197) + "...";
        }

        return new MovieViewModel(
                displayTitle,
                displayDate,
                displayRating,
                displayVoteCount,
                posterUrl,
                displayOverview
        );
    }
}