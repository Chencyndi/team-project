package movieapp.use_case.movielist;

/**
 * Input Data (DTO) for Use Case 5
 * Contains request parameters for fetching movies
 * Application Business Rules Layer
 */
public class FetchMoviesInputData {
    private final int targetCount;
    private final String movieType; // "popular" or "recent"

    /**
     * Create input data for movie fetching
     * @param targetCount number of movies to fetch (typically 100)
     * @param movieType type of movies ("popular" or "recent")
     */
    public FetchMoviesInputData(int targetCount, String movieType) {
        this.targetCount = targetCount;
        this.movieType = movieType;
    }

    public int getTargetCount() {
        return targetCount;
    }

    public String getMovieType() {
        return movieType;
    }
}