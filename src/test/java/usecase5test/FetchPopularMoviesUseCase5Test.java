package usecase5test;

import movieapp.data_access.TMDBMovieAPIAccess;
import movieapp.entity.Movie;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FetchPopularMoviesUseCase5Test {

    @Test
    void fetchPopularMovies_returnsAtMostRequestedCountAndNotEmpty() throws Exception {
        TMDBMovieAPIAccess api = new TMDBMovieAPIAccess();

        int requested = 100;
        List<Movie> movies = api.fetchPopularMovies(requested);

        assertNotNull(movies, "Popular movies list should not be null");
        assertFalse(movies.isEmpty(), "Popular movies list should not be empty");
        assertTrue(movies.size() <= requested,
                "Popular movies list size should be <= " + requested);
    }

    @Test
    void fetchPopularMovies_everyMovieHasEssentialFields() throws Exception {
        TMDBMovieAPIAccess api = new TMDBMovieAPIAccess();

        List<Movie> movies = api.fetchPopularMovies(50);

        for (Movie m : movies) {
            assertTrue(m.getId() > 0, "Movie id must be positive");
            assertNotNull(m.getTitle(), "Movie title must not be null");
            assertFalse(m.getTitle().isBlank(), "Movie title must not be blank");
        }
    }
}
