package movieapp.interface_adapter.createAccount;

public class CreateAccountViewModel {

    public static final String TITLE_LABEL = "Create Account View";
    public static final String USERNAME_LABEL = "Choose username";
    public static final String PASSWORD_LABEL = "Choose password";
    public static final String REPEAT_PASSWORD_LABEL = "Confirm password";

    public static final String SIGNUP_BUTTON_LABEL = "Create";
    public static final String CANCEL_BUTTON_LABEL = "Cancel";

    private boolean success;
    private String username;
    private String password;
    private String message;

    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public boolean isSuccess() {
        return success;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getMessage() {
        return message;
    }
}
