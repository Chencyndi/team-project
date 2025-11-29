package movieapp.interface_adapter.login;

import movieapp.use_case.login.LoginInputBoundary;
import movieapp.use_case.login.LoginInputData;
import movieapp.use_case.login.LoginOutputBoundary;
import movieapp.use_case.login.LoginOutputData;

/**
 * The controller for the Login Use Case.
 */
public class LoginController {
    private final LoginInputBoundary loginUseCase;

    public LoginController(LoginInputBoundary loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    public void login(String username, String password) {
        LoginInputData inputData = new LoginInputData(username, password);
        loginUseCase.execute(inputData);
    }
}
