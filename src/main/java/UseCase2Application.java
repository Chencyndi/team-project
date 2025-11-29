import movieapp.data_access.InMemoryWatchlistDAO;
import movieapp.data_access.TMDBMovieAPIAccess;
import movieapp.entity.Movie;
import movieapp.interface_adapter.watchlist.*;
import movieapp.use_case.watchlist.*;
import movieapp.view.WatchlistView;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Use Case 2 Application: Watchlist Management
 */
public class UseCase2Application {

    private static String currentUsername = "demo_user";

    public static void main(String[] args) {
        // Setup data access
        InMemoryWatchlistDAO watchlistDAO = new InMemoryWatchlistDAO();
        watchlistDAO.setCurrentUsername(currentUsername);
        TMDBMovieAPIAccess movieAPI = new TMDBMovieAPIAccess();

        // Setup use cases
        WatchlistViewModel watchlistViewModel = new WatchlistViewModel();
        WatchlistPresenter watchlistPresenter = new WatchlistPresenter(watchlistViewModel);

        AddToWatchlistInputBoundary addInteractor =
                new AddToWatchlistInteractor(watchlistDAO, watchlistPresenter);
        RemoveFromWatchlistInputBoundary removeInteractor =
                new RemoveFromWatchlistInteractor(watchlistDAO, watchlistPresenter);
        ViewWatchlistInputBoundary viewInteractor =
                new ViewWatchlistInteractor(watchlistDAO, watchlistPresenter);

        WatchlistController watchlistController = new WatchlistController(
                addInteractor, removeInteractor, viewInteractor);

        SwingUtilities.invokeLater(() -> {
            try {
                // Fetch movies
                System.out.println("Fetching movies from TMDB API...");
                List<Movie> movies = movieAPI.fetchPopularMovies(100);
                System.out.println("Fetched " + movies.size() + " movies");

                // Create main frame
                JFrame frame = new JFrame("Use Case 2: Watchlist Management");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(1000, 700);
                frame.setLocationRelativeTo(null);

                // Create tabbed pane (simple navigation)
                JTabbedPane tabbedPane = new JTabbedPane();

                // Tab 1: Browse Movies
                JPanel browsePanel = createBrowsePanel(movies, watchlistController, watchlistDAO);
                tabbedPane.addTab("Browse Movies", browsePanel);

                // Tab 2: My Watchlist
                WatchlistView watchlistView = new WatchlistView(watchlistViewModel, watchlistController);
                watchlistView.setCurrentUsername(currentUsername);
                tabbedPane.addTab("My Watchlist", watchlistView);

                // Refresh browse panel when switching tabs
                tabbedPane.addChangeListener(e -> {
                    if (tabbedPane.getSelectedIndex() == 0) {
                        // Switched to Browse Movies - refresh the cards
                        refreshBrowsePanel(browsePanel, movies, watchlistController, watchlistDAO);
                    }
                });

                frame.add(tabbedPane);
                frame.setVisible(true);

                System.out.println("Application started successfully!");

            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Failed to load movies: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private static JPanel createBrowsePanel(List<Movie> movies,
                                            WatchlistController controller,
                                            InMemoryWatchlistDAO dao) {
        JPanel panel = new JPanel(new BorderLayout());

        // Header
        JLabel header = new JLabel("Popular Movies (" + movies.size() + " loaded)",
                SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 16));
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(header, BorderLayout.NORTH);

        // Movie list
        JPanel movieList = new JPanel();
        movieList.setLayout(new BoxLayout(movieList, BoxLayout.Y_AXIS));

        // Add all movies
        for (Movie movie : movies) {
            JPanel movieCard = createSimpleMovieCard(movie, controller, dao);
            movieList.add(movieCard);
        }

        JScrollPane scrollPane = new JScrollPane(movieList);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private static void refreshBrowsePanel(JPanel browsePanel,
                                           List<Movie> movies,
                                           WatchlistController controller,
                                           InMemoryWatchlistDAO dao) {
        // Get the scroll pane (component 1 in BorderLayout.CENTER)
        Component scrollComp = browsePanel.getComponent(1);
        if (scrollComp instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) scrollComp;
            JViewport viewport = scrollPane.getViewport();
            Component view = viewport.getView();

            if (view instanceof JPanel) {
                JPanel movieList = (JPanel) view;

                movieList.removeAll();

                for (Movie movie : movies) {
                    JPanel movieCard = createSimpleMovieCard(movie, controller, dao);
                    movieList.add(movieCard);
                }

                movieList.revalidate();
                movieList.repaint();
            }
        }
    }

    private static JPanel createSimpleMovieCard(Movie movie,
                                                WatchlistController controller,
                                                InMemoryWatchlistDAO dao) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        // Movie info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(movie.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel detailsLabel = new JLabel(
                String.format("⭐ %.1f/10 | 📅 %s",
                        movie.getVoteAverage(),
                        movie.getReleaseDate())
        );
        detailsLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        infoPanel.add(titleLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(detailsLabel);

        card.add(infoPanel, BorderLayout.CENTER);

        // Button
        JButton button;
        if (dao.isInWatchlist(currentUsername, movie.getId())) {
            button = new JButton("✓ In Watchlist");
            button.setEnabled(false);
        } else {
            button = new JButton("Add to Watchlist");
            button.addActionListener(e -> {
                if (dao.isInWatchlist(currentUsername, movie.getId())) {
                    // Alternative Flow: Already in watchlist
                    JOptionPane.showMessageDialog(
                            SwingUtilities.getWindowAncestor(card),
                            "\"" + movie.getTitle() + "\" is already in your watchlist.",
                            "Movie Already in Watchlist",
                            JOptionPane.WARNING_MESSAGE
                    );
                } else {
                    // Main Flow: Add to watchlist
                    controller.addToWatchlist(currentUsername, movie);
                    button.setText("✓ In Watchlist");
                    button.setEnabled(false);
                    // Removed duplicate popup - WatchlistPresenter will show it
                }
            });
        }

        card.add(button, BorderLayout.EAST);

        return card;
    }
}
