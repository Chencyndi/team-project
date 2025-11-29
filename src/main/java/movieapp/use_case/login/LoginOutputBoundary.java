// LoginOutputBoundary.java
package movieapp.use_case.login;

public interface LoginOutputBoundary {
    void presentSuccess(LoginOutputData loginOutputData);
    void presentUserNotFound(String message);
    void presentInvalidPassword(String message);
    void presentValidationError(String message);
}