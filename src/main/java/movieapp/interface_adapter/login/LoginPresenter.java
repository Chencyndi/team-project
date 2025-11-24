// LoginPresenter.java
package movieapp.interface_adapter.login;

import movieapp.use_case.login.LoginOutputBoundary;
import movieapp.use_case.login.LoginOutputData;

public class LoginPresenter implements LoginOutputBoundary {
    @Override
    public void presentSuccess(String message, String username) {
       final LoginOutputData outputdata = new LoginOutputData(true, message, username);
       LoginViewModel.SUCCESS = outputdata.isSuccess();
       LoginViewModel.MESSAGE = outputdata.getMessage();
       LoginViewModel.USERNAME = outputdata.getUsername();
    }
    
    @Override
    public void presentUserNotFound(String message) {
        final LoginOutputData outputdata = new LoginOutputData(false, message, null);
        LoginViewModel.SUCCESS = outputdata.isSuccess();
        LoginViewModel.MESSAGE = outputdata.getMessage();
        LoginViewModel.USERNAME = outputdata.getUsername();
    }
    
    @Override
    public void presentInvalidPassword(String message) {
        final LoginOutputData outputdata = new LoginOutputData(false, message, null);
        LoginViewModel.SUCCESS = outputdata.isSuccess();
        LoginViewModel.MESSAGE = outputdata.getMessage();
        LoginViewModel.USERNAME = outputdata.getUsername();
    }
    
    @Override
    public void presentValidationError(String message) {
        final LoginOutputData outputdata = new LoginOutputData(false, message, null);
        LoginViewModel.SUCCESS = outputdata.isSuccess();
        LoginViewModel.MESSAGE = outputdata.getMessage();
        LoginViewModel.USERNAME = outputdata.getUsername();
    }
}