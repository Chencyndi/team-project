// Application.java

import movieapp.data_access.UserDataAccessObject;
import movieapp.interface_adapter.createAccount.CreateAccountViewModel;
import movieapp.interface_adapter.login.LoginController;
import movieapp.interface_adapter.login.LoginPresenter;
import movieapp.interface_adapter.createAccount.CreateAccountController;
import movieapp.interface_adapter.createAccount.CreateAccountPresenter;
import movieapp.interface_adapter.login.LoginViewModel;
import movieapp.use_case.login.LoginInputBoundary;
import movieapp.use_case.login.LoginInteractor;
import movieapp.use_case.createAccount.CreateAccountInputBoundary;
import movieapp.use_case.createAccount.CreateAccountInteractor;
import movieapp.view.CreateAccountView;
import movieapp.view.LoginView;
import movieapp.use_case.common.UserDataAccessInterface;

import javax.swing.*;

public class UseCase1Application {
    public static void main(String[] args) {
        // Dependency Injection Setup
        String baseUrl = "http://vm003.teach.cs.toronto.edu:20112";
        UserDataAccessInterface accountRepository = new UserDataAccessObject();

        CreateAccountViewModel createAccountViewModel = new CreateAccountViewModel();
        LoginViewModel loginViewModel = new LoginViewModel();

        CreateAccountPresenter createAccountPresenter = new CreateAccountPresenter(createAccountViewModel);
        LoginPresenter loginPresenter = new LoginPresenter(loginViewModel);

        CreateAccountInputBoundary createAccountUseCase = new CreateAccountInteractor(
                accountRepository, createAccountPresenter);

        LoginInputBoundary loginUseCase = new LoginInteractor(
                accountRepository, loginPresenter);

        CreateAccountController createAccountController = new CreateAccountController(createAccountUseCase);
        LoginController loginController = new LoginController(loginUseCase);

        // Start UI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView(loginController, loginViewModel, createAccountController, createAccountViewModel);
            CreateAccountView view = new CreateAccountView(createAccountController, createAccountViewModel, loginView);
            loginView.setVisible(true);
        });
    }
}