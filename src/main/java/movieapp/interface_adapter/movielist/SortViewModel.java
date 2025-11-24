package movieapp.interface_adapter.movielist;

import java.util.Collections;
import java.util.List;

/**
 * View model representing a sorted movie list.
 * Interface Adapters layer.
 */
public class SortViewModel {

    private final String listName;
    private final String sortDescription;
    private final List<MovieViewModel> movies;

    /**
     * Create a view model for a sorted movie list.
     *
     * @param listName        the logical name of the movie list (e.g., "100 Most Popular Movies")
     * @param sortDescription human-readable sort description (e.g., "Alphabetical (A→Z)")
     * @param movies          sorted movies as UI-ready view models
     */
    public SortViewModel(String listName,
                         String sortDescription,
                         List<MovieViewModel> movies) {
        this.listName = listName == null ? "" : listName;
        this.sortDescription = sortDescription == null ? "" : sortDescription;
        this.movies = movies == null ? List.of() : List.copyOf(movies);
    }

    /**
     * Returns the logical name of this movie list.
     */
    public String getListName() {
        return listName;
    }

    /**
     * Returns the human-readable sort description
     * (e.g., "Average Rating (High→Low)").
     */
    public String getSortDescription() {
        return sortDescription;
    }

    /**
     * Returns an unmodifiable list of sorted movies ready for display.
     */
    public List<MovieViewModel> getMovies() {
        return Collections.unmodifiableList(movies);
    }
}
