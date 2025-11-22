package movieapp.interface_adapter.movielist;

/**
 * View Model for displaying a movie.
 * Interface Adapters layer.
 */
public class MovieViewModel {

    private final String displayTitle;
    private final String displayReleaseDate;
    private final String displayRating;
    private final String displayVoteCount;
    private final String posterUrl;
    private final String displayOverview;

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