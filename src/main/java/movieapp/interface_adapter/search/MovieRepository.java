// MovieRepository.java
package movieapp.interface_adapter.search;

import movieapp.entity.Movie;
import java.util.List;

public interface MovieRepository {
    List<Movie> searchMovies(String query);
    void addMovie(Movie movie);
}