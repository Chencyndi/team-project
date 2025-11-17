package movieapp.use_case.movielist;

import movieapp.entity.Movie;
import java.util.List;

/**
 * Output Data (DTO) for Use Case 5
 * Contains fetched movies and metadata
 * Application Business Rules Layer
 */
public class FetchMoviesOutputData {
    private final List<Movie> movies;
    private final int totalCount;
    private final int skippedCount;
    private final boolean success;
    private final String listName;

    /**
     * Create output data for successful movie fetch
     */
    public FetchMoviesOutputData(List<Movie> movies, int totalCount, int skippedCount, String listName) {
        this.movies = movies;
        this.totalCount = totalCount;
        this.skippedCount = skippedCount;
        this.success = true;
        this.listName = listName;
    }

    /**
     * Create output data for failed movie fetch
     */
    public FetchMoviesOutputData() {
        this.movies = null;
        this.totalCount = 0;
        this.skippedCount = 0;
        this.success = false;
        this.listName = "";
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getSkippedCount() {
        return skippedCount;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getListName() {
        return listName;
    }
}