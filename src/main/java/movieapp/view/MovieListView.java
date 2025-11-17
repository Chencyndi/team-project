package movieapp.view;

import movieapp.interface_adapter.movielist.MovieViewModel;

import java.util.List;

/**
 * View Interface for Use Case 5
 * Frameworks & Drivers Layer
 *
 * Defines what the UI must be able to do
 * Can be implemented with Swing, JavaFX, or any other UI framework
 */
public interface MovieListView {
    /**
     * Display a list of movies in the UI
     * @param movies list of view models ready for display
     */
    void displayMovies(List<MovieViewModel> movies);

    /**
     * Show an error message to the user
     * @param message error message to display
     */
    void showError(String message);

    /**
     * Show loading state
     * @param isLoading true to show loading indicator, false to hide it
     */
    void showLoading(boolean isLoading);

    /**
     * Show a notification message
     * @param message notification message (e.g., "10 items skipped")
     */
    void showNotification(String message);

    /**
     * Update the sort indicator in the UI
     * @param sortDescription description of current sort (e.g., "Alphabetical A→Z")
     */
    void updateSortIndicator(String sortDescription);
}