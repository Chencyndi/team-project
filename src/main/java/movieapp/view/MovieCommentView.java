package movieapp.view;

import movieapp.data_access.CommentDataAccessObject;
import movieapp.entity.Comment;
import movieapp.entity.Movie;
import movieapp.interface_adapter.comment.PostCommentController;
import movieapp.interface_adapter.comment.PostCommentViewModel;
import movieapp.interface_adapter.login.LoginController;
import movieapp.interface_adapter.login.LoginViewModel;
import movieapp.use_case.comment.CommentDataAccessInterface;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.function.Supplier;

/**
 * Complete view for displaying movie information and comments.
 * Supports viewing comments, posting comments, and replying to comments.
 * Implements UseCase 4.1 and 4.2.
 */
public class MovieCommentView extends JFrame {
    private final Movie movie;
    private final PostCommentController postCommentController;
    private final PostCommentViewModel postCommentViewModel;
    private final CommentDataAccessInterface commentDataAccess;
    private final Supplier<String> currentUsernameSupplier;
    private final LoginViewModel loginViewModel;
    private final LoginController loginController;
    
    private JPanel commentsPanel;
    private JScrollPane commentsScrollPane;
    
    public MovieCommentView(Movie movie,
                           PostCommentController postCommentController,
                           PostCommentViewModel postCommentViewModel,
                           CommentDataAccessInterface commentDataAccess,
                           Supplier<String> currentUsernameSupplier,
                           LoginViewModel loginViewModel,
                           LoginController loginController) {
        this.movie = movie;
        this.postCommentController = postCommentController;
        this.postCommentViewModel = postCommentViewModel;
        this.commentDataAccess = commentDataAccess;
        this.currentUsernameSupplier = currentUsernameSupplier;
        this.loginViewModel = loginViewModel;
        this.loginController = loginController;
        
        initializeView();
        loadAndDisplayComments();
    }
    
    private void initializeView() {
        setTitle("Movie: " + movie.getTitle());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Movie Information Panel
        JPanel movieInfoPanel = createMovieInfoPanel();
        mainPanel.add(movieInfoPanel, BorderLayout.NORTH);
        
        // Comments Section
        JPanel commentsSection = createCommentsSection();
        mainPanel.add(commentsSection, BorderLayout.CENTER);
        
        // Post Comment Button
        JButton postCommentButton = new JButton("Post Comment");
        postCommentButton.addActionListener(e -> openPostCommentDialog());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(postCommentButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createMovieInfoPanel() {
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder("Movie Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        infoPanel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(movie.getTitle()), gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        infoPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JTextArea descriptionArea = new JTextArea(movie.getOverview());
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBackground(infoPanel.getBackground());
        descriptionArea.setRows(2);
        infoPanel.add(descriptionArea, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        infoPanel.add(new JLabel("Rating:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(String.format("%.1f/10", movie.getVoteAverage())), gbc);
        
        return infoPanel;
    }
    
    private JPanel createCommentsSection() {
        JPanel sectionPanel = new JPanel(new BorderLayout());
        sectionPanel.setBorder(BorderFactory.createTitledBorder("Comments"));
        
        commentsPanel = new JPanel();
        commentsPanel.setLayout(new BoxLayout(commentsPanel, BoxLayout.Y_AXIS));
        commentsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        commentsScrollPane = new JScrollPane(commentsPanel);
        commentsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        commentsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        sectionPanel.add(commentsScrollPane, BorderLayout.CENTER);
        
        return sectionPanel;
    }
    
    private void loadAndDisplayComments() {
        commentsPanel.removeAll();
        
        List<Comment> comments = commentDataAccess.getComments(movie.getId());
        
        if (comments.isEmpty()) {
            JLabel noCommentsLabel = new JLabel("No comments yet. Be the first to comment!");
            noCommentsLabel.setForeground(Color.GRAY);
            noCommentsLabel.setBorder(new EmptyBorder(20, 20, 20, 20));
            commentsPanel.add(noCommentsLabel);
        } else {
            for (Comment comment : comments) {
                commentsPanel.add(createCommentPanel(comment, 0));
            }
        }
        
        commentsPanel.revalidate();
        commentsPanel.repaint();
    }
    
    private JPanel createCommentPanel(Comment comment, int indentLevel) {
        JPanel commentPanel = new JPanel();
        commentPanel.setLayout(new BoxLayout(commentPanel, BoxLayout.Y_AXIS));
        commentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
            new EmptyBorder(10, 10 + indentLevel * 30, 10, 10)
        ));
        commentPanel.setBackground(indentLevel > 0 ? new Color(250, 250, 250) : Color.WHITE);
        
        // Main comment content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(commentPanel.getBackground());
        
        // Comment header (username)
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerPanel.setBackground(commentPanel.getBackground());
        JLabel usernameLabel = new JLabel(comment.getCommenter().getUsername());
        usernameLabel.setFont(usernameLabel.getFont().deriveFont(Font.BOLD));
        headerPanel.add(usernameLabel);
        
        // Comment text
        JTextArea commentTextArea = new JTextArea(comment.getText());
        commentTextArea.setEditable(false);
        commentTextArea.setLineWrap(true);
        commentTextArea.setWrapStyleWord(true);
        commentTextArea.setBackground(commentPanel.getBackground());
        commentTextArea.setBorder(new EmptyBorder(5, 0, 5, 0));
        
        // Reply button
        JButton replyButton = new JButton("Reply");
        replyButton.addActionListener(e -> openReplyDialog(comment));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttonPanel.setBackground(commentPanel.getBackground());
        buttonPanel.add(replyButton);
        
        contentPanel.add(headerPanel, BorderLayout.NORTH);
        contentPanel.add(commentTextArea, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        commentPanel.add(contentPanel);
        
        // Add replies if any
        List<Comment> replies = comment.getReplies();
        if (!replies.isEmpty()) {
            JPanel repliesPanel = new JPanel();
            repliesPanel.setLayout(new BoxLayout(repliesPanel, BoxLayout.Y_AXIS));
            repliesPanel.setBackground(new Color(245, 245, 245));
            repliesPanel.setBorder(new EmptyBorder(5, 20, 0, 0));
            
            for (Comment reply : replies) {
                repliesPanel.add(createCommentPanel(reply, indentLevel + 1));
            }
            
            commentPanel.add(repliesPanel);
        }
        
        return commentPanel;
    }
    
    private void openPostCommentDialog() {
        PostCommentView postCommentView = new PostCommentView(
            this,
            postCommentController,
            postCommentViewModel,
            movie.getId(),
            currentUsernameSupplier,
            this::loadAndDisplayComments,
            loginViewModel,
            loginController
        );
        postCommentView.showView();
    }
    
    private void openReplyDialog(Comment parentComment) {
        PostReplyView postReplyView = new PostReplyView(
            this,
            postCommentController,
            postCommentViewModel,
            movie.getId(),
            parentComment.getCommentID(),
            currentUsernameSupplier,
            this::loadAndDisplayComments,
            loginViewModel,
            loginController
        );
        postReplyView.showView();
    }
    
    public void showView() {
        setVisible(true);
    }
}

