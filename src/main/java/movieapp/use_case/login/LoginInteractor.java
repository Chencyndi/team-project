// LoginInteractor.java
package movieapp.use_case.login;


import movieapp.entity.User;
import movieapp.use_case.common.UserDataAccessInterface;

public class LoginInteractor implements LoginInputBoundary {
    private final UserDataAccessInterface accountRepository;
    private final LoginOutputBoundary outputBoundary;
    
    public LoginInteractor(UserDataAccessInterface accountRepository, LoginOutputBoundary outputBoundary) {
        this.accountRepository = accountRepository;
        this.outputBoundary = outputBoundary;
    }
    
    @Override
    public LoginOutputData execute(LoginInputData inputData) {
        try {
            // Find user by username
            User user = accountRepository.findByUsername(inputData.getUsername());
            
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