package movieapp.interface_adapter.movielist;

import movieapp.entity.Movie;
import movieapp.use_case.movielist.FetchMoviesOutputBoundary;
import movieapp.use_case.movielist.FetchMoviesOutputData;
import movieapp.use_case.movielist.SortMoviesOutputBoundary;
import movieapp.use_case.movielist.SortMoviesOutputData;
import movieapp.use_case.movielist.SortType;
import movieapp.view.MovieListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter for movie list use cases.
 * Interface Adapters layer.
 */
public class MoviePresenter implements FetchMoviesOutputBoundary, SortMoviesOutputBoundary {

    private MovieListView view;

    public void setView(MovieListView view) {
        this.view = view;
    }

    @Override
    public void presentMovies(FetchMoviesOutputData outputData) {
        if (view == null) return;

        List<MovieViewModel> viewModels = new ArrayList<>();
        for (Movie movie : outputData.getMovies()) {
            viewModels.add(createViewModel(movie));
        }

        view.displayMovies(viewModels);

        if (outputData.getTotalCount() < 100) {
            String message = String.format(
                    "Showing %d of 100 movies due to limited results.",
                    outputData.getTotalCount()
            );
            view.showNotification(message);
        }

        if (outputData.getSkippedCount() > 0) {
            String message = String.format(
                    "%d items were skipped due to missing data.",
                    outputData.getSkippedCount()
            );
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

    @Override
    public void presentSortedMovies(SortMoviesOutputData outputData) {
        if (view == null) return;

        List<MovieViewModel> viewModels = new ArrayList<>();
        for (Movie movie : outputData.getSortedMovies()) {
            viewModels.add(createViewModel(movie));
        }

        view.displayMovies(viewModels);

        String sortDescription = mapSortTypeToDescription(outputData.getSortType());
        view.updateSortIndicator(sortDescription);
    }

    private String mapSortTypeToDescription(SortType sortType) {
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

    private MovieViewModel createViewModel(Movie movie) {
        String displayTitle = movie.getTitle();
        if (displayTitle != null && displayTitle.length() > 30) {
            displayTitle = displayTitle.substring(0, 27) + "...";
        }

        String displayDate = (movie.getReleaseDate() != null && !movie.getReleaseDate().isEmpty())
                ? "Released: " + movie.getReleaseDate()
                : "Released: —";

        String displayRating = movie.getVoteAverage() > 0
                ? String.format("%.1f", movie.getVoteAverage())
                : "—";

        String displayVoteCount = String.format("%.0f votes", movie.getPopularity());

        String posterUrl = movie.getPosterUrl();

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