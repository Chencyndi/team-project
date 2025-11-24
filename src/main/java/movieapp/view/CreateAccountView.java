// CreateAccountView.java
package movieapp.view;

import movieapp.interface_adapter.account.CreateAccountController;
import movieapp.use_case.account.CreateAccountOutputData;
import movieapp.interface_adapter.account.CreateAccountViewModel;

import javax.swing.*;
import java.awt.*;

public class CreateAccountView extends JFrame {
    private final CreateAccountController accountController;
    private final LoginView loginView;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    
    public CreateAccountView(CreateAccountController accountController, LoginView loginView) {
        this.accountController = accountController;
        this.loginView = loginView;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle(CreateAccountViewModel.TITLE_LABEL);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(350, 250);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        mainPanel.add(new JLabel(CreateAccountViewModel.USERNAME_LABEL));
        usernameField = new JTextField();
        mainPanel.add(usernameField);
        
        mainPanel.add(new JLabel(CreateAccountViewModel.PASSWORD_LABEL));
        passwordField = new JPasswordField();
        mainPanel.add(passwordField);
        
        mainPanel.add(new JLabel(CreateAccountViewModel.REPEAT_PASSWORD_LABEL));
        confirmPasswordField = new JPasswordField();
        mainPanel.add(confirmPasswordField);
        
        JButton createButton = new JButton(CreateAccountViewModel.SIGNUP_BUTTON_LABEL);
        createButton.addActionListener(e -> handleCreateAccount());
        
        JButton backButton = new JButton(CreateAccountViewModel.CANCEL_BUTTON_LABEL);
        backButton.addActionListener(e -> goBackToLogin());
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.add(createButton);
        buttonPanel.add(backButton);
        
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void handleCreateAccount() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmedPassword = new String(confirmPasswordField.getPassword());
        
        CreateAccountOutputData result = accountController.createAccount(username, password, confirmedPassword);
        
        if (result.isSuccess()) {
            JOptionPane.showMessageDialog(this, result.getMessage(), "Success", JOptionPane.INFORMATION_MESSAGE);
            goBackToLogin();
        } else {
            showPasswordNotSameView(result.getMessage());
        }
    }
    
    private void showPasswordNotSameView(String errorMessage) {
        PasswordNotSameView passwordNotSameView = new PasswordNotSameView(this, errorMessage);
        passwordNotSameView.setVisible(true);
    }
    
    private void goBackToLogin() {
        loginView.showView();
        dispose();
    }
}