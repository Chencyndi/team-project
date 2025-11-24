package movieapp.use_case.movielist;

import movieapp.entity.Movie;

import java.util.Collections;
import java.util.List;

/**
 * Output Data for sorted movies.
 */
public class SortMoviesOutputData {

    private final String listName;
    private final SortType sortType;
    private final List<Movie> sortedMovies;

    public SortMoviesOutputData(String listName, SortType sortType, List<Movie> sortedMovies) {
        this.listName = listName == null ? "" : listName;
        this.sortType = sortType;
        this.sortedMovies = sortedMovies == null ? List.of() : List.copyOf(sortedMovies);
    }

    public String getListName() {
        return listName;
    }

    public SortType getSortType() {
        return sortType;
    }

    public List<Movie> getSortedMovies() {
        return Collections.unmodifiableList(sortedMovies); //TODO should this be changed?
    }
}
