// LoginPresenter.java
package movieapp.interface_adapter.login;

import movieapp.use_case.login.LoginOutputBoundary;
import movieapp.use_case.login.LoginOutputData;
import movieapp.view.LoginView;

public class LoginPresenter implements LoginOutputBoundary {
    private LoginViewModel loginViewModel;

    public LoginPresenter(LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
    }
    @Override
    public void presentSuccess(LoginOutputData loginOutputData) {
        loginViewModel.setUsername(loginOutputData.getUsername());
        loginViewModel.setSuccess(loginOutputData.isSuccess());
        loginViewModel.setMessage(loginOutputData.getMessage());
    }
    
    @Override
    public void presentUserNotFound(String message) {
        loginViewModel.setSuccess(false);
        loginViewModel.setMessage(message);
        loginViewModel.setUsername(null);
    }
    
    @Override
    public void presentInvalidPassword(String message) {
        loginViewModel.setSuccess(false);
        loginViewModel.setMessage(message);
        loginViewModel.setUsername(null);
    }
    
    @Override
    public void presentValidationError(String message) {
        loginViewModel.setSuccess(false);
        loginViewModel.setMessage(message);
        loginViewModel.setUsername(null);
    }
}