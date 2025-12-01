package usecase5test;

import movieapp.data_access.TMDBMovieAPIAccess;
import movieapp.entity.Movie;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FetchRecentMoviesUseCase5Test {

    @Test
    void fetchRecentMovies_returnsAtMostRequestedCountAndNotEmpty() throws Exception {
        TMDBMovieAPIAccess api = new TMDBMovieAPIAccess();

        int requested = 100;
        List<Movie> movies = api.fetchRecentMovies(requested);

        assertNotNull(movies, "Recent movies list should not be null");
        assertFalse(movies.isEmpty(), "Recent movies list should not be empty");
        assertTrue(movies.size() <= requested,
                "Recent movies list size should be <= " + requested);
    }

    @Test
    void fetchRecentMovies_sortedByReleaseDateDescending() throws Exception {
        TMDBMovieAPIAccess api = new TMDBMovieAPIAccess();

        List<Movie> movies = api.fetchRecentMovies(50);

        // Ensure at least 2 to meaningfully check ordering
        assertTrue(movies.size() >= 2, "Need at least 2 movies to test ordering");

        for (int i = 0; i < movies.size() - 1; i++) {
            String d1 = movies.get(i).getReleaseDate();
            String d2 = movies.get(i + 1).getReleaseDate();

            // release_date is ISO yyyy-MM-dd, so lexicographic compare works
            assertNotNull(d1, "Release date should not be null");
            assertNotNull(d2, "Release date should not be null");
            assertFalse(d1.isBlank(), "Release date should not be blank");
            assertFalse(d2.isBlank(), "Release date should not be blank");

            assertTrue(
                    d1.compareTo(d2) >= 0,
                    "Movies should be sorted by release date descending; found " + d1 + " then " + d2
            );
        }
    }
}