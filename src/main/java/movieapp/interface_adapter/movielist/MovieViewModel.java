package movieapp.interface_adapter.movielist;

/**
 * View Model for displaying a movie
 * Interface Adapters Layer
 *
 * Contains UI-ready formatted data (all strings)
 * No business logic, just data for display
 */
public class MovieViewModel {
    private final String displayTitle;
    private final String displayReleaseDate;
    private final String displayRating;
    private final String displayVoteCount;
    private final String posterUrl;
    private final String displayOverview;

    /**
     * Create a view model with formatted display data
     * All fields are ready for direct display in UI
     */
    public MovieViewModel(String displayTitle,
                          String displayReleaseDate,
                          String displayRating,
                          String displayVoteCount,
                          String posterUrl,
                          String displayOverview) {
        this.displayTitle = displayTitle;
        this.displayReleaseDate = displayReleaseDate;
        this.displayRating = displayRating;
        this.displayVoteCount = displayVoteCount;
        this.posterUrl = posterUrl;
        this.displayOverview = displayOverview;
    }

    // Getters only - view models are immutable

    public String getDisplayTitle() {
        return displayTitle;
    }

    public String getDisplayReleaseDate() {
        return displayReleaseDate;
    }

    public String getDisplayRating() {
        return displayRating;
    }

    public String getDisplayVoteCount() {
        return displayVoteCount;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getDisplayOverview() {
        return displayOverview;
    }
}