// UseCase4Application.java
import movieapp.data_access.CommentDataAccessObject;
import movieapp.data_access.TMDBMovieAPIAccess;
import movieapp.data_access.UserDataAccessObject;
import movieapp.entity.Movie;
import movieapp.entity.User;
import movieapp.entity.Watchlist;
import movieapp.interface_adapter.account.CreateAccountController;
import movieapp.interface_adapter.account.CreateAccountPresenter;
import movieapp.interface_adapter.comment.PostCommentController;
import movieapp.interface_adapter.comment.PostCommentPresenter;
import movieapp.interface_adapter.comment.PostCommentViewModel;
import movieapp.interface_adapter.login.LoginController;
import movieapp.interface_adapter.login.LoginPresenter;
import movieapp.use_case.account.CreateAccountInputBoundary;
import movieapp.use_case.account.CreateAccountInteractor;
import movieapp.use_case.comment.PostCommentInputBoundary;
import movieapp.use_case.comment.PostCommentInteractor;
import movieapp.use_case.common.UserDataAccessInterface;
import movieapp.use_case.login.LoginInputBoundary;
import movieapp.use_case.login.LoginInteractor;
import movieapp.view.MovieCommentView;

import javax.swing.*;
import java.util.function.Supplier;

public class UseCase4Application {
    // Simple current username storage for demo purposes
    private static volatile String currentUsername = null;
    
    // Simple username setter for login callbacks
    public static void setCurrentUsername(String username) {
        currentUsername = username;
    }
    
    public static String getCurrentUsername() {
        return currentUsername;
    }
    
    public static void main(String[] args) {
        UserDataAccessInterface accountRepo = new UserDataAccessObject();

        // Create test users
        User testUser1 = new User("testuser", "password123");
        User testUser2 = new User("max", "password123");
        User testUser3 = new User("Luke", "password123");
        accountRepo.addUser(testUser1);
        accountRepo.addUser(testUser2);
        accountRepo.addUser(testUser3);

        UserDataAccessInterface userDataAccess = new UserDataAccessAdapter(accountRepo);
        CommentDataAccessObject commentDataAccess = new CommentDataAccessObject();

        // Setup Post Comment use case
        PostCommentViewModel postCommentViewModel = new PostCommentViewModel();
        PostCommentPresenter postCommentPresenter = new PostCommentPresenter(postCommentViewModel);
        PostCommentInputBoundary postCommentInteractor = new PostCommentInteractor(
                commentDataAccess,
                userDataAccess,
                postCommentPresenter);
        PostCommentController postCommentController = new PostCommentController(postCommentInteractor);

        // Setup Login use case
        LoginPresenter loginPresenter = new LoginPresenter();
        LoginInputBoundary loginInteractor = new LoginInteractor(accountRepo, loginPresenter);
        LoginController loginController = new LoginController(loginInteractor);
        
        // Setup Create Account use case
        CreateAccountPresenter createAccountPresenter = new CreateAccountPresenter();
        CreateAccountInputBoundary createAccountInteractor = new CreateAccountInteractor
                (accountRepo, createAccountPresenter);
        CreateAccountController createAccountController = new CreateAccountController(createAccountInteractor);

        // Current username supplier - returns the logged in username
        Supplier<String> currentUsernameSupplier = () -> currentUsername;

        // Movie ID to display
        int movieID = 640146;

        SwingUtilities.invokeLater(() -> {
            try {
                // Fetch movie information from TMDB API
                TMDBMovieAPIAccess movieAPI = new TMDBMovieAPIAccess();
                Movie movie = movieAPI.findById(movieID);

                //just example
                if (movie == null) {
                    movie = new Movie(movieID, "Movie " + movieID, 
                            "Movie description", "", "", 7.5, 0.0, 0, 
                            new java.util.ArrayList<>());
                }

                // Create and show the complete movie comment view
                MovieCommentView movieCommentView = new MovieCommentView(
                        movie,
                        postCommentController,
                        postCommentViewModel,
                        commentDataAccess,
                        currentUsernameSupplier,
                        loginController
                );
                movieCommentView.showView();
                
            } catch (Exception e) {
                // If API fails, create a simple movie object
                Movie movie = new Movie(movieID, "Movie " + movieID, 
                        "Movie description", "", "", 7.5, 0.0, 0, 
                        new java.util.ArrayList<>());
                
                MovieCommentView movieCommentView = new MovieCommentView(
                        movie,
                        postCommentController,
                        postCommentViewModel,
                        commentDataAccess,
                        currentUsernameSupplier,
                        loginController
                );
                movieCommentView.showView();
            }
        });
    }

    // Adapter class to convert AccountRepository to UserDataAccessInterface
    private static class UserDataAccessAdapter implements UserDataAccessInterface {
        private final UserDataAccessInterface accountRepository;

        public UserDataAccessAdapter(UserDataAccessInterface accountRepository) {
            this.accountRepository = accountRepository;
        }

        @Override
        public boolean existsByUsername(String username) {
            return accountRepository.existsByUsername(username);
        }

        @Override
        public void addUser(User user) {
            accountRepository.addUser(user);
        }

        @Override
        public User findByUsername(String username) {
            return accountRepository.findByUsername(username);
        }

        @Override
        public Watchlist getWatchlist(String username) {
            return new Watchlist();
        }

        @Override
        public void addWatchlist(Integer movieID, String username) {
            // Not implemented for this use case
        }

        @Override
        public void removeWatchlist(Integer movieID, String username) {
            // Not implemented for this use case
        }

        @Override
        public Integer getMovieRating(String username, Integer movieID) {
            return null;
        }

        @Override
        public void addRating(Integer movieID, String username, Integer rating) {
            // Not implemented for this use case
        }

        @Override
        public void removeRating(Integer movieID, String username) {
            // Not implemented for this use case
        }
    }
}

