package movieapp.view;

import movieapp.data_access.CommentDataAccessObject;
import movieapp.data_access.InMemoryWatchlistDAO;
import movieapp.data_access.TMDBMovieAPIAccess;
import movieapp.entity.Comment;
import movieapp.entity.Movie;
import movieapp.interface_adapter.comment.PostCommentController;
import movieapp.interface_adapter.rating.RatingController;
import movieapp.interface_adapter.watchlist.WatchlistController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

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
    private final InMemoryWatchlistDAO watchlistDAO;
    private final RatingController ratingController;
    private final PostCommentController commentController;
    private final CommentDataAccessObject commentDataAccess;
    private final String username;
    private final JPanel movieListPanel;
    private List<Movie> currentMovies;

    // Search and filter components
    private final JTextField searchField;
    private final JComboBox<String> sortComboBox;
    private final JComboBox<String> filterComboBox;

    public HomePageView(TMDBMovieAPIAccess movieAPI,
                        WatchlistController watchlistController,
                        InMemoryWatchlistDAO watchlistDAO,
                        RatingController ratingController,
                        PostCommentController commentController,
                        CommentDataAccessObject commentDataAccess,
                        String username) {
        this.movieAPI = movieAPI;
        this.watchlistController = watchlistController;
        this.watchlistDAO = watchlistDAO;
        this.ratingController = ratingController;
        this.commentController = commentController;
        this.commentDataAccess = commentDataAccess;
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

        // Search (Use Case 5)
        searchField = new JTextField(20);
        searchField.setToolTipText("Search movies by title");
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchMovies());

        // Sort (Use Case 5)
        sortComboBox = new JComboBox<>(new String[]{
                "Most Popular", "Highest Rated", "Most Recent", "Title A-Z"
        });
        sortComboBox.addActionListener(e -> sortMovies());

        // Filter (Use Case 5)
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

    private void searchMovies() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            displayMovies(currentMovies);
            return;
        }

        List<Movie> filtered = currentMovies.stream()
                .filter(m -> m.getTitle().toLowerCase().contains(query.toLowerCase()))
                .toList();
        displayMovies(filtered);
    }

    private void sortMovies() {
        if (currentMovies == null) return;

        String sortOption = (String) sortComboBox.getSelectedItem();
        List<Movie> sorted = switch (sortOption) {
            case "Highest Rated" -> currentMovies.stream()
                    .sorted((m1, m2) -> Double.compare(m2.getVoteAverage(), m1.getVoteAverage()))
                    .toList();
            case "Most Recent" -> currentMovies.stream()
                    .sorted((m1, m2) -> m2.getReleaseDate().compareTo(m1.getReleaseDate()))
                    .toList();
            case "Title A-Z" -> currentMovies.stream()
                    .sorted((m1, m2) -> m1.getTitle().compareTo(m2.getTitle()))
                    .toList();
            default -> currentMovies.stream()
                    .sorted((m1, m2) -> Double.compare(m2.getPopularity(), m1.getPopularity()))
                    .toList();
        };
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
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

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
        int commentCount = getCommentCount(movie.getId());
        JButton commentsButton = new JButton("💬 Comments (" + commentCount + ")");
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
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Rate: " + movie.getTitle(), true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel currentRatingLabel = new JLabel("Current Rating: " + movie.getVoteAverage() + "/10");
        currentRatingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(currentRatingLabel);
        panel.add(Box.createVerticalStrut(20));

        JLabel instructionLabel = new JLabel("Your Rating:");
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(instructionLabel);

        // Rating slider
        JSlider ratingSlider = new JSlider(0, 10, 5);
        ratingSlider.setMajorTickSpacing(1);
        ratingSlider.setPaintTicks(true);
        ratingSlider.setPaintLabels(true);
        panel.add(ratingSlider);
        panel.add(Box.createVerticalStrut(10));

        JLabel selectedRatingLabel = new JLabel("Selected: 5/10");
        selectedRatingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ratingSlider.addChangeListener(e ->
                selectedRatingLabel.setText("Selected: " + ratingSlider.getValue() + "/10"));
        panel.add(selectedRatingLabel);
        panel.add(Box.createVerticalStrut(20));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton submitButton = new JButton("Submit Rating");
        submitButton.addActionListener(e -> {
            int rating = ratingSlider.getValue();
            // Call rating controller
            var result = ratingController.execute(username, movie.getId(), rating);

            if (result.isSuccess()) {
                JOptionPane.showMessageDialog(dialog,
                        result.getMessage(),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog,
                        result.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    // === USE CASE 4: COMMENTS ===

    /**
     * Get the count of comments for a movie from the database
     */
    private int getCommentCount(Integer movieId) {
        try {
            List<Comment> comments = commentDataAccess.getComments(movieId);
            return comments.size();
        } catch (Exception e) {
            return 0;
        }
    }

    private void showCommentsDialog(Movie movie) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Comments: " + movie.getTitle(), true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // Load comments from database
        List<Comment> comments;
        try {
            comments = commentDataAccess.getComments(movie.getId());
        } catch (Exception e) {
            comments = List.of();
        }

        // Comments list
        JPanel commentsPanel = new JPanel();
        commentsPanel.setLayout(new BoxLayout(commentsPanel, BoxLayout.Y_AXIS));

        if (comments.isEmpty()) {
            JLabel noCommentsLabel = new JLabel("No comments yet. Be the first to comment!");
            noCommentsLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            commentsPanel.add(noCommentsLabel);
        } else {
            for (Comment comment : comments) {
                commentsPanel.add(createCommentCard(comment));
            }
        }

        JScrollPane commentsScroll = new JScrollPane(commentsPanel);
        dialog.add(commentsScroll, BorderLayout.CENTER);

        // Post comment section
        JPanel postPanel = new JPanel(new BorderLayout(10, 10));
        postPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextArea commentArea = new JTextArea(3, 40);
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        JScrollPane textScroll = new JScrollPane(commentArea);

        JButton postButton = new JButton("Post Comment");
        postButton.addActionListener(e -> {
            String commentText = commentArea.getText().trim();
            if (!commentText.isEmpty()) {
                // Call comment controller
                var result = commentController.execute(username, commentText, movie.getId());

                if (result.getCommentId() != null) {
                    JOptionPane.showMessageDialog(dialog,
                            "Comment posted successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    // Refresh the comments dialog to show the new comment
                    showCommentsDialog(movie);
                } else {
                    JOptionPane.showMessageDialog(dialog,
                            "Failed to post comment. Please try again.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(dialog,
                        "Comment cannot be empty!",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        postPanel.add(new JLabel("Post a comment:"), BorderLayout.NORTH);
        postPanel.add(textScroll, BorderLayout.CENTER);
        postPanel.add(postButton, BorderLayout.EAST);

        dialog.add(postPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    /**
     * Create a comment card UI component (supports nested replies)
     */
    private JPanel createCommentCard(Comment comment) {
        JPanel commentCard = new JPanel();
        commentCard.setLayout(new BoxLayout(commentCard, BoxLayout.Y_AXIS));
        commentCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Author and text
        JLabel authorLabel = new JLabel(comment.getCommenter().getUsername());
        authorLabel.setFont(new Font("Arial", Font.BOLD, 12));

        JLabel contentLabel = new JLabel("<html><p style='width:500px'>" +
                comment.getText() + "</p></html>");
        contentLabel.setFont(new Font("Arial", Font.PLAIN, 11));

        commentCard.add(authorLabel);
        commentCard.add(Box.createVerticalStrut(5));
        commentCard.add(contentLabel);

        // Display replies (if any)
        if (!comment.getReplies().isEmpty()) {
            JPanel repliesPanel = new JPanel();
            repliesPanel.setLayout(new BoxLayout(repliesPanel, BoxLayout.Y_AXIS));
            repliesPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 0, 0)); // Indent replies

            for (Comment reply : comment.getReplies()) {
                repliesPanel.add(createReplyCard(reply));
            }

            commentCard.add(repliesPanel);
        }

        return commentCard;
    }

    /**
     * Create a reply card (simplified version of comment card)
     */
    private JPanel createReplyCard(Comment reply) {
        JPanel replyCard = new JPanel();
        replyCard.setLayout(new BoxLayout(replyCard, BoxLayout.Y_AXIS));
        replyCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 2, 0, 0, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 5)
        ));
        replyCard.setBackground(new Color(250, 250, 250));

        JLabel authorLabel = new JLabel("↳ " + reply.getCommenter().getUsername());
        authorLabel.setFont(new Font("Arial", Font.BOLD, 11));
        authorLabel.setForeground(new Color(100, 100, 100));

        JLabel contentLabel = new JLabel("<html><p style='width:450px'>" +
                reply.getText() + "</p></html>");
        contentLabel.setFont(new Font("Arial", Font.PLAIN, 10));

        replyCard.add(authorLabel);
        replyCard.add(contentLabel);

        return replyCard;
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