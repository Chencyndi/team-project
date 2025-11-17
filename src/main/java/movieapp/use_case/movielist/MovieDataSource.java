package movieapp.use_case.movielist;

import movieapp.entity.Movie;
import java.util.List;

/**
 * Data Access Interface for Use Case 5
 * Defines how to access movie data from external sources
 * Application Business Rules Layer
 *
 * This interface is implemented in the Frameworks & Drivers layer
 */
public interface MovieDataSource {
    /**
     * Fetch popular movies from data source
     * @param count number of movies to fetch
     * @return list of Movie entities
     * @throws Exception if data fetching fails
     */
    List<Movie> fetchPopularMovies(int count) throws Exception; //TODO

    /**
     * Fetch recently released movies from data source
     * @param count number of movies to fetch
     * @return list of Movie entities
     * @throws Exception if data fetching fails
     */
    List<Movie> fetchRecentMovies(int count) throws Exception; //TODO
}