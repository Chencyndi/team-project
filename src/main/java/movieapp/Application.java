package movieapp;

import movieapp.data_access.*;
import movieapp.interface_adapter.createAccount.*;
import movieapp.interface_adapter.login.*;
import movieapp.interface_adapter.rating.*;
import movieapp.interface_adapter.comment.*;
import movieapp.interface_adapter.watchlist.*;
import movieapp.use_case.login.*;
import movieapp.use_case.movielist.MovieDataSource;
import movieapp.use_case.createAccount.*;
import movieapp.use_case.watchlist.*;
import movieapp.use_case.rating.*;
import movieapp.use_case.comment.*;
import movieapp.view.*;

import javax.swing.*;

public class Application {

    private static JFrame mainFrame;
    private static LoginView loginView;
    private static String loggedInUser;
    private static UserDataAccessObject userDB;

    /** ========================= START PROGRAM ========================== **/
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Application::launchLoginScreen);
    }

    /** ========================= SHOW LOGIN FIRST ======================= **/
    private static void launchLoginScreen() {
        userDB = new UserDataAccessObject();

        LoginViewModel loginVM = new LoginViewModel();
        CreateAccountViewModel createAccountVM = new CreateAccountViewModel();

        LoginPresenter loginPresenter = new LoginPresenter(loginVM) {
            @Override
            public void presentSuccess(LoginOutputData data) {
                loginVM.setSuccess(true);
                loginVM.setMessage("Welcome " + data.getUsername() + "!");
                loginVM.setUsername(data.getUsername());
                loggedInUser = data.getUsername();
                launchHomePage();
            }
        };

        CreateAccountPresenter createPresenter = new CreateAccountPresenter(createAccountVM);

        LoginInputBoundary loginInteractor = new LoginInteractor(userDB, loginPresenter);
        CreateAccountInputBoundary createInteractor = new CreateAccountInteractor(userDB, createPresenter);

        LoginController loginController = new LoginController(loginInteractor);
        CreateAccountController createController = new CreateAccountController(createInteractor);

        LoginView loginScreen = new LoginView(loginController, loginVM, createController, createAccountVM);
        new CreateAccountView(createController, createAccountVM, loginScreen);

        loginScreen.setVisible(true);
    }


    /** ====================== HOMEPAGE AFTER LOGIN ====================== **/
    private static void launchHomePage() {
        if (loginView != null) {
            loginView.setVisible(false);
        }

        /** ---------- Watchlist System ----------- */
        DatabaseWatchlistDAO watchlistDAO = new DatabaseWatchlistDAO();
        watchlistDAO.setCurrentUsername(loggedInUser);

        WatchlistViewModel watchlistVM = new WatchlistViewModel();
        WatchlistPresenter watchlistPresenter = new WatchlistPresenter(watchlistVM);

        WatchlistController watchlistController = new WatchlistController(
                new AddToWatchlistInteractor(watchlistDAO, watchlistPresenter),
                new RemoveFromWatchlistInteractor(watchlistDAO, watchlistPresenter),
                new ViewWatchlistInteractor(watchlistDAO, watchlistPresenter)
        );

        /** ---------- Rating System ----------- */
        RatingViewModel ratingViewModel = new RatingViewModel();
        RatingPresenter ratingPresenter = new RatingPresenter(ratingViewModel);
        RatingInteractor ratingInteractor = new RatingInteractor(userDB, ratingPresenter);
        RatingController ratingController = new RatingController(ratingInteractor);

        /** ---------- Comment System ----------- */
        CommentDataAccessObject commentDB = new CommentDataAccessObject(userDB);
        PostCommentViewModel commentVM = new PostCommentViewModel();
        PostCommentPresenter commentPresenter = new PostCommentPresenter(commentVM);
        PostCommentController commentController = new PostCommentController(
                new PostCommentInteractor(commentDB, userDB, commentPresenter)
        );

//        /** ---------- Login Objects for HomePageView ----------- */
        LoginViewModel loginVM = new LoginViewModel();
        LoginController loginController = new LoginController(
                new LoginInteractor(userDB, new LoginPresenter(loginVM) {
                    @Override
                    public void presentSuccess(LoginOutputData data) {}
                    @Override
                    public void presentUserNotFound(String msg) {}
                    @Override
                    public void presentInvalidPassword(String msg) {}
                    @Override
                    public void presentValidationError(String msg) {}
                })
        );

        /** ---------- Home UI Frame ----------- */
        MovieDataSource movieDataSource = new TMDBMovieAPIAccess();
        HomePageView homePage = new HomePageView(
                movieDataSource,
                watchlistController,
                watchlistDAO,
                ratingController,
                ratingViewModel,
                commentController,
                commentVM,
                commentDB,
                loginVM,
                loginController,
                loggedInUser
        );

        WatchlistView watchlistView = new WatchlistView(watchlistVM, watchlistController);
        watchlistView.setCurrentUsername(loggedInUser);

        /** ---------- UI Tabs ----------- */
        mainFrame = new JFrame("Movie App - Logged in as " + loggedInUser);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1200, 800);
        mainFrame.setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("🏠 Home", homePage);
        tabs.addTab("⭐ My Watchlist", watchlistView);

        tabs.addChangeListener(e -> {
            if (tabs.getSelectedIndex() == 0) {
                homePage.refreshMovies();
            }
            if (tabs.getSelectedIndex() == 1) {
                watchlistController.viewWatchlist(loggedInUser);
            }
        });

        /** ---------- LOGOUT BUTTON ----------- */
        JMenuBar bar = new JMenuBar();
        JMenu account = new JMenu("Account");
        JMenuItem logout = new JMenuItem("Logout");
        logout.addActionListener(e -> logout());
        account.add(logout);
        bar.add(account);
        mainFrame.setJMenuBar(bar);

        mainFrame.add(tabs);
        mainFrame.setVisible(true);
    }

    /** ======================== RETURN TO LOGIN ======================== **/
    private static void logout() {
        if (mainFrame != null) {
            mainFrame.dispose();
        }
        loggedInUser = null;
        if (loginView != null) {
            loginView.setVisible(true);
        }
    }
}