// Application.java

import movieapp.data_access.DBUserDataAccessObject;
import movieapp.interface_adapter.AccountRepository;
import movieapp.interface_adapter.login.LoginController;
import movieapp.interface_adapter.login.LoginPresenter;
import movieapp.interface_adapter.account.CreateAccountController;
import movieapp.interface_adapter.account.CreateAccountPresenter;
import movieapp.use_case.login.LoginInputBoundary;
import movieapp.use_case.login.LoginInteractor;
import movieapp.use_case.account.CreateAccountInputBoundary;
import movieapp.use_case.account.CreateAccountInteractor;
import movieapp.view.CreateAccountView;
import movieapp.view.LoginView;

import javax.swing.*;

public class UseCase1Application {
    public static void main(String[] args) {
        // Dependency Injection Setup
        String baseUrl = "http://vm003.teach.cs.toronto.edu:20112";
        AccountRepository accountRepository = new DBUserDataAccessObject(baseUrl);

        CreateAccountPresenter createAccountPresenter = new CreateAccountPresenter();
        LoginPresenter loginPresenter = new LoginPresenter();

        CreateAccountInputBoundary createAccountUseCase = new CreateAccountInteractor(
                accountRepository, createAccountPresenter);

        LoginInputBoundary loginUseCase = new LoginInteractor(
                accountRepository, loginPresenter);

        CreateAccountController accountController = new CreateAccountController(createAccountUseCase);
        LoginController loginController = new LoginController(loginUseCase);

        // Start UI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView(loginController, accountController);
            CreateAccountView view = new CreateAccountView(accountController, loginView);
            loginView.setVisible(true);
        });
    }
}