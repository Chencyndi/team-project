// LoginInputBoundary.java
package movieapp.use_case.login;

public interface LoginInputBoundary {
    LoginOutputData execute(LoginInputData inputData);
}