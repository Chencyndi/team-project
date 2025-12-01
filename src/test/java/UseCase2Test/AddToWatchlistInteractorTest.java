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
 * Test class for AddToWatchlistInteractor.
 * Tests all paths through the business logic to achieve 100% code coverage.
 */
class AddToWatchlistInteractorTest {

    private TestWatchlistDAO dataAccess;
    private TestPresenter presenter;
    private AddToWatchlistInteractor interactor;

    @BeforeEach
    void setUp() {
        dataAccess = new TestWatchlistDAO();
        presenter = new TestPresenter();
        interactor = new AddToWatchlistInteractor(dataAccess, presenter);
    }

    @Test
    void testAddMovieSuccess() {
        Movie movie = createTestMovie(1, "Inception");
        AddToWatchlistInputData inputData = new AddToWatchlistInputData("user1", movie);

        interactor.execute(inputData);

        assertTrue(presenter.successCalled, "Success method should be called");
        assertFalse(presenter.failCalled, "Fail method should NOT be called");
        assertEquals("Successfully added to watchlist", presenter.successMessage);
        assertTrue(dataAccess.isInWatchlist("user1", 1), "Movie should be in watchlist");
    }

    @Test
    void testAddMovieAlreadyInWatchlist() {
        Movie movie = createTestMovie(1, "Inception");
        dataAccess.addMovieToWatchlist("user1", movie); // Already added

        AddToWatchlistInputData inputData = new AddToWatchlistInputData("user1", movie);

        interactor.execute(inputData);

        assertTrue(presenter.failCalled, "Fail method should be called");
        assertFalse(presenter.successCalled, "Success method should NOT be called");
        assertEquals("Movie is already in your watchlist.", presenter.errorMessage);
    }

    @Test
    void testAddMovieDataAccessFailure() {
        dataAccess.setShouldFail(true);
        Movie movie = createTestMovie(1, "Inception");
        AddToWatchlistInputData inputData = new AddToWatchlistInputData("user1", movie);

        interactor.execute(inputData);

        assertTrue(presenter.failCalled, "Fail method should be called");
        assertFalse(presenter.successCalled, "Success method should NOT be called");
        assertTrue(presenter.errorMessage.contains("Failed"), "Error should mention failure");
    }

    @Test
    void testAddMultipleMoviesForSameUser() {
        Movie movie1 = createTestMovie(1, "Inception");
        Movie movie2 = createTestMovie(2, "The Matrix");

        interactor.execute(new AddToWatchlistInputData("user1", movie1));
        presenter.reset();
        interactor.execute(new AddToWatchlistInputData("user1", movie2));

        assertTrue(presenter.successCalled);
        assertTrue(dataAccess.isInWatchlist("user1", 1));
        assertTrue(dataAccess.isInWatchlist("user1", 2));
    }

    @Test
    void testAddSameMovieForDifferentUsers() {
        Movie movie = createTestMovie(1, "Inception");

        interactor.execute(new AddToWatchlistInputData("user1", movie));
        presenter.reset();
        interactor.execute(new AddToWatchlistInputData("user2", movie));

        assertTrue(presenter.successCalled);
        assertTrue(dataAccess.isInWatchlist("user1", 1));
        assertTrue(dataAccess.isInWatchlist("user2", 1));
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
     * Simple in-memory implementation for testing.
     */
    private static class TestWatchlistDAO implements WatchlistDataAccessInterface {

        private final Map<String, Watchlist> watchlists = new HashMap<>();
        private boolean shouldFail = false;

        public void setShouldFail(boolean fail) {
            this.shouldFail = fail;
        }

        @Override
        public boolean addMovieToWatchlist(String username, Movie movie) {
            if (shouldFail) {
                return false;
            }

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
     * Test double for AddToWatchlistOutputBoundary.
     * Records what methods were called for verification.
     */
    private static class TestPresenter implements AddToWatchlistOutputBoundary {

        boolean successCalled = false;
        boolean failCalled = false;
        String successMessage = null;
        String errorMessage = null;

        @Override
        public void prepareSuccessView(AddToWatchlistOutputData outputData) {
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