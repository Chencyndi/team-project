package usecase1test;

import movieapp.data_access.InMemoryUserDataAccessObject;
import movieapp.use_case.account.CreateAccountInputData;
import movieapp.use_case.account.CreateAccountOutputBoundary;
import movieapp.use_case.common.UserDataAccessInterface;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateAccountTest {

    @Test
    void createSuccessAccountTest() {
        CreateAccountInputData inputData = new CreateAccountInputData("Paul", "password", "password");
        // UserDAI userRepository = new In

    }
    @Test
    void createAccountAlreadyExistTest() {

    }
    @Test
    void createAccountInvalidPasswordTest() {
    }
}
