import movieapp.data_access.UserDataAccessObject;
import movieapp.entity.User;
import movieapp.interface_adapter.rating.*;
import movieapp.use_case.rating.*;
import movieapp.use_case.common.UserDataAccessInterface;
import movieapp.view.RatingView;

import javax.swing.*;

public class UseCase3Application {

    public static void main(String[] args) {
        UserDataAccessInterface userDataAccess = new UserDataAccessObject();
        setupTestUser(userDataAccess); // Helper to ensure user exists

        RatingViewModel ratingViewModel = new RatingViewModel();
        RatingPresenter ratingPresenter = new RatingPresenter(ratingViewModel);
        RatingInputBoundary ratingInteractor = new RatingInteractor(userDataAccess, ratingPresenter);
        RatingController ratingController = new RatingController(ratingInteractor);

        String username = "testuser";
        int movieID = 640146;
        String movieName = "Ant-Man and the Wasp: Quantumania";
        Integer initialRating = userDataAccess.getMovieRating(username, movieID);

        SwingUtilities.invokeLater(() -> {
            JFrame appFrame = new JFrame("Rating Use Case Demo");
            appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            appFrame.setSize(450, 400);
            appFrame.setLocationRelativeTo(null);

            RatingView ratingView = new RatingView(ratingController, ratingViewModel, username, movieID, movieName, initialRating);

            appFrame.add(ratingView);
            appFrame.setVisible(true);
        });
    }

    private static void setupTestUser(UserDataAccessInterface dao) {
        if (!dao.existsByUsername("testuser")) {
            User testUser = new User("testuser", "password123");
            dao.addUser(testUser);
        }
    }
}