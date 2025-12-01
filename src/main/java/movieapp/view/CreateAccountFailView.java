// PasswordNotSameView.java
package movieapp.view;

import javax.swing.*;
import java.awt.*;

public class CreateAccountFailView extends JFrame {
    private final CreateAccountView createAccountView;
    
    public CreateAccountFailView(CreateAccountView createAccountView, String errorMessage) {
        this.createAccountView = createAccountView;
        initializeUI(errorMessage);
    }
    
    private void initializeUI(String errorMessage) {
        setTitle("Fail");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel messageLabel = new JLabel("<html><center>" + errorMessage + "</center></html>", SwingConstants.CENTER);
        mainPanel.add(messageLabel, BorderLayout.CENTER);
        
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> goBackToCreateAccount());
        
        add(mainPanel, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);
    }
    
    private void goBackToCreateAccount() {
        createAccountView.setVisible(true);
        dispose();
    }
}