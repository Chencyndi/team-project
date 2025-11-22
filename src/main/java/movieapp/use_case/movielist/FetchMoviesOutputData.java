package movieapp.use_case.movielist;

import movieapp.entity.Movie;

import java.util.Collections;
import java.util.List;

/**
 * Output Data for fetched movies.
 * Application Business Rules layer.
 */
public class FetchMoviesOutputData {

    private final List<Movie> movies;
    private final int totalCount;
    private final int skippedCount;
    private final String listName;

    public FetchMoviesOutputData(List<Movie> movies,
                                 int totalCount,
                                 int skippedCount,
                                 String listName) {
        this.movies = movies == null ? List.of() : List.copyOf(movies);
        this.totalCount = totalCount;
        this.skippedCount = skippedCount;
        this.listName = listName == null ? "" : listName;
    }

    public List<Movie> getMovies() {
        return Collections.unmodifiableList(movies); //TODO should this be changed?
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getSkippedCount() {
        return skippedCount;
    }

    public String getListName() {
        return listName;
    }
}