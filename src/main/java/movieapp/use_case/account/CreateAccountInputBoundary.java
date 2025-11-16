// CreateAccountInputBoundary.java
package movieapp.use_case.account;

public interface CreateAccountInputBoundary {
    CreateAccountOutputData execute(CreateAccountInputData inputData);
}