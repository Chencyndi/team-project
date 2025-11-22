package movieapp.view;

import movieapp.interface_adapter.movielist.MovieViewModel;

import javax.swing.*;
import java.awt.*;
import java.util.List; /**
 * Swing implementation of MovieListView.
 * Frameworks & Drivers layer.
 */
public class MovieListSwingView extends JFrame implements MovieListView {

    private final DefaultListModel<MovieViewModel> listModel = new DefaultListModel<>();
    private final JList<MovieViewModel> movieList;
    private final JLabel sortIndicatorLabel = new JLabel("Sort: Default");
    private final JLabel loadingLabel = new JLabel("Loading...");
    private final JLabel notificationLabel = new JLabel("");

    /**
     * Controller references (assigned externally)
     */
    private Runnable onPopularPressed;
    private Runnable onRecentPressed;
    private java.util.function.Consumer<String> onSortSelected;

    public MovieListSwingView() {
        setTitle("Movie Browser");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // Layout
        setLayout(new BorderLayout(10, 10));

        // Top panel (buttons + sort indicator)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JButton popularButton = new JButton("Popular Movies");
        JButton recentButton = new JButton("Recent Releases");

        JComboBox<String> sortDropdown = new JComboBox<>(new String[]{
                "Alphabetical (A→Z)",
                "Alphabetical (Z→A)",
                "Average Rating (High→Low)",
                "Average Rating (Low→High)",
                "Number of Ratings (High→Low)",
                "Number of Ratings (Low→High)"
        });

        popularButton.addActionListener(e -> {
            if (onPopularPressed != null) onPopularPressed.run();
        });

        recentButton.addActionListener(e -> {
            if (onRecentPressed != null) onRecentPressed.run();
        });

        sortDropdown.addActionListener(e -> {
            if (onSortSelected != null) {
                String option = (String) sortDropdown.getSelectedItem();
                onSortSelected.accept(option);
            }
        });

        topPanel.add(popularButton);
        topPanel.add(recentButton);
        topPanel.add(sortDropdown);
        topPanel.add(sortIndicatorLabel);

        add(topPanel, BorderLayout.NORTH);

        // Movie list
        movieList = new JList<>(listModel);
        movieList.setCellRenderer(new MovieListCellRenderer());
        JScrollPane scrollPane = new JScrollPane(movieList);

        add(scrollPane, BorderLayout.CENTER);

        // Status panel (loading + notifications)
        JPanel statusPanel = new JPanel(new BorderLayout());
        loadingLabel.setVisible(false);
        notificationLabel.setForeground(Color.BLUE);
        statusPanel.add(loadingLabel, BorderLayout.WEST);
        statusPanel.add(notificationLabel, BorderLayout.CENTER);

        add(statusPanel, BorderLayout.SOUTH);
    }

    // -------------------------------------------------------------------------
    // Interface Methods
    // -------------------------------------------------------------------------

    @Override
    public void displayMovies(List<MovieViewModel> movies) {
        SwingUtilities.invokeLater(() -> {
            listModel.clear();
            for (MovieViewModel vm : movies) {
                listModel.addElement(vm);
            }
            notificationLabel.setText(""); // clear notifications when new list is displayed
        });
    }

    @Override
    public void showError(String message) {
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE)
        );
    }

    @Override
    public void showLoading(boolean isLoading) {
        SwingUtilities.invokeLater(() -> loadingLabel.setVisible(isLoading));
    }

    @Override
    public void showNotification(String message) {
        SwingUtilities.invokeLater(() -> notificationLabel.setText(message));
    }

    @Override
    public void updateSortIndicator(String sortDescription) {
        SwingUtilities.invokeLater(() -> sortIndicatorLabel.setText("Sort: " + sortDescription));
    }

    // -------------------------------------------------------------------------
    // Controller Hook Registration
    // -------------------------------------------------------------------------

    public void setOnPopularPressed(Runnable callback) {
        this.onPopularPressed = callback;
    }

    public void setOnRecentPressed(Runnable callback) {
        this.onRecentPressed = callback;
    }

    public void setOnSortSelected(java.util.function.Consumer<String> callback) {
        this.onSortSelected = callback;
    }

    // -------------------------------------------------------------------------
    // Custom Renderer for MovieViewModel
    // -------------------------------------------------------------------------

    private static class MovieListCellRenderer extends JPanel implements ListCellRenderer<MovieViewModel> {

        private final JLabel titleLabel = new JLabel();
        private final JLabel ratingLabel = new JLabel();
        private final JLabel voteLabel = new JLabel();
        private final JTextArea overviewArea = new JTextArea();

        public MovieListCellRenderer() {
            setLayout(new BorderLayout(5, 5));

            JPanel topInfo = new JPanel(new GridLayout(1, 3));
            topInfo.add(titleLabel);
            topInfo.add(ratingLabel);
            topInfo.add(voteLabel);

            overviewArea.setLineWrap(true);
            overviewArea.setWrapStyleWord(true);
            overviewArea.setEditable(false);
            overviewArea.setOpaque(false);

            add(topInfo, BorderLayout.NORTH);
            add(overviewArea, BorderLayout.CENTER);

            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        }

        @Override
        public Component getListCellRendererComponent(
                JList<? extends MovieViewModel> list,
                MovieViewModel vm,
                int index,
                boolean isSelected,
                boolean cellHasFocus
        ) {
            titleLabel.setText(vm.getDisplayTitle());
            ratingLabel.setText("Rating: " + vm.getDisplayRating());
            voteLabel.setText("Votes: " + vm.getDisplayVoteCount());
            overviewArea.setText(vm.getDisplayOverview());

            setBackground(isSelected ? new Color(220, 220, 250) : Color.WHITE);

            return this;
        }
    }
}
