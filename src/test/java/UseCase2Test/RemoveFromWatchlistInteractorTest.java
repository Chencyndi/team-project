package UseCase2Test;

import movieapp.entity.Movie;
import movieapp.entity.Watchlist;
import movieapp.use_case.watchlist.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for RemoveFromWatchlistInteractor.
 * Tests all paths through the business logic to achieve 100% code coverage.
 */
class RemoveFromWatchlistInteractorTest {

    private TestWatchlistDAO dataAccess;
    private TestPresenter presenter;
    private RemoveFromWatchlistInteractor interactor;

    @BeforeEach
    void setUp() {
        dataAccess = new TestWatchlistDAO();
        presenter = new TestPresenter();
        interactor = new RemoveFromWatchlistInteractor(dataAccess, presenter);
    }

    @Test
    void testRemoveMovieSuccess() {
        Movie movie = createTestMovie(1, "Inception");
        dataAccess.addMovieToWatchlist("user1", movie);

        RemoveFromWatchlistInputData inputData =
                new RemoveFromWatchlistInputData("user1", 1, "Inception");

        interactor.execute(inputData);

        assertTrue(presenter.successCalled, "Success method should be called");
        assertFalse(presenter.failCalled, "Fail method should NOT be called");
        assertEquals("Successfully removed from watchlist", presenter.successMessage);
        assertFalse(dataAccess.isInWatchlist("user1", 1), "Movie should be removed");
    }

    @Test
    void testRemoveMovieNotInWatchlist() {
        RemoveFromWatchlistInputData inputData =
                new RemoveFromWatchlistInputData("user1", 999, "Nonexistent Movie");

        interactor.execute(inputData);

        assertTrue(presenter.failCalled, "Fail method should be called");
        assertFalse(presenter.successCalled, "Success method should NOT be called");
        assertTrue(presenter.errorMessage.contains("not") ||
                presenter.errorMessage.contains("not in watchlist"));
    }

    @Test
    void testRemoveMovieDataAccessFailure() {
        Movie movie = createTestMovie(1, "Inception");
        dataAccess.addMovieToWatchlist("user1", movie);
        dataAccess.setShouldFail(true);

        RemoveFromWatchlistInputData inputData =
                new RemoveFromWatchlistInputData("user1", 1, "Inception");

        interactor.execute(inputData);

        assertTrue(presenter.failCalled, "Fail method should be called");
        assertFalse(presenter.successCalled, "Success method should NOT be called");
        assertTrue(presenter.errorMessage.contains("Failed"));
    }

    @Test
    void testRemoveFromEmptyWatchlist() {
        RemoveFromWatchlistInputData inputData =
                new RemoveFromWatchlistInputData("user1", 1, "Some Movie");

        interactor.execute(inputData);

        assertTrue(presenter.failCalled, "Fail method should be called");
        assertFalse(presenter.successCalled, "Success method should NOT be called");
    }

    @Test
    void testRemoveMultipleMovies() {
        Movie movie1 = createTestMovie(1, "Inception");
        Movie movie2 = createTestMovie(2, "The Matrix");
        dataAccess.addMovieToWatchlist("user1", movie1);
        dataAccess.addMovieToWatchlist("user1", movie2);

        interactor.execute(new RemoveFromWatchlistInputData("user1", 1, "Inception"));

        assertTrue(presenter.successCalled);
        assertFalse(dataAccess.isInWatchlist("user1", 1));
        assertTrue(dataAccess.isInWatchlist("user1", 2), "Other movie should remain");
    }

    @Test
    void testRemoveFromDifferentUsers() {
        Movie movie = createTestMovie(1, "Inception");
        dataAccess.addMovieToWatchlist("user1", movie);
        dataAccess.addMovieToWatchlist("user2", movie);

        interactor.execute(new RemoveFromWatchlistInputData("user1", 1, "Inception"));

        assertTrue(presenter.successCalled);
        assertFalse(dataAccess.isInWatchlist("user1", 1));
        assertTrue(dataAccess.isInWatchlist("user2", 1), "user2 should still have it");
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
            if (shouldFail) {
                return false;
            }

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
     * Test double for RemoveFromWatchlistOutputBoundary.
     */
    private static class TestPresenter implements RemoveFromWatchlistOutputBoundary {

        boolean successCalled = false;
        boolean failCalled = false;
        String successMessage = null;
        String errorMessage = null;

        @Override
        public void prepareSuccessView(RemoveFromWatchlistOutputData outputData) {
            successCalled = true;
            successMessage = outputData.getMessage();
        }

        @Override
        public void prepareFailView(String error) {
            failCalled = true;
            errorMessage = error;
        }

        public void reset() {
            successCalled = false;
            failCalled = false;
            successMessage = null;
            errorMessage = null;
        }
    }
}