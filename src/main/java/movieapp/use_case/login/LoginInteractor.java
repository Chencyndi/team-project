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
    public void execute(LoginInputData inputData) {
        try {
            // Find user by username
            User user = accountRepository.findByUsername(inputData.getUsername());
            
            if (user == null) {
                outputBoundary.presentUserNotFound("User does not exist");
                return;
            }
            
            // Validate password
            if (!user.validateCredentials(inputData.getPassword())) {
                outputBoundary.presentInvalidPassword("Invalid password");
                return;
            }
            LoginOutputData outputData = new LoginOutputData(true,
                    "Success", inputData.getUsername());
            outputBoundary.presentSuccess(outputData);
            
        } catch (IllegalArgumentException e) {
            outputBoundary.presentValidationError(e.getMessage());
        }
    }
}