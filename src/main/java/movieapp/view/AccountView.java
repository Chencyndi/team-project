// AccountView.java
package movieapp.view;

import movieapp.interface_adapter.account.CreateAccountController;

import javax.swing.*;
import java.awt.*;

public class AccountView extends JFrame {
    private final CreateAccountController accountController;
    private final String username;
    private final LoginView loginView;
    
    public AccountView(CreateAccountController accountController, String username, LoginView loginView) {
        this.accountController = accountController;
        this.username = username;
        this.loginView = loginView;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Account - " + username);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!", SwingConstants.CENTER);
        mainPanel.add(welcomeLabel, BorderLayout.CENTER);
        
        JButton logoutButton = new JButton("Log Out");
        logoutButton.addActionListener(e -> handleLogout());
        
        add(mainPanel, BorderLayout.CENTER);
        add(logoutButton, BorderLayout.SOUTH);
    }
    
    private void handleLogout() {
        loginView.showView();
        dispose();
    }
}