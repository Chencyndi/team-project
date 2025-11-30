import movieapp.entity.User;
import movieapp.interface_adapter.rating.RatingPresenter;
import movieapp.interface_adapter.rating.RatingViewModel;
import movieapp.use_case.rating.RatingDataAccessInterface;
import movieapp.use_case.rating.RatingInteractor;
import movieapp.use_case.rating.RatingOutputData;
import movieapp.use_case.rating.RatingInputData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RatingTest {

    private InMemoryUserDAO userDAO;
    private RatingViewModel viewModel;
    private RatingPresenter presenter;
    private RatingInteractor interactor;

    @BeforeEach
    void setUp() {
        userDAO = new InMemoryUserDAO();
        viewModel = new RatingViewModel();
        presenter = new RatingPresenter(viewModel);
        interactor = new RatingInteractor(userDAO, presenter);

        userDAO.addUser(new User("user1", "pass1"));
        userDAO.addUser(new User("user2", "pass2"));
        userDAO.addUser(new User("user3", "pass3"));
    }

    @Test
    @DisplayName("Execute should add rating and return success")
    void testExecuteSuccess() {
        RatingOutputData result = interactor.execute(
                new RatingInputData("user1", 1, 8)
        );

        assertTrue(result.isSuccess());
        assertEquals("Rating submitted successfully", result.getMessage());
        assertEquals(8, result.getNewRating());
        assertEquals(8.0, result.getAverageRating());
    }

    @Test
    @DisplayName("Execute should reject rating below 1")
    void testExecuteRatingTooLow() {
        RatingOutputData result = interactor.execute(
                new RatingInputData("user1", 1, 0)
        );

        assertFalse(result.isSuccess());
        assertEquals("Something went wrong", result.getMessage());
        assertNull(result.getNewRating());
        assertNull(result.getAverageRating());
    }

    @Test
    @DisplayName("Execute should reject rating above 10")
    void testExecuteRatingTooHigh() {
        RatingOutputData result = interactor.execute(
                new RatingInputData("user1", 1, 11)
        );

        assertFalse(result.isSuccess());
        assertEquals("Something went wrong", result.getMessage());
        assertNull(result.getNewRating());
        assertNull(result.getAverageRating());
    }

    @Test
    @DisplayName("Execute should handle boundary rating 1")
    void testExecuteBoundaryRating1() {
        RatingOutputData result = interactor.execute(
                new RatingInputData("user1", 1, 1)
        );

        assertTrue(result.isSuccess());
        assertEquals(1, result.getNewRating());
        assertEquals(1.0, result.getAverageRating());
    }

    @Test
    @DisplayName("Execute should handle boundary rating 10")
    void testExecuteBoundaryRating10() {
        RatingOutputData result = interactor.execute(
                new RatingInputData("user1", 1, 10)
        );

        assertTrue(result.isSuccess());
        assertEquals(10, result.getNewRating());
        assertEquals(10.0, result.getAverageRating());
    }

    @Test
    @DisplayName("RemoveRating should remove rating and return success")
    void testRemoveRatingSuccess() {
        interactor.execute(
                new RatingInputData("user1", 1, 8)
        );

        RatingOutputData result = interactor.removeRating("user1", 1);

        assertTrue(result.isSuccess());
        assertEquals("Rating removed successfully", result.getMessage());
        assertNull(result.getNewRating());
        assertNull(result.getAverageRating());
    }

    @Test
    @DisplayName("RemoveRating should handle non-existent rating")
    void testRemoveRatingNonExistent() {
        RatingOutputData result = interactor.removeRating("user1", 999);

        assertTrue(result.isSuccess());
        assertNull(result.getNewRating());
        assertNull(result.getAverageRating());
    }

    @Test
    @DisplayName("Execute should handle exception when adding rating")
    void testExecuteExceptionOnAddRating() {
        RatingDataAccessInterface faultyDAO = new FaultyRatingDAO(true, false);
        RatingInteractor interactorWithFaultyDAO = new RatingInteractor(faultyDAO, presenter);

        RatingOutputData result = interactorWithFaultyDAO.execute(
                new RatingInputData("user1", 1, 8)
        );

        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().startsWith("Failed to submit rating:"));
        assertNull(result.getNewRating());
        assertNull(result.getAverageRating());
    }

    @Test
    @DisplayName("Execute should handle exception when getting average rating")
    void testExecuteExceptionOnGetAverage() {
        RatingDataAccessInterface faultyDAO = new FaultyRatingDAO(false, true);
        RatingInteractor interactorWithFaultyDAO = new RatingInteractor(faultyDAO, presenter);

        RatingOutputData result = interactorWithFaultyDAO.execute(
                new RatingInputData("user1", 1, 8)
        );

        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().startsWith("Failed to submit rating:"));
        assertNull(result.getNewRating());
        assertNull(result.getAverageRating());
    }

    @Test
    @DisplayName("RemoveRating should handle exception when removing")
    void testRemoveRatingException() {
        RatingDataAccessInterface faultyDAO = new FaultyRatingDAO(true, false);
        RatingInteractor interactorWithFaultyDAO = new RatingInteractor(faultyDAO, presenter);

        RatingOutputData result = interactorWithFaultyDAO.removeRating("user1", 1);

        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().startsWith("Failed to remove rating:"));
        assertNull(result.getNewRating());
        assertNull(result.getAverageRating());
    }

    @Test
    @DisplayName("RemoveRating should handle exception when getting average after removal")
    void testRemoveRatingExceptionOnGetAverage() {
        RatingDataAccessInterface faultyDAO = new FaultyRatingDAO(false, true);
        RatingInteractor interactorWithFaultyDAO = new RatingInteractor(faultyDAO, presenter);

        RatingOutputData result = interactorWithFaultyDAO.removeRating("user1", 1);

        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().startsWith("Failed to remove rating:"));
        assertNull(result.getNewRating());
        assertNull(result.getAverageRating());
    }

    private static class FaultyRatingDAO implements RatingDataAccessInterface {
        private final boolean throwOnAddOrRemove;
        private final boolean throwOnGetAverage;

        public FaultyRatingDAO(boolean throwOnAddOrRemove, boolean throwOnGetAverage) {
            this.throwOnAddOrRemove = throwOnAddOrRemove;
            this.throwOnGetAverage = throwOnGetAverage;
        }

        @Override
        public void addRating(Integer movieID, String username, Integer rating) {
            if (throwOnAddOrRemove) {
                throw new RuntimeException("Database error on addRating");
            }
        }

        @Override
        public void removeRating(Integer movieID, String username) {
            if (throwOnAddOrRemove) {
                throw new RuntimeException("Database error on removeRating");
            }
        }

        @Override
        public Integer getMovieRating(String username, Integer movieID) {
            return null;
        }

        @Override
        public Double getAverageRating(Integer movieID) {
            if (throwOnGetAverage) {
                throw new RuntimeException("Database error on getAverageRating");
            }
            return null;
        }
    }
}