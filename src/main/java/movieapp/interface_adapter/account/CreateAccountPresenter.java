package movieapp.interface_adapter.account;

import movieapp.use_case.account.CreateAccountOutputBoundary;
import movieapp.use_case.account.CreateAccountOutputData;
import movieapp.view.CreateAccountView;

public class CreateAccountPresenter implements CreateAccountOutputBoundary {

    private final CreateAccountViewModel viewModel;

    public CreateAccountPresenter(CreateAccountViewModel viewModel) {
        this.viewModel = viewModel;
    }
    @Override
    public void presentSuccess(CreateAccountOutputData outputData) {
        viewModel.setSuccess(outputData.isSuccess());
        viewModel.setMessage(outputData.getMessage());
        viewModel.setUsername(outputData.getUsername());

    }

    @Override
    public void presentPasswordMismatch(String message) {
        viewModel.setSuccess(false);
        viewModel.setMessage(message);
        viewModel.setUsername(null);
    }

    @Override
    public void presentUsernameExists(String message) {
        viewModel.setSuccess(false);
        viewModel.setMessage(message);
        viewModel.setUsername(null);
    }

    @Override
    public void presentValidationError(String message) {
        viewModel.setSuccess(false);
        viewModel.setMessage(message);
        viewModel.setUsername(null);
    }

}