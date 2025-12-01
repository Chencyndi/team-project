// CreateAccountInteractor.java
package movieapp.use_case.createAccount;

import movieapp.entity.User;
import movieapp.use_case.common.UserDataAccessInterface;

public class CreateAccountInteractor implements CreateAccountInputBoundary {
    private final UserDataAccessInterface accountRepository;
    private final CreateAccountOutputBoundary outputBoundary;
    
    public CreateAccountInteractor(UserDataAccessInterface accountRepository,
                                  CreateAccountOutputBoundary outputBoundary) {
        this.accountRepository = accountRepository;
        this.outputBoundary = outputBoundary;
    }
    
    @Override
    public void execute(CreateAccountInputData inputData) {
        try {
            // Validate passwords match
            if (!inputData.getPassword().equals(inputData.getConfirmedPassword())) {
                outputBoundary.presentPasswordMismatch("Password and confirmed password do not match");
                return;
            }
            // Check if username exists
            if (accountRepository.existsByUsername(inputData.getUsername())) {
                outputBoundary.presentUsernameExists("Username already exists");
                return;
            }
            // Create and save user
            User user = new User(inputData.getUsername(), inputData.getPassword());
            accountRepository.addUser(user);
            CreateAccountOutputData outputData = new CreateAccountOutputData(
                    true, "Success", inputData.getUsername());
            outputBoundary.presentSuccess(outputData);
            
        } catch (IllegalArgumentException e) {
            outputBoundary.presentValidationError(e.getMessage());
        }
    }
}