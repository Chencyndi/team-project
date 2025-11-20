package movieapp.view;

import movieapp.entity.Movie;
import movieapp.interface_adapter.watchlist.WatchlistController;
import movieapp.interface_adapter.watchlist.WatchlistState;
import movieapp.interface_adapter.watchlist.WatchlistViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * View for displaying and managing the watchlist.
 */
public class WatchlistView extends JPanel implements PropertyChangeListener {

    private final String viewName = "watchlist";
    private final WatchlistViewModel viewModel;
    private final WatchlistController controller;

    private final JPanel moviesPanel;
    private final JLabel messageLabel;
    private final JButton refreshButton;
    private String currentUsername;

    public WatchlistView(WatchlistViewModel viewModel, WatchlistController controller) {
        this.viewModel = viewModel;
        this.controller = controller;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        final JPanel titlePanel = new JPanel();
        final JLabel title = new JLabel(WatchlistViewModel.TITLE);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(title);

        messageLabel = new JLabel();
        messageLabel.setForeground(Color.BLUE);
        titlePanel.add(messageLabel);

        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshWatchlist());
        titlePanel.add(refreshButton);

        add(titlePanel, BorderLayout.NORTH);

        moviesPanel = new JPanel();
        moviesPanel.setLayout(new BoxLayout(moviesPanel, BoxLayout.Y_AXIS));
        final JScrollPane scrollPane = new JScrollPane(moviesPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Sets the current username for this view.
     * Call this when the view is displayed.
     * @param username the logged-in user's username
     */
    public void setCurrentUsername(String username) {
        this.currentUsername = username;
        refreshWatchlist();
    }

    private void refreshWatchlist() {
        if (currentUsername != null) {
            controller.viewWatchlist(currentUsername);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (WatchlistViewModel.WATCHLIST_UPDATED.equals(evt.getPropertyName())) {
            final WatchlistState state = (WatchlistState) evt.getNewValue();
            updateView(state);
        }
    }

    private void updateView(WatchlistState state) {
        if (state.getMessage() != null) {
            messageLabel.setText(state.getMessage());
            messageLabel.setForeground(Color.BLUE);
        } else if (state.getError() != null) {
            messageLabel.setText(state.getError());
            messageLabel.setForeground(Color.RED);
        } else {
            messageLabel.setText("");
        }

        moviesPanel.removeAll();

        if (state.getMovies().isEmpty()) {
            final JLabel emptyLabel = new JLabel("Your watchlist is empty");
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            moviesPanel.add(emptyLabel);
        } else {
            for (Movie movie : state.getMovies()) {
                moviesPanel.add(createMoviePanel(movie));
            }
        }

        moviesPanel.revalidate();
        moviesPanel.repaint();
    }

    private JPanel createMoviePanel(Movie movie) {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        final JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        final JLabel titleLabel = new JLabel(movie.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        infoPanel.add(titleLabel);

        panel.add(infoPanel, BorderLayout.CENTER);

        final JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(e -> {
            if (currentUsername != null) {
                controller.removeFromWatchlist(currentUsername, movie.getId(), movie.getTitle());
            }
        });
        panel.add(removeButton, BorderLayout.EAST);

        return panel;
    }

    public String getViewName() {
        return viewName;
    }
}