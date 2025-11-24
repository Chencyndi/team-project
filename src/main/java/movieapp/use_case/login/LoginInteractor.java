// LoginInteractor.java
package movieapp.use_case.login;


import movieapp.entity.User;
import movieapp.interface_adapter.AccountRepository;

public class LoginInteractor implements LoginInputBoundary {
    private final AccountRepository accountRepository;
    private final LoginOutputBoundary outputBoundary;
    
    public LoginInteractor(AccountRepository accountRepository, LoginOutputBoundary outputBoundary) {
        this.accountRepository = accountRepository;
        this.outputBoundary = outputBoundary;
    }
    
    @Override
    public void execute(LoginInputData inputData) {
        try {
            // Find user by username
            User user = accountRepository.findByUsername(inputData.getUsername())
                    .orElse(null);
            
            if (user == null) {
                outputBoundary.presentUserNotFound("User does not exist");
            }
            
            // Validate password
            if (!user.validateCredentials(inputData.getPassword())) {
                outputBoundary.presentInvalidPassword("Invalid password");
            }
            
            outputBoundary.presentSuccess("Login successful", user.getUsername());
            
        } catch (IllegalArgumentException e) {
            outputBoundary.presentValidationError(e.getMessage());
        }
    }
}