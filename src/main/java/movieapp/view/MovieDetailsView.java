// MovieDetailsView.java
package movieapp.view;

import movieapp.entity.Movie;
import movieapp.interface_adapter.search.MovieViewModel;

import javax.swing.*;
import java.awt.*;

public class MovieDetailsView extends JFrame {
    private final MovieViewModel movieViewModel;
    
    public MovieDetailsView(MovieViewModel movieViewModel) {
        this.movieViewModel = movieViewModel;
        initializeView();
    }
    
    private void initializeView() {
        setTitle("Movie: " + movieViewModel.getName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Movie info
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Movie Information"));
        
        infoPanel.add(new JLabel("Title: " + movieViewModel.getName()));
        infoPanel.add(new JLabel("Overview: " + movieViewModel.getOverview()));
        infoPanel.add(new JLabel("Rating: " + movieViewModel.getRating() + "/10"));
        infoPanel.add(new JLabel("Comments: " + movieViewModel.getCommentCount()));
        
        // Close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        mainPanel.add(closeButton, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
}