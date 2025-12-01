// CreateAccountInputData.java
package movieapp.use_case.createAccount;

public class CreateAccountInputData {
    private final String username;
    private final String password;
    private final String confirmedPassword;
    
    public CreateAccountInputData(String username, String password, String confirmedPassword) {
        this.username = username;
        this.password = password;
        this.confirmedPassword = confirmedPassword;
    }
    
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getConfirmedPassword() { return confirmedPassword; }
}