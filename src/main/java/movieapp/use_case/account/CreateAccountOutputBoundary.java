// CreateAccountOutputBoundary.java
package movieapp.use_case.account;

public interface CreateAccountOutputBoundary {
    CreateAccountOutputData presentSuccess(String message, String username);
    CreateAccountOutputData presentPasswordMismatch(String message);
    CreateAccountOutputData presentUsernameExists(String message);
    CreateAccountOutputData presentValidationError(String message);
}