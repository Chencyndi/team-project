// LoginOutputBoundary.java
package movieapp.use_case.login;

public interface LoginOutputBoundary {
    LoginOutputData presentSuccess(String message, String username);
    LoginOutputData presentUserNotFound(String message);
    LoginOutputData presentInvalidPassword(String message);
    LoginOutputData presentValidationError(String message);
}