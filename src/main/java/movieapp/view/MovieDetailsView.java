// MovieDetailsView.java
package movieapp.view;

import movieapp.entity.Movie;

import javax.swing.*;
import java.awt.*;

public class MovieDetailsView extends JFrame {
    private final Movie movie;
    
    public MovieDetailsView(Movie movie) {
        this.movie = movie;
        initializeView();
    }
    
    private void initializeView() {
        setTitle("Movie: " + movie.getName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Movie info
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Movie Information"));
        
        infoPanel.add(new JLabel("Title: " + movie.getName()));
        infoPanel.add(new JLabel("Overview: " + movie.getOverview()));
        infoPanel.add(new JLabel("Rating: " + movie.getRate() + "/10"));
        infoPanel.add(new JLabel("Comments: " + movie.getComments().size()));
        
        // Close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        mainPanel.add(closeButton, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
}