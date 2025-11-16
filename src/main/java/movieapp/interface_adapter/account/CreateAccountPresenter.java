package movieapp.interface_adapter.account;

import movieapp.use_case.account.CreateAccountOutputBoundary;
import movieapp.use_case.account.CreateAccountOutputData;

public class CreateAccountPresenter implements CreateAccountOutputBoundary {
    @Override
    public CreateAccountOutputData presentSuccess(String message, String username) {
        return new CreateAccountOutputData(true, message, username);
    }

    @Override
    public CreateAccountOutputData presentPasswordMismatch(String message) {
        return new CreateAccountOutputData(false, message, null);
    }

    @Override
    public CreateAccountOutputData presentUsernameExists(String message) {
        return new CreateAccountOutputData(false, message, null);
    }

    @Override
    public CreateAccountOutputData presentValidationError(String message) {
        return new CreateAccountOutputData(false, message, null);
    }
}