// CreateAccountInteractor.java
package movieapp.use_case.account;

import movieapp.entity.User;
import movieapp.entity.Watchlist;
import movieapp.interface_adapter.AccountRepository;

public class CreateAccountInteractor implements CreateAccountInputBoundary {
    private final AccountRepository accountRepository;
    private final CreateAccountOutputBoundary outputBoundary;
    
    public CreateAccountInteractor(AccountRepository accountRepository, 
                                  CreateAccountOutputBoundary outputBoundary) {
        this.accountRepository = accountRepository;
        this.outputBoundary = outputBoundary;
    }
    
    @Override
    public CreateAccountOutputData execute(CreateAccountInputData inputData) {
        try {
            // Validate passwords match
            if (!inputData.getPassword().equals(inputData.getConfirmedPassword())) {
                return outputBoundary.presentPasswordMismatch("Password and confirmed password do not match");
            }
            
            // Check if username exists
            if (accountRepository.existsByUsername(inputData.getUsername())) {
                return outputBoundary.presentUsernameExists("Username already exists");
            }
            
            // Create and save user
            Watchlist watchlist = new Watchlist();
            User user = new User(inputData.getUsername(), inputData.getPassword());
            accountRepository.save(user);
            
            return outputBoundary.presentSuccess("Account created successfully", user.getUsername());
            
        } catch (IllegalArgumentException e) {
            return outputBoundary.presentValidationError(e.getMessage());
        }
    }
}