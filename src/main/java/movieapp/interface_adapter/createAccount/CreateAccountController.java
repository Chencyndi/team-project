package movieapp.interface_adapter.createAccount;

import movieapp.use_case.createAccount.CreateAccountInputBoundary;
import movieapp.use_case.createAccount.CreateAccountInputData;

public class CreateAccountController {
    private final CreateAccountInputBoundary createAccountUseCase;

    public CreateAccountController(CreateAccountInputBoundary createAccountUseCase) {
        this.createAccountUseCase = createAccountUseCase;
    }

    public void createAccount(String username, String password, String confirmedPassword) {
        CreateAccountInputData inputData = new CreateAccountInputData(username, password, confirmedPassword);
        createAccountUseCase.execute(inputData);
    }

}