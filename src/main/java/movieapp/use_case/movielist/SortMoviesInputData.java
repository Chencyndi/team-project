package movieapp.use_case.movielist;

import movieapp.entity.Movie;

import java.util.List;

/**
 * Input Data for sorting movies.
 */
public class SortMoviesInputData {

    private final String listName;
    private final SortType sortType;
    private final List<Movie> movies;

    public SortMoviesInputData(String listName, SortType sortType, List<Movie> movies) {
        if (sortType == null) {
            throw new IllegalArgumentException("sortType cannot be null");
        }
        if (movies == null) {
            throw new IllegalArgumentException("movies cannot be null");
        }
        this.listName = listName == null ? "" : listName;
        this.sortType = sortType;
        this.movies = List.copyOf(movies);
    }

    public String getListName() {
        return listName;
    }

    public SortType getSortType() {
        return sortType;
    }

    public List<Movie> getMovies() {
        return movies;
    }
}
