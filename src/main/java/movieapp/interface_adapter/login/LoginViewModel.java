package movieapp.interface_adapter.login;

import movieapp.entity.User;
import movieapp.interface_adapter.ViewModel;

import java.util.ArrayList;

/**
 * The View Model for the Login View.
 */
public class LoginViewModel {

    public static final String USERNAME_LABEL = "Username:";
    public static final String PASSWORD = "Password:";
    public static final String LOGIN_BUTTON = "Login";
    public static final String CREATE_BUTTON = "Create Account";
    private boolean success;
    private String message;
    private String username;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }

}