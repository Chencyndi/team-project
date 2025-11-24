// LoginOutputBoundary.java
package movieapp.use_case.login;

public interface LoginOutputBoundary {
    void presentSuccess(String message, String username);
    void presentUserNotFound(String message);
    void presentInvalidPassword(String message);
    void presentValidationError(String message);
}