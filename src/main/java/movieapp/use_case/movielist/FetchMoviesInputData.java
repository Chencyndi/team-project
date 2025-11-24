package movieapp.use_case.movielist;

/**
 * Input Data for fetching movies.
 * Application Business Rules layer.
 */
public class FetchMoviesInputData {

    private final int targetCount;
    private final MovieCategory category;

    public FetchMoviesInputData(int targetCount, MovieCategory category) {
        if (targetCount <= 0) {
            throw new IllegalArgumentException("targetCount must be positive");
        }
        if (category == null) {
            throw new IllegalArgumentException("category cannot be null");
        }
        this.targetCount = targetCount;
        this.category = category;
    }

    public int getTargetCount() {
        return targetCount;
    }

    public MovieCategory getCategory() {
        return category;
    }
}