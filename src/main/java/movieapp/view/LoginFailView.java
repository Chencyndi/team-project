// UserNotExistView.java
package movieapp.view;

import javax.swing.*;
import java.awt.*;

public class LoginFailView extends JFrame {
    private final LoginView loginView;
    
    public LoginFailView(LoginView loginView, String errorMessage) {
        this.loginView = loginView;
        initializeUI(errorMessage);
    }
    
    private void initializeUI(String errorMessage) {
        setTitle("Login Error");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel messageLabel = new JLabel("<html><center>" + errorMessage + "</center></html>", SwingConstants.CENTER);
        mainPanel.add(messageLabel, BorderLayout.CENTER);
        
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> goBackToLogin());
        
        add(mainPanel, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);
    }
    
    private void goBackToLogin() {
        loginView.setVisible(true);
        dispose();
    }
}