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
    public LoginOutputData execute(LoginInputData inputData) {
        try {
            // Find user by username
            User user = accountRepository.findByUsername(inputData.getUsername())
                    .orElse(null);
            
            if (user == null) {
                return outputBoundary.presentUserNotFound("User does not exist");
            }
            
            // Validate password
            if (!user.validateCredentials(inputData.getPassword())) {
                return outputBoundary.presentInvalidPassword("Invalid password");
            }
            
            return outputBoundary.presentSuccess("Login successful", user.getUsername());
            
        } catch (IllegalArgumentException e) {
            return outputBoundary.presentValidationError(e.getMessage());
        }
    }
}