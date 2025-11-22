package movieapp.interface_adapter.movielist;

import movieapp.entity.Movie;
import movieapp.use_case.movielist.SortMoviesOutputBoundary;
import movieapp.use_case.movielist.SortMoviesOutputData;
import movieapp.use_case.movielist.SortType;
import movieapp.view.MovieListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter for the movie sorting use case.
 * Interface Adapters layer.
 */
public class SortPresenter implements SortMoviesOutputBoundary {

    private MovieListView view;

    /**
     * Create a SortPresenter with no view attached yet.
     * Call {@link #setView(MovieListView)} before using.
     */
    public SortPresenter() {
    }

    /**
     * Create a SortPresenter with an attached view.
     *
     * @param view the view to be updated with sorted results
     */
    public SortPresenter(MovieListView view) {
        this.view = view;
    }

    /**
     * Attach or replace the view that will display sorted movies.
     *
     * @param view a MovieListView implementation (e.g., MovieListSwingView)
     */
    public void setView(MovieListView view) {
        this.view = view;
    }

    @Override
    public void presentSortedMovies(SortMoviesOutputData outputData) {
        if (view == null || outputData == null) {
            return;
        }

        // Convert entities to view models
        List<MovieViewModel> movieViewModels = new ArrayList<>();
        for (Movie movie : outputData.getSortedMovies()) {
            movieViewModels.add(createMovieViewModel(movie));
        }

        // Map SortType -> readable description
        String sortDescription = mapSortTypeToDescription(outputData.getSortType());

        // Build SortViewModel
        SortViewModel sortViewModel = new SortViewModel(
                outputData.getListName(),
                sortDescription,
                movieViewModels
        );

        // Drive the view
        view.displayMovies(sortViewModel.getMovies());
        view.updateSortIndicator(sortViewModel.getSortDescription());
    }

    /**
     * Convert a Movie entity into a MovieViewModel.
     * All data is formatted as strings ready for display.
     */
    private MovieViewModel createMovieViewModel(Movie movie) {
        // Title (truncate if too long)
        String displayTitle = movie.getTitle();
        if (displayTitle != null && displayTitle.length() > 30) {
            displayTitle = displayTitle.substring(0, 27) + "...";
        }

        // Release date (handle missing)
        String displayDate = (movie.getReleaseDate() != null && !movie.getReleaseDate().isEmpty())
                ? "Released: " + movie.getReleaseDate()
                : "Released: —";

        // Rating using voteAverage (handle missing/zero)
        String displayRating = movie.getVoteAverage() > 0
                ? String.format("%.1f", movie.getVoteAverage())
                : "—";

        // Popularity as number-of-ratings proxy – treat 0 as missing
        double popularity = movie.getPopularity();
        String displayVoteCount = popularity > 0
                ? String.format("%.0f votes", popularity)
                : "—";

        String posterUrl = movie.getPosterUrl();

        // Overview (truncate long text)
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

    /**
     * Map sort type to human-readable description for the UI.
     */
    private String mapSortTypeToDescription(SortType sortType) {
        if (sortType == null) {
            return "Default";
        }

        return switch (sortType) {
            case ALPHABETICAL_AZ -> "Alphabetical (A→Z)";
            case ALPHABETICAL_ZA -> "Alphabetical (Z→A)";
            case RATING_DESC -> "Average Rating (High→Low)";
            case RATING_ASC -> "Average Rating (Low→High)";
            case POPULARITY_DESC -> "Number of Ratings (High→Low)";
            case POPULARITY_ASC -> "Number of Ratings (Low→High)";
            case NONE -> "Default";
        };
    }
}
