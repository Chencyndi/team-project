package movieapp.interface_adapter.account;

import movieapp.use_case.account.CreateAccountInputBoundary;
import movieapp.use_case.account.CreateAccountInputData;
import movieapp.use_case.account.CreateAccountOutputData;

public class CreateAccountController {
    private final CreateAccountInputBoundary createAccountUseCase;

    public CreateAccountController(CreateAccountInputBoundary createAccountUseCase) {
        this.createAccountUseCase = createAccountUseCase;
    }

    public CreateAccountOutputData createAccount(String username, String password, String confirmedPassword) {
        CreateAccountInputData inputData = new CreateAccountInputData(username, password, confirmedPassword);
        return createAccountUseCase.execute(inputData);
    }

}