package movieapp.view;

import movieapp.interface_adapter.rating.RatingController;
import movieapp.interface_adapter.rating.RatingState;
import movieapp.interface_adapter.rating.RatingViewModel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RatingView extends JPanel implements PropertyChangeListener {
    private final RatingController controller;
    private final RatingViewModel viewModel;
    private final String username;
    private final Integer movieID;
    private final String movieTitle;

    private JLabel currentRatingLabel;
    private JSlider ratingSlider;
    private JLabel sliderValueLabel;
    private JButton submitButton;
    private JButton removeButton;

    public RatingView(RatingController controller, RatingViewModel viewModel, String username, Integer movieID, String movieTitle, Integer initialRating) {
        this.controller = controller;
        this.viewModel = viewModel;
        this.username = username;
        this.movieID = movieID;
        this.movieTitle = movieTitle;

        this.viewModel.addPropertyChangeListener(this);
        initializeLayout();
        updateRatingDisplay(initialRating);
    }

    private void initializeLayout() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        infoPanel.setBorder(new TitledBorder("Movie Details"));

        JLabel userLabel = new JLabel("User: " + username);

        JLabel movieLabel = new JLabel("Movie: " + movieTitle);
        movieLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        currentRatingLabel = new JLabel("Current Rating: Loading...");
        currentRatingLabel.setFont(currentRatingLabel.getFont().deriveFont(Font.BOLD, 14f));

        infoPanel.add(userLabel);
        infoPanel.add(movieLabel);
        infoPanel.add(currentRatingLabel);

        this.add(infoPanel, BorderLayout.NORTH);

        setupInputPanel();
    }

    private void setupInputPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel(RatingViewModel.RATING_LABEL), gbc);

        gbc.gridx = 1;
        sliderValueLabel = new JLabel("5");
        sliderValueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        sliderValueLabel.setForeground(Color.BLUE);
        inputPanel.add(sliderValueLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        ratingSlider = new JSlider(1, 10, 5);
        ratingSlider.setMajorTickSpacing(1);
        ratingSlider.setPaintTicks(true);
        ratingSlider.setPaintLabels(true);
        ratingSlider.addChangeListener(e ->
                sliderValueLabel.setText(String.valueOf(ratingSlider.getValue()))
        );
        inputPanel.add(ratingSlider, gbc);

        this.add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        submitButton = new JButton(RatingViewModel.SUBMIT_BUTTON_LABEL);
        removeButton = new JButton(RatingViewModel.REMOVE_BUTTON_LABEL);

        submitButton.setPreferredSize(new Dimension(120, 35));
        removeButton.setPreferredSize(new Dimension(120, 35));

        submitButton.addActionListener(e -> {
            controller.execute(username, movieID, ratingSlider.getValue());
        });
        removeButton.addActionListener(e -> controller.removeRating(username, movieID));

        buttonPanel.add(submitButton);
        buttonPanel.add(removeButton);

        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateRatingDisplay(Integer rating) {
        if (rating != null) {
            currentRatingLabel.setText("Current Rating: ⭐ " + rating + "/10");
            currentRatingLabel.setForeground(new Color(0, 128, 0));
            ratingSlider.setValue(rating);
            sliderValueLabel.setText(String.valueOf(rating));
            removeButton.setEnabled(true);
        } else {
            currentRatingLabel.setText("Current Rating: Not Rated");
            currentRatingLabel.setForeground(Color.GRAY);
            removeButton.setEnabled(false);
            ratingSlider.setValue(5);
            sliderValueLabel.setText("5");
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            RatingState state = viewModel.getState();

            if (state.getMessage() != null && !state.getMessage().isEmpty()) {
                if (state.isSuccess()) {
                    JOptionPane.showMessageDialog(this,
                            state.getMessage(),
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);

                    updateRatingDisplay(state.getCurrentRating());
                } else {
                    JOptionPane.showMessageDialog(this,
                            state.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}