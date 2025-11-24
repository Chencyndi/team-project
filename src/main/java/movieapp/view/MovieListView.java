package movieapp.view;

import movieapp.interface_adapter.movielist.MovieViewModel;
import java.util.List;

/**
 * View Interface for the movie list screen.
 * Frameworks & Drivers layer.
 */
public interface MovieListView {

    void displayMovies(List<MovieViewModel> movies);

    void showError(String message);

    void showLoading(boolean isLoading);

    void showNotification(String message);

    void updateSortIndicator(String sortDescription);
}