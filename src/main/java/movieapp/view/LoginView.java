// LoginView.java
package movieapp.view;

import movieapp.interface_adapter.createAccount.CreateAccountViewModel;
import movieapp.interface_adapter.login.LoginController;
import movieapp.interface_adapter.createAccount.CreateAccountController;
import movieapp.interface_adapter.login.LoginViewModel;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    private final LoginController loginController;
    private final LoginViewModel loginViewModel;
    private final CreateAccountController accountController;
    private final CreateAccountViewModel createAccountViewModel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    
    public LoginView(LoginController loginController,
                     LoginViewModel loginViewModel,
                     CreateAccountController accountController,
                     CreateAccountViewModel createAccountViewModel) {
        this.loginController = loginController;
        this.loginViewModel = loginViewModel;
        this.accountController = accountController;
        this.createAccountViewModel = createAccountViewModel;
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
        
        mainPanel.add(new JLabel(LoginViewModel.USERNAME_LABEL));
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
        
        loginController.login(username, password);
        
        if (loginViewModel.isSuccess()) {
            dispose();
        } else {
            showLoginFailView(loginViewModel.getMessage());
        }
    }
    
    private void openCreateAccountView() {

        CreateAccountView createAccountView = new CreateAccountView(accountController,
                createAccountViewModel,this);
        createAccountView.setVisible(true);
        setVisible(false);
    }
    
    private void showLoginFailView(String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage, "Login Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public void showView() {
        usernameField.setText("");
        passwordField.setText("");
        setVisible(true);
    }
}