// SearchView.java
package movieapp.view;

import movieapp.entity.Movie;
import movieapp.interface_adapter.search.MovieViewModel;
import movieapp.interface_adapter.search.SearchController;
import movieapp.interface_adapter.search.SearchViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;


public class SearchView extends JFrame {
    private final SearchController searchController;
    private final SearchViewModel  searchViewModel;
    private JTextField searchField;
    private JPanel resultsPanel;
    
    public SearchView(SearchController searchController,  SearchViewModel searchViewModel) {
        this.searchController = searchController;
        this.searchViewModel = searchViewModel;
        initializeView();
    }
    
    private void initializeView() {
        setTitle("Movie Search");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        
        // Main layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Search area
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchField = new JTextField();
        JButton searchButton = new JButton("Search");
        
        searchPanel.add(new JLabel("Enter movie title:"), BorderLayout.NORTH);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        
        // Results area
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBorder(BorderFactory.createTitledBorder("Top 3 Results"));
        
        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        
        // Close button
        JButton closeButton = new JButton("Close");
        
        // Add actions
        searchButton.addActionListener(this::performSearch);
        closeButton.addActionListener(e -> dispose());
        
        // Layout
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(closeButton, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void performSearch(ActionEvent e) {
        String query = searchField.getText().trim();
        
        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term");
            return;
        }

        searchController.search(query);
        
        if (searchViewModel.isSuccess()) {
            displayMovies((searchViewModel.getMovies()));
        } else {
            JOptionPane.showMessageDialog(this, searchViewModel.getMessage());
        }
    }

    private void displayMovies(List<MovieViewModel> movieViewModels) {
        resultsPanel.removeAll();

        if (movieViewModels == null || movieViewModels.isEmpty()) {
            resultsPanel.add(new JLabel("No movies found"));
        } else {
            for (MovieViewModel vm : movieViewModels) {
                JPanel moviePanel = createMoviePanel(vm);
                resultsPanel.add(moviePanel);
                resultsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }

        resultsPanel.revalidate();
        resultsPanel.repaint();
    }


    private JPanel createMoviePanel(MovieViewModel vm) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(vm.getName());
        nameLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        nameLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showMovieDetails(vm);
            }
        });

        JLabel ratingLabel = new JLabel("Rating: " + vm.getRating() + "/10");

        panel.add(nameLabel, BorderLayout.CENTER);
        panel.add(ratingLabel, BorderLayout.EAST);

        return panel;
    }

    
    private void showMovieDetails(MovieViewModel movieViewModel) {
        MovieDetailsView detailsView = new MovieDetailsView(movieViewModel);
        detailsView.setVisible(true);
    }
}