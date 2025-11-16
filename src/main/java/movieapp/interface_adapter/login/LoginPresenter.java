// LoginPresenter.java
package movieapp.interface_adapter.login;

import movieapp.use_case.login.LoginOutputBoundary;
import movieapp.use_case.login.LoginOutputData;

public class LoginPresenter implements LoginOutputBoundary {
    @Override
    public LoginOutputData presentSuccess(String message, String username) {
        return new LoginOutputData(true, message, username);
    }
    
    @Override
    public LoginOutputData presentUserNotFound(String message) {
        return new LoginOutputData(false, message, null);
    }
    
    @Override
    public LoginOutputData presentInvalidPassword(String message) {
        return new LoginOutputData(false, message, null);
    }
    
    @Override
    public LoginOutputData presentValidationError(String message) {
        return new LoginOutputData(false, message, null);
    }
}