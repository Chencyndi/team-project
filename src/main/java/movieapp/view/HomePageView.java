package movieapp.view;

import movieapp.data_access.CommentDataAccessObject;
import movieapp.data_access.DatabaseWatchlistDAO;
import movieapp.data_access.TMDBMovieAPIAccess;
import movieapp.data_access.TMDBMovieRepository;
import movieapp.entity.Movie;
import movieapp.interface_adapter.comment.PostCommentController;
import movieapp.interface_adapter.comment.PostCommentViewModel;
import movieapp.interface_adapter.login.LoginController;
import movieapp.interface_adapter.login.LoginViewModel;
import movieapp.interface_adapter.rating.RatingController;
import movieapp.interface_adapter.search.MovieRepository;
import movieapp.interface_adapter.search.SearchController;
import movieapp.interface_adapter.search.SearchPresenter;
import movieapp.interface_adapter.search.SearchViewModel;
import movieapp.interface_adapter.rating.RatingViewModel;
import movieapp.interface_adapter.watchlist.WatchlistController;
import movieapp.use_case.search.SearchInputBoundary;
import movieapp.use_case.search.SearchInteractor;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Supplier;

/**
 * Complete HomePage integrating all 6 use cases:
 * 1. Login/Logout (Authentication)
 * 2. Watchlist (Add to watchlist)
 * 3. Ratings (Rate movies)
 * 4. Comments (Post comments)
 * 5. Browse Movies (Search/Filter/Sort)
 * 6. Movie Details (View details)
 */
public class HomePageView extends JPanel {

    private final TMDBMovieAPIAccess movieAPI;
    private final WatchlistController watchlistController;
    private final DatabaseWatchlistDAO watchlistDAO;
    private final RatingController ratingController;
    private final RatingViewModel ratingViewModel;
    private final PostCommentController commentController;
    private final PostCommentViewModel commentViewModel;
    private final CommentDataAccessObject commentDataAccess;
    private final LoginViewModel loginViewModel;
    private final LoginController loginController;
    private final String username;
    private final JPanel movieListPanel;
    private List<Movie> currentMovies;

    // Search and filter components
    private final JTextField searchField;
    private final JComboBox<String> sortComboBox;
    private final JComboBox<String> filterComboBox;

    public HomePageView(TMDBMovieAPIAccess movieAPI,
                        WatchlistController watchlistController,
                        DatabaseWatchlistDAO watchlistDAO,
                        RatingController ratingController,
                        RatingViewModel ratingViewModel,
                        PostCommentController commentController,
                        PostCommentViewModel commentViewModel,
                        CommentDataAccessObject commentDataAccess,
                        LoginViewModel loginViewModel,
                        LoginController loginController,
                        String username) {
        this.movieAPI = movieAPI;
        this.watchlistController = watchlistController;
        this.watchlistDAO = watchlistDAO;
        this.ratingController = ratingController;
        this.ratingViewModel = ratingViewModel;
        this.commentController = commentController;
        this.commentViewModel = commentViewModel;
        this.commentDataAccess = commentDataAccess;
        this.loginViewModel = loginViewModel;
        this.loginController = loginController;
        this.username = username;


        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // === TOP PANEL: Search and Filter (Use Case 5: Browse Movies) ===
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Welcome message (Use Case 1: Login/Authentication)
        JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomePanel.add(welcomeLabel);
        topPanel.add(welcomePanel, BorderLayout.NORTH);

        // Search and filter panel
        JPanel searchFilterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        
        searchField = new JTextField(20);
        searchField.setToolTipText("Search movies by title");
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchMovies());

        // Fetch Popular Movies (Use Case 5)
        JButton popularButton = new JButton("Popular Movies");
        popularButton.addActionListener(e -> loadPopularMovies());

        // Fetch Recent Movies (Use Case 5)
        JButton recentButton = new JButton("Recent Releases");
        recentButton.addActionListener(e -> loadRecentMovies());

        // Sort options (Use Case 5)
        sortComboBox = new JComboBox<>(new String[]{
            "Alphabetical (A→Z)",
            "Alphabetical (Z→A)",
            "Average Rating (High→Low)",
            "Average Rating (Low→High)",
            "Number of Ratings (High→Low)",
            "Number of Ratings (Low→High)"
        });
        sortComboBox.addActionListener(e -> sortMovies());

        filterComboBox = new JComboBox<>(new String[]{
                "All Movies", "Action", "Comedy", "Drama", "Horror", "Sci-Fi"
        });
        filterComboBox.addActionListener(e -> filterMovies());

        // Refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadMovies());

        searchFilterPanel.add(new JLabel("Search:"));
        searchFilterPanel.add(searchField);
        searchFilterPanel.add(searchButton);
        searchFilterPanel.add(popularButton);
        searchFilterPanel.add(recentButton);
        searchFilterPanel.add(new JLabel("Sort:"));
        searchFilterPanel.add(sortComboBox);

        searchFilterPanel.add(new JLabel("Filter:"));
        searchFilterPanel.add(filterComboBox);
        searchFilterPanel.add(refreshButton);

        topPanel.add(searchFilterPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // === CENTER PANEL: Movie List ===
        movieListPanel = new JPanel();
        movieListPanel.setLayout(new BoxLayout(movieListPanel, BoxLayout.Y_AXIS));
        movieListPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(movieListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Load initial movies
        loadMovies();
    }

    private void loadMovies() {
        movieListPanel.removeAll();
        movieListPanel.add(createLoadingLabel());
        movieListPanel.revalidate();
        movieListPanel.repaint();

        SwingWorker<List<Movie>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Movie> doInBackground() throws Exception {
                return movieAPI.fetchPopularMovies(100);
            }

            @Override
            protected void done() {
                try {
                    currentMovies = get();
                    displayMovies(currentMovies);
                } catch (Exception e) {
                    showError("Failed to load movies: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    /**
     * Loads popular movies by default.
     */
    private void loadPopularMovies() {
        loadMovies();
    }

    /**
     * Loads recently released movies using a background task to avoid blocking the UI.
     * Displays a temporary loading indicator while fetching data and then updates the UI.
     */
    private void loadRecentMovies() {
        // Clear the movie panel and show a loading indicator
        movieListPanel.removeAll();
        movieListPanel.add(createLoadingLabel());
        movieListPanel.revalidate();
        movieListPanel.repaint();

        // Perform the API call in a background thread to keep the UI responsive
        SwingWorker<List<Movie>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Movie> doInBackground() throws Exception {
                // Fetch the most recent 100 movies from the API
                return movieAPI.fetchRecentMovies(100);
            }

            @Override
            protected void done() {
                try {
                    // Retrieve the fetched movie list and display it
                    currentMovies = get();
                    displayMovies(currentMovies);
                } catch (Exception e) {
                    // Show an error message if something goes wrong
                    showError("Failed to load recent movies: " + e.getMessage());
                }
            }
        };

        // Start the background task
        worker.execute();
    }

    private void searchMovies() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            displayMovies(currentMovies);
            return;
        }
        MovieRepository movieRepository = new TMDBMovieRepository();
        SearchViewModel searchViewModel = new SearchViewModel();
        SearchPresenter searchPresenter = new SearchPresenter(searchViewModel);
        SearchInputBoundary searchInteractor = new SearchInteractor(movieRepository, searchPresenter);
        SearchController searchController = new SearchController(searchInteractor);
        searchController.search(query);

        List<Movie> filtered = searchViewModel.getMovies();
        displayMovies(filtered);
    }

    /**
     * Sorts the current list of movies based on the option selected in the combo box
     * and updates the UI with the sorted results.
     */
    private void sortMovies() {
        // Exit early if there are no movies to sort
        if (currentMovies == null) return;

        // Retrieve the selected sorting option from the combo box
        String option = (String) sortComboBox.getSelectedItem();

        // Determine sorting logic based on the selected option using a switch expression
        List<Movie> sorted = switch (option) {

            // Sort titles alphabetically A→Z (case-insensitive)
            case "Alphabetical (A→Z)" ->
                    currentMovies.stream()
                            .sorted((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()))
                            .toList();

            // Sort titles alphabetically Z→A (case-insensitive)
            case "Alphabetical (Z→A)" ->
                    currentMovies.stream()
                            .sorted((a, b) -> b.getTitle().compareToIgnoreCase(a.getTitle()))
                            .toList();

            // Sort movies by average rating, highest first
            case "Average Rating (High→Low)" ->
                    currentMovies.stream()
                            .sorted((a, b) -> Double.compare(b.getVoteAverage(), a.getVoteAverage()))
                            .toList();

            // Sort movies by average rating, lowest first
            case "Average Rating (Low→High)" ->
                    currentMovies.stream()
                            .sorted((a, b) -> Double.compare(a.getVoteAverage(), b.getVoteAverage()))
                            .toList();

            // Sort by total number of ratings, highest first
            case "Number of Ratings (High→Low)" ->
                    currentMovies.stream()
                            .sorted((a, b) -> Integer.compare(b.getVoteCount(), a.getVoteCount()))
                            .toList();

            // Sort by total number of ratings, lowest first
            case "Number of Ratings (Low→High)" ->
                    currentMovies.stream()
                            .sorted((a, b) -> Integer.compare(a.getVoteCount(), b.getVoteCount()))
                            .toList();

            // If no matching option found, keep original list unsorted
            default -> currentMovies;
        };

        // Update the UI to display the sorted list of movies
        displayMovies(sorted);
    }

    private void filterMovies() {
        if (currentMovies == null) return;

        String filter = (String) filterComboBox.getSelectedItem();
        if ("All Movies".equals(filter)) {
            displayMovies(currentMovies);
            return;
        }

        // Note: TMDB API doesn't return genre in basic movie object
        // This is a placeholder - you'd need to fetch genre data separately
        displayMovies(currentMovies);
    }

    private void displayMovies(List<Movie> movies) {
        movieListPanel.removeAll();

        if (movies.isEmpty()) {
            JLabel emptyLabel = new JLabel("No movies found", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            movieListPanel.add(emptyLabel);
        } else {
            for (Movie movie : movies) {
                movieListPanel.add(createMovieCard(movie));
                movieListPanel.add(Box.createVerticalStrut(10));
            }
        }

        movieListPanel.revalidate();
        movieListPanel.repaint();
    }

    private JPanel createMovieCard(Movie movie) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        // === LEFT: Movie Info (Use Case 6: Movie Details) ===
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(movie.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel ratingLabel = new JLabel(String.format("⭐ %.1f/10 (%d votes)",
                movie.getVoteAverage(), movie.getVoteCount()));
        ratingLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        ratingLabel.setForeground(new Color(100, 100, 100));

        JLabel dateLabel = new JLabel("📅 " + movie.getReleaseDate());
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        dateLabel.setForeground(new Color(100, 100, 100));

        // Overview (truncated)
        String overview = movie.getOverview();
        if (overview.length() > 150) {
            overview = overview.substring(0, 147) + "...";
        }
        JLabel overviewLabel = new JLabel("<html><p style='width:400px'>" + overview + "</p></html>");
        overviewLabel.setFont(new Font("Arial", Font.PLAIN, 11));

        infoPanel.add(titleLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(ratingLabel);
        infoPanel.add(dateLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(overviewLabel);

        card.add(infoPanel, BorderLayout.CENTER);

        // === RIGHT: Action Buttons ===
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Color.WHITE);

        // Use Case 2: Add to Watchlist
        JButton watchlistButton = createWatchlistButton(movie);
        buttonPanel.add(watchlistButton);
        buttonPanel.add(Box.createVerticalStrut(5));

        // Use Case 3: Rate Movie
        JButton rateButton = new JButton("⭐ Rate");
        rateButton.addActionListener(e -> showRatingDialog(movie));
        buttonPanel.add(rateButton);
        buttonPanel.add(Box.createVerticalStrut(5));

        // Use Case 4: View/Post Comments - Load comment count from database
        JButton commentsButton = new JButton("💬 Comments");
        commentsButton.addActionListener(e -> showCommentsDialog(movie));
        buttonPanel.add(commentsButton);
        buttonPanel.add(Box.createVerticalStrut(5));

        // Use Case 6: View Details
        JButton detailsButton = new JButton("ℹ️ Details");
        detailsButton.addActionListener(e -> showMovieDetails(movie));
        buttonPanel.add(detailsButton);

        card.add(buttonPanel, BorderLayout.EAST);

        return card;
    }

    // === USE CASE 2: WATCHLIST ===
    private JButton createWatchlistButton(Movie movie) {
        JButton button;
        if (watchlistDAO.isInWatchlist(username, movie.getId())) {
            button = new JButton("✓ In Watchlist");
            button.setEnabled(false);
        } else {
            button = new JButton("Add to Watchlist");
            button.addActionListener(e -> {
                watchlistController.addToWatchlist(username, movie);
                button.setText("✓ In Watchlist");
                button.setEnabled(false);
            });
        }
        button.setFocusPainted(false);
        return button;
    }

    // === USE CASE 3: RATE MOVIE ===
    private void showRatingDialog(Movie movie) {
        SwingUtilities.invokeLater(() -> {
            // initial rating is optional; RatingView will handle fetching via controller
            Integer initialRating = null;

            RatingView ratingView = new RatingView(
                    ratingController,
                    ratingViewModel,
                    username,
                    movie.getId(),
                    movie.getTitle(),
                    initialRating
            );

            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                    "Rate: " + movie.getTitle(), true);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.add(ratingView);
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        });
    }

    // === USE CASE 4: COMMENTS ===
    private void showCommentsDialog(Movie movie) {
        SwingUtilities.invokeLater(() -> {
            Supplier<String> currentUsernameSupplier = () -> username;

            MovieCommentView commentView = new MovieCommentView(
                    movie,
                    commentController,
                    commentViewModel,
                    commentDataAccess,
                    currentUsernameSupplier,
                    loginViewModel,
                    loginController
            );

            commentView.showView(); // Opens the comment UI exactly like Use Case 4
        });
    }

    // === USE CASE 6: MOVIE DETAILS ===
    private void showMovieDetails(Movie movie) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                movie.getTitle(), true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel(movie.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));

        panel.add(new JLabel("Release Date: " + movie.getReleaseDate()));
        panel.add(new JLabel("Rating: " + movie.getVoteAverage() + "/10 (" +
                movie.getVoteCount() + " votes)"));
        panel.add(new JLabel("Popularity: " + movie.getPopularity()));
        panel.add(Box.createVerticalStrut(10));

        JLabel overviewLabel = new JLabel("<html><p style='width:500px'><b>Overview:</b><br>" +
                movie.getOverview() + "</p></html>");
        panel.add(overviewLabel);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        panel.add(Box.createVerticalStrut(20));
        panel.add(closeButton);

        dialog.add(new JScrollPane(panel));
        dialog.setVisible(true);
    }

    // === UTILITY METHODS ===
    private JLabel createLoadingLabel() {
        JLabel label = new JLabel("Loading movies...", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.ITALIC, 16));
        return label;
    }

    private void showError(String message) {
        movieListPanel.removeAll();
        JLabel errorLabel = new JLabel(message, SwingConstants.CENTER);
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        errorLabel.setForeground(Color.RED);
        movieListPanel.add(errorLabel);
        movieListPanel.revalidate();
        movieListPanel.repaint();
    }

    public void refreshMovies() {
        loadMovies();
    }
}