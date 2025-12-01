// CreateAccountOutputBoundary.java
package movieapp.use_case.createAccount;

public interface CreateAccountOutputBoundary {
    void presentSuccess(CreateAccountOutputData outputData);
    void presentPasswordMismatch(String message);
    void presentUsernameExists(String message);
    void presentValidationError(String message);
}