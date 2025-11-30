import movieapp.entity.User;
import movieapp.interface_adapter.rating.RatingPresenter;
import movieapp.interface_adapter.rating.RatingViewModel;
import movieapp.use_case.rating.RatingDataAccessInterface;
import movieapp.use_case.rating.RatingInputData;
import movieapp.use_case.rating.RatingInteractor;
import movieapp.use_case.rating.RatingOutputData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RatingInteractorTest {

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
    }

    @Test
    void testExecuteValidRating() {
        RatingOutputData result = interactor.execute(
                new RatingInputData("user1", 1, 8)
        );

        assertTrue(result.isSuccess());
        assertEquals(8, result.getNewRating());
        assertEquals(8.0, result.getAverageRating());
    }

    @Test
    void testExecuteRatingTooLow() {
        RatingOutputData result = interactor.execute(
                new RatingInputData("user1", 1, 0)
        );

        assertFalse(result.isSuccess());
        assertNull(result.getNewRating());
    }

    @Test
    void testExecuteRatingTooHigh() {
        RatingOutputData result = interactor.execute(
                new RatingInputData("user1", 1, 11)
        );

        assertFalse(result.isSuccess());
        assertNull(result.getNewRating());
    }

    @Test
    void testExecuteMinRating() {
        RatingOutputData result = interactor.execute(
                new RatingInputData("user1", 1, 1)
        );

        assertTrue(result.isSuccess());
        assertEquals(1, result.getNewRating());
    }

    @Test
    void testExecuteMaxRating() {
        RatingOutputData result = interactor.execute(
                new RatingInputData("user1", 1, 10)
        );

        assertTrue(result.isSuccess());
        assertEquals(10, result.getNewRating());
    }

    @Test
    void testRemoveRating() {
        interactor.execute(new RatingInputData("user1", 1, 8));

        RatingOutputData result = interactor.removeRating("user1", 1);

        assertTrue(result.isSuccess());
        assertNull(result.getNewRating());
    }

    @Test
    void testRemoveNonExistentRating() {
        RatingOutputData result = interactor.removeRating("user1", 999);

        assertTrue(result.isSuccess());
    }

    @Test
    void testAverageRatingMultipleUsers() {
        interactor.execute(new RatingInputData("user1", 1, 8));
        interactor.execute(new RatingInputData("user2", 1, 6));

        assertEquals(7.0, userDAO.getAverageRating(1));
    }

    @Test
    void testExecuteException() {
        BrokenDAO brokenDAO = new BrokenDAO();
        RatingInteractor brokenInteractor = new RatingInteractor(brokenDAO, presenter);

        RatingOutputData result = brokenInteractor.execute(
                new RatingInputData("user1", 1, 5)
        );

        assertFalse(result.isSuccess());
        assertNull(result.getNewRating());
    }

    @Test
    void testRemoveRatingException() {
        BrokenDAO brokenDAO = new BrokenDAO();
        RatingInteractor brokenInteractor = new RatingInteractor(brokenDAO, presenter);

        RatingOutputData result = brokenInteractor.removeRating("user1", 1);

        assertFalse(result.isSuccess());
    }

    private static class BrokenDAO implements RatingDataAccessInterface {
        @Override
        public void addRating(Integer movieID, String username, Integer rating) {
            throw new RuntimeException("error");
        }

        @Override
        public void removeRating(Integer movieID, String username) {
            throw new RuntimeException("error");
        }

        @Override
        public Integer getMovieRating(String username, Integer movieID) {
            return null;
        }

        @Override
        public Double getAverageRating(Integer movieID) {
            throw new RuntimeException("error");
        }
    }
}