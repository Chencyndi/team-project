package UseCase2Test;

import movieapp.entity.Movie;
import movieapp.entity.Watchlist;
import movieapp.use_case.watchlist.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ViewWatchlistInteractor.
 * Tests all paths through the business logic to achieve 100% code coverage.
 */
class ViewWatchlistInteractorTest {

    private TestWatchlistDAO dataAccess;
    private TestPresenter presenter;
    private ViewWatchlistInteractor interactor;

    @BeforeEach
    void setUp() {
        dataAccess = new TestWatchlistDAO();
        presenter = new TestPresenter();
        interactor = new ViewWatchlistInteractor(dataAccess, presenter);
    }

    @Test
    void testViewEmptyWatchlist() {
        ViewWatchlistInputData inputData = new ViewWatchlistInputData("user1");

        interactor.execute(inputData);

        assertTrue(presenter.successCalled, "Success should be called even for empty list");
        assertFalse(presenter.failCalled, "Fail should NOT be called");
        assertNotNull(presenter.movieList, "Movie list should not be null");
        assertTrue(presenter.movieList.isEmpty(), "Movie list should be empty");
    }

    @Test
    void testViewWatchlistWithOneMovie() {
        Movie movie = createTestMovie(1, "Inception");
        dataAccess.addMovieToWatchlist("user1", movie);

        ViewWatchlistInputData inputData = new ViewWatchlistInputData("user1");

        interactor.execute(inputData);

        assertTrue(presenter.successCalled);
        assertFalse(presenter.failCalled);
        assertNotNull(presenter.movieList);
        assertEquals(1, presenter.movieList.size());
        assertEquals("Inception", presenter.movieList.get(0).getTitle());
    }

    @Test
    void testViewWatchlistWithMultipleMovies() {
        Movie movie1 = createTestMovie(1, "Inception");
        Movie movie2 = createTestMovie(2, "The Matrix");
        Movie movie3 = createTestMovie(3, "Interstellar");
        dataAccess.addMovieToWatchlist("user1", movie1);
        dataAccess.addMovieToWatchlist("user1", movie2);
        dataAccess.addMovieToWatchlist("user1", movie3);

        ViewWatchlistInputData inputData = new ViewWatchlistInputData("user1");

        interactor.execute(inputData);

        assertTrue(presenter.successCalled);
        assertNotNull(presenter.movieList);
        assertEquals(3, presenter.movieList.size());
    }

    @Test
    void testViewWatchlistDataAccessFailure() {
        dataAccess.setShouldFail(true);
        ViewWatchlistInputData inputData = new ViewWatchlistInputData("user1");

        interactor.execute(inputData);

        assertTrue(presenter.failCalled, "Fail should be called");
        assertFalse(presenter.successCalled, "Success should NOT be called");
        assertTrue(presenter.errorMessage.contains("Failed") ||
                presenter.errorMessage.contains("load"));
    }

    @Test
    void testViewWatchlistForDifferentUsers() {
        Movie movie1 = createTestMovie(1, "Inception");
        Movie movie2 = createTestMovie(2, "The Matrix");
        dataAccess.addMovieToWatchlist("user1", movie1);
        dataAccess.addMovieToWatchlist("user2", movie2);

        interactor.execute(new ViewWatchlistInputData("user1"));

        assertTrue(presenter.successCalled);
        assertEquals(1, presenter.movieList.size());
        assertEquals("Inception", presenter.movieList.get(0).getTitle());

        presenter.reset();
        interactor.execute(new ViewWatchlistInputData("user2"));

        assertTrue(presenter.successCalled);
        assertEquals(1, presenter.movieList.size());
        assertEquals("The Matrix", presenter.movieList.get(0).getTitle());
    }

    @Test
    void testViewWatchlistAfterAddAndRemove() {
        Movie movie1 = createTestMovie(1, "Inception");
        Movie movie2 = createTestMovie(2, "The Matrix");
        Movie movie3 = createTestMovie(3, "Interstellar");

        dataAccess.addMovieToWatchlist("user1", movie1);
        dataAccess.addMovieToWatchlist("user1", movie2);
        dataAccess.addMovieToWatchlist("user1", movie3);

        dataAccess.removeMovieFromWatchlist("user1", 2);

        ViewWatchlistInputData inputData = new ViewWatchlistInputData("user1");

        interactor.execute(inputData);

        assertTrue(presenter.successCalled);
        assertEquals(2, presenter.movieList.size());

        boolean hasMatrix = presenter.movieList.stream()
                .anyMatch(m -> m.getTitle().equals("The Matrix"));
        assertFalse(hasMatrix, "Removed movie should not be in list");
    }

    @Test
    void testViewWatchlistReturnsCorrectMovieDetails() {
        Movie movie = createTestMovie(550, "Fight Club");
        dataAccess.addMovieToWatchlist("user1", movie);

        ViewWatchlistInputData inputData = new ViewWatchlistInputData("user1");

        interactor.execute(inputData);

        assertTrue(presenter.successCalled);
        assertEquals(1, presenter.movieList.size());

        Movie retrievedMovie = presenter.movieList.get(0);
        assertEquals(550, retrievedMovie.getId());
        assertEquals("Fight Club", retrievedMovie.getTitle());
        assertEquals(8.5, retrievedMovie.getVoteAverage());
    }

    // ================== HELPER METHODS ==================

    /**
     * Creates a test movie with the given ID and title.
     */
    private Movie createTestMovie(int id, String title) {
        return new Movie(
                id,
                title,
                "Test overview for " + title,
                "2024-01-01",
                "/test-poster.jpg",
                8.5,
                100.0,
                1000,
                new ArrayList<>()
        );
    }

    // ================== TEST DOUBLES (MOCKS) ==================

    /**
     * Test double for WatchlistDataAccessInterface.
     */
    private static class TestWatchlistDAO implements WatchlistDataAccessInterface {

        private final Map<String, Watchlist> watchlists = new HashMap<>();
        private boolean shouldFail = false;

        public void setShouldFail(boolean fail) {
            this.shouldFail = fail;
        }

        @Override
        public boolean addMovieToWatchlist(String username, Movie movie) {
            Watchlist watchlist = watchlists.computeIfAbsent(username, k -> new Watchlist());
            watchlist.addMovie(movie);
            return true;
        }

        @Override
        public boolean removeMovieFromWatchlist(String username, int movieId) {
            Watchlist watchlist = watchlists.get(username);
            if (watchlist == null) return false;

            Movie toRemove = watchlist.getList().stream()
                    .filter(m -> m.getId() == movieId)
                    .findFirst()
                    .orElse(null);

            if (toRemove != null) {
                watchlist.removeMovie(toRemove);
                return true;
            }
            return false;
        }

        @Override
        public Watchlist getWatchlist(String username) {
            if (shouldFail) {
                throw new RuntimeException("Failed to load watchlist");
            }
            return watchlists.getOrDefault(username, new Watchlist());
        }

        @Override
        public boolean isInWatchlist(String username, int movieId) {
            Watchlist watchlist = watchlists.get(username);
            return watchlist != null && watchlist.containsMovieById(movieId);
        }

        @Override
        public String getCurrentUsername() {
            return "testuser";
        }
    }

    /**
     * Test double for ViewWatchlistOutputBoundary.
     */
    private static class TestPresenter implements ViewWatchlistOutputBoundary {

        boolean successCalled = false;
        boolean failCalled = false;
        List<Movie> movieList = null;
        String errorMessage = null;

        @Override
        public void prepareSuccessView(ViewWatchlistOutputData outputData) {
            successCalled = true;
            movieList = outputData.getMovies();
        }

        @Override
        public void prepareFailView(String error) {
            failCalled = true;
            errorMessage = error;
        }

        public void reset() {
            successCalled = false;
            failCalled = false;
            movieList = null;
            errorMessage = null;
        }
    }
}