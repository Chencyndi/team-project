package movieapp.view;


import movieapp.entity.Movie;
import movieapp.interface_adapter.watchlist.WatchlistController;

import javax.swing.*;
import java.awt.*;

/**
 * Button component to add a movie to the watchlist.
 */
public class AddToWatchlistButton extends JButton {

    private final Movie movie;
    private final WatchlistController controller;
    private final String username;
    private boolean isInWatchlist;

    public AddToWatchlistButton(Movie movie, WatchlistController controller, String username) {
        this.movie = movie;
        this.controller = controller;
        this.username = username;
        this.isInWatchlist = false;

        // Initial appearance
        setText("+ Add to Watchlist");
        setFont(new Font("Arial", Font.PLAIN, 12));
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add click listener
        addActionListener(e -> toggleWatchlist());

        // Style the button
        updateButtonStyle();
    }

    private void toggleWatchlist() {
        if (!isInWatchlist) {
            // Add to watchlist
            controller.addToWatchlist(username, movie);
            isInWatchlist = true;
            setText("✓ In Watchlist");
            setEnabled(false); // Prevent adding twice
            updateButtonStyle();

            JOptionPane.showMessageDialog(
                    this,
                    movie.getTitle() + " added to your watchlist!",
                    "Added to Watchlist",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void updateButtonStyle() {
        if (isInWatchlist) {
            setBackground(new Color(76, 175, 80)); // Green
            setForeground(Color.WHITE);
        } else {
            setBackground(new Color(33, 150, 243)); // Blue
            setForeground(Color.WHITE);
        }
    }

    /**
     * Check if this movie is already in the watchlist.
     * Call this after creating the button to update its state.
     */
    public void checkIfInWatchlist(boolean inWatchlist) {
        this.isInWatchlist = inWatchlist;
        if (inWatchlist) {
            setText("✓ In Watchlist");
            setEnabled(false);
            updateButtonStyle();
        }
    }
}