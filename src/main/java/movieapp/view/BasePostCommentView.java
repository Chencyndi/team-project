package movieapp.view;

import movieapp.interface_adapter.comment.PostCommentController;
import movieapp.interface_adapter.comment.PostCommentViewModel;
import movieapp.interface_adapter.login.LoginController;
import movieapp.use_case.comment.PostCommentOutputData;
import movieapp.use_case.login.LoginOutputData;

import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;

/**
 * Base class for PostCommentView and PostReplyView to reduce code duplication.
 */
public abstract class BasePostCommentView extends JDialog {
    protected final PostCommentController postCommentController;
    protected final PostCommentViewModel viewModel;
    protected final Integer movieID;
    protected final Supplier<String> currentUsernameSupplier;
    protected final Runnable onPostedCallback;
    protected final LoginController loginController;
    protected JTextArea textArea;
    protected JLabel errorLabel;

    protected BasePostCommentView(JFrame parent,
                                   PostCommentController postCommentController,
                                   PostCommentViewModel viewModel,
                                   Integer movieID,
                                   Supplier<String> currentUsernameSupplier,
                                   Runnable onPostedCallback,
                                   LoginController loginController) {
        super(parent, "", true);
        this.postCommentController = postCommentController;
        this.viewModel = viewModel;
        this.movieID = movieID;
        this.currentUsernameSupplier = currentUsernameSupplier;
        this.onPostedCallback = onPostedCallback;
        this.loginController = loginController;
    }

    @Override
    public abstract String getTitle();
    protected abstract String getTextLabel();
    protected abstract String getPostButtonLabel();
    protected abstract String getEmptyErrorMessage();
    protected abstract String getLoginRequiredMessage();
    protected abstract String getSuccessMessage();
    protected abstract String getErrorDialogTitle();
    protected abstract String getErrorDialogMessage();
    protected abstract PostCommentOutputData executePost(String username, String text);
    protected abstract PostCommentOutputData executeRetry(String username, String text);

    protected void initializeView(int width, int height, int textAreaRows, int textAreaCols, boolean resizable) {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(width, height);
        setLocationRelativeTo(getParent());
        setResizable(resizable);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Text area
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel(getTextLabel()), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        textArea = new JTextArea(textAreaRows, textAreaCols);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        formPanel.add(scrollPane, gbc);

        // Error label
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        formPanel.add(errorLabel, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton postButton = new JButton(getPostButtonLabel());
        JButton cancelButton = new JButton(PostCommentViewModel.CANCEL_BUTTON_LABEL);

        postButton.addActionListener(e -> handlePost());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(postButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    protected void handlePost() {
        String text = textArea.getText().trim();

        // CHECK EMPTY
        if (text.isEmpty()) {
            errorLabel.setText(getEmptyErrorMessage());
            return;
        }

        // CHECK LOGIN
        String currentUsername = currentUsernameSupplier != null ? currentUsernameSupplier.get() : null;
        if (currentUsername == null || currentUsername.isEmpty()) {
            openLoginWindow();
            return;
        }

        // Try to post
        try {
            PostCommentOutputData result = executePost(currentUsername, text);

            if (result.getCommentId() != null) {
                JOptionPane.showMessageDialog(this,
                        getSuccessMessage(),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                if (onPostedCallback != null) {
                    onPostedCallback.run();
                }
                dispose();
            } else {
                showErrorWindow(text);
            }
        } catch (Exception e) {
            showErrorWindow(text);
        }
    }

    protected void openLoginWindow() {
        if (loginController != null) {
            // Create a simple login dialog that updates the username on success
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            LoginDialog loginDialog = new LoginDialog(parentFrame, loginController);
            loginDialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                    getLoginRequiredMessage(),
                    "Log In Required",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // Simple login dialog that updates username on success
    private static class LoginDialog extends JDialog {
        private final LoginController loginController;
        private JTextField usernameField;
        private JPasswordField passwordField;
        
        public LoginDialog(JFrame parent, LoginController loginController) {
            super(parent, "Log In", true);
            this.loginController = loginController;
            initializeDialog();
        }
        
        private void initializeDialog() {
            setSize(300, 150);
            setLocationRelativeTo(getParent());
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            
            JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            
            panel.add(new JLabel("Username:"));
            usernameField = new JTextField();
            panel.add(usernameField);
            
            panel.add(new JLabel("Password:"));
            passwordField = new JPasswordField();
            panel.add(passwordField);
            
            JButton loginButton = new JButton("Login");
            loginButton.addActionListener(e -> handleLogin());
            
            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(e -> dispose());
            
            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.add(loginButton);
            buttonPanel.add(cancelButton);
            
            add(panel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }
        
        private void handleLogin() {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            
            LoginOutputData result = loginController.login(username, password);
            
            if (result.isSuccess()) {
                // Update the current username in UseCase4Application
                try {
                    java.lang.reflect.Method setMethod = Class.forName("UseCase4Application")
                        .getMethod("setCurrentUsername", String.class);
                    setMethod.invoke(null, result.getUsername());
                } catch (Exception e) {
                    // If reflection fails, just continue
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        result.getMessage(),
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    protected void showErrorWindow(String textToRetry) {
        JDialog errorDialog = new JDialog(this, getErrorDialogTitle(), true);
        errorDialog.setSize(350, 150);
        errorDialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel messageLabel = new JLabel("<html><center>" + getErrorDialogMessage() +
                "<br>Please check your connection and try again.</center></html>");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(messageLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton retryButton = new JButton("Retry");
        JButton cancelButton = new JButton("Cancel");

        retryButton.addActionListener(e -> {
            errorDialog.dispose();
            retryPost(textToRetry);
        });

        cancelButton.addActionListener(e -> errorDialog.dispose());

        buttonPanel.add(retryButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        errorDialog.add(panel);
        errorDialog.setVisible(true);
    }

    protected void retryPost(String text) {
        String currentUsername = currentUsernameSupplier != null ? currentUsernameSupplier.get() : null;
        if (currentUsername == null || currentUsername.isEmpty()) {
            openLoginWindow();
            return;
        }

        try {
            PostCommentOutputData result = executeRetry(currentUsername, text);

            if (result.getCommentId() != null) {
                JOptionPane.showMessageDialog(this,
                        getSuccessMessage(),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                if (onPostedCallback != null) {
                    onPostedCallback.run();
                }

                dispose();
            } else {
                showErrorWindow(text);
            }
        } catch (Exception e) {
            showErrorWindow(text);
        }
    }

    public void showView() {
        textArea.setText("");
        errorLabel.setText("");
        setVisible(true);
    }
}

