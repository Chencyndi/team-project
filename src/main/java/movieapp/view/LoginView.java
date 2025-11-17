// LoginView.java
package movieapp.view;

import movieapp.interface_adapter.login.LoginController;
import movieapp.interface_adapter.account.CreateAccountController;
import movieapp.use_case.login.LoginOutputData;
import movieapp.interface_adapter.login.LoginViewModel;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    private final LoginController loginController;
    private final CreateAccountController accountController;
    private JTextField usernameField;
    private JPasswordField passwordField;
    
    public LoginView(LoginController loginController, CreateAccountController accountController) {
        this.loginController = loginController;
        this.accountController = accountController;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        mainPanel.add(new JLabel(LoginViewModel.USERNAME));
        usernameField = new JTextField();
        mainPanel.add(usernameField);
        
        mainPanel.add(new JLabel(LoginViewModel.PASSWORD));
        passwordField = new JPasswordField();
        mainPanel.add(passwordField);
        
        JButton loginButton = new JButton(LoginViewModel.LOGIN_BUTTON);
        loginButton.addActionListener(e -> handleLogin());
        
        JButton createAccountButton = new JButton(LoginViewModel.CREATE_BUTTON);
        createAccountButton.addActionListener(e -> openCreateAccountView());
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.add(loginButton);
        buttonPanel.add(createAccountButton);
        
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        LoginOutputData result = loginController.login(username, password);
        
        if (result.isSuccess()) {
            openAccountView(result.getUsername());
            dispose();
        } else {
            showUserNotExistView(result.getMessage());
        }
    }
    
    private void openCreateAccountView() {

        CreateAccountView createAccountView = new CreateAccountView(accountController, this);
        createAccountView.setVisible(true);
        setVisible(false);
    }
    
    private void openAccountView(String username) {
        AccountView accountView = new AccountView(accountController, username, this);
        accountView.setVisible(true);
    }
    
    private void showUserNotExistView(String errorMessage) {
        UserNotExistView userNotExistView = new UserNotExistView(this, errorMessage);
        userNotExistView.setVisible(true);
    }
    
    public void showView() {
        usernameField.setText("");
        passwordField.setText("");
        setVisible(true);
    }
}