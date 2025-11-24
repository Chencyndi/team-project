package movieapp.use_case.movielist;

import movieapp.entity.Movie;
import java.util.List;

/**
 * Data Access Interface for movies.
 * Application Business Rules layer.
 */
public interface MovieDataSource {
    List<Movie> fetchPopularMovies(int count) throws Exception;
    List<Movie> fetchRecentMovies(int count) throws Exception;
    Movie findById(int id) throws Exception;
}
