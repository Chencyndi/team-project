import movieapp.data_access.UserDataAccessObject;
import movieapp.entity.User;
import movieapp.interface_adapter.rating.*;
import movieapp.use_case.rating.*;
import movieapp.use_case.common.UserDataAccessInterface;
import movieapp.view.RatingView;

import javax.swing.*;

public class UseCase3Application {

    public static void main(String[] args) {
        RatingDataAccessInterface userDataAccess = new UserDataAccessObject();

        RatingViewModel ratingViewModel = new RatingViewModel();
        RatingPresenter ratingPresenter = new RatingPresenter(ratingViewModel);
        RatingInputBoundary ratingInteractor = new RatingInteractor(userDataAccess, ratingPresenter);
        RatingController ratingController = new RatingController(ratingInteractor);

        String username = "testuser";
        int movieID = 640146;
        String movieName = "Ant-Man and the Wasp: Quantumania";
        Integer initialRating = userDataAccess.getMovieRating(username, movieID);
        Double initialAverage = userDataAccess.getAverageRating(movieID);

        SwingUtilities.invokeLater(() -> {
            JFrame appFrame = new JFrame("Rating Use Case Demo");
            appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            appFrame.setSize(450, 450);
            appFrame.setLocationRelativeTo(null);

            RatingView ratingView = new RatingView(ratingController, ratingViewModel, username, movieID, movieName, initialRating);

            RatingState state = ratingViewModel.getState();
            if (initialAverage != null) {
                state.setAverageRatingLabel(initialAverage + "/10");
            } else {
                state.setAverageRatingLabel("N/A");
            }
            ratingViewModel.firePropertyChange();

            appFrame.add(ratingView);
            appFrame.setVisible(true);
        });
    }
}