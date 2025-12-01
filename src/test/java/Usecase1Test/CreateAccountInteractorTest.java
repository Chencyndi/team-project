package Usecase1Test;

import movieapp.use_case.account.*;
import org.junit.jupiter.api.Test;

public class CreateAccountInteractorTest {

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