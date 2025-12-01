package Usecase1Test;

import data_access.InMemoryUserDAO;
import movieapp.entity.User;
import movieapp.use_case.createAccount.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;;

class CreateAccountInteractorTest {

    @Test
    void createSuccessAccountTest() {
        CreateAccountInputData inputData =
                new CreateAccountInputData("Paul", "password", "password");

        InMemoryUserDAO userRepository = new InMemoryUserDAO();

        CreateAccountOutputBoundary successPresenter = new CreateAccountOutputBoundary() {
            @Override
            public void presentSuccess(CreateAccountOutputData outputData) {
                assertTrue(outputData.isSuccess());
                assertEquals("Paul", outputData.getUsername());
                assertTrue(userRepository.existsByUsername("Paul"));
            }

            @Override
            public void presentPasswordMismatch(String message) {
                fail("Unexpected mismatch");
            }

            @Override
            public void presentUsernameExists(String message) {
                fail("Unexpected username exists");
            }

            @Override
            public void presentValidationError(String message) {
                fail("Unexpected validation error");
            }
        };

        CreateAccountInputBoundary interactor =
                new CreateAccountInteractor(userRepository, successPresenter);
        interactor.execute(inputData);
    }

    @Test
    void createAccountAlreadyExistTest() {
        CreateAccountInputData inputData =
                new CreateAccountInputData("Paul", "password", "password");

        InMemoryUserDAO userRepository = new InMemoryUserDAO();
        userRepository.addUser(new movieapp.entity.User("Paul", "password"));

        CreateAccountOutputBoundary failurePresenter = new CreateAccountOutputBoundary() {
            @Override
            public void presentSuccess(CreateAccountOutputData outputData) {
                fail("Unexpected success");
            }

            @Override
            public void presentPasswordMismatch(String message) {
                fail("Unexpected mismatch");
            }

            @Override
            public void presentUsernameExists(String message) {
                assertEquals("Username already exists", message);
            }

            @Override
            public void presentValidationError(String message) {
                fail("Unexpected validation error");
            }
        };

        CreateAccountInputBoundary interactor =
                new CreateAccountInteractor(userRepository, failurePresenter);
        interactor.execute(inputData);
    }

    @Test
    void createAccountInvalidPasswordTest() {
        CreateAccountInputData inputData =
                new CreateAccountInputData("Paul", "password1", "password2");

        InMemoryUserDAO userRepository = new InMemoryUserDAO();

        CreateAccountOutputBoundary failurePresenter = new CreateAccountOutputBoundary() {
            @Override
            public void presentSuccess(CreateAccountOutputData outputData) {
                fail("Unexpected success");
            }

            @Override
            public void presentPasswordMismatch(String message) {
                assertEquals("Password and confirmed password do not match", message);
            }

            @Override
            public void presentUsernameExists(String message) {
                fail("Unexpected username exists");
            }

            @Override
            public void presentValidationError(String message) {
                fail("Unexpected validation error");
            }
        };

        CreateAccountInputBoundary interactor =
                new CreateAccountInteractor(userRepository, failurePresenter);
        interactor.execute(inputData);
    }
    @Test
    void createAccountValidationErrorTest() {
        InMemoryUserDAO repo = new InMemoryUserDAO() {
            @Override
            public void addUser(User user) {
                throw new IllegalArgumentException("Validation exception");
            }
        };

        CreateAccountInputData inputData =
                new CreateAccountInputData("Paul", "password", "password");

        CreateAccountOutputBoundary presenter = new CreateAccountOutputBoundary() {
            @Override
            public void presentSuccess(CreateAccountOutputData outputData) {
                fail("Unexpected success");
            }

            @Override
            public void presentPasswordMismatch(String message) {
                fail("Unexpected password mismatch");
            }

            @Override
            public void presentUsernameExists(String message) {
                fail("Unexpected username exists");
            }

            @Override
            public void presentValidationError(String message) {
                assertEquals("Validation exception", message);
            }
        };

        CreateAccountInputBoundary interactor =
                new CreateAccountInteractor(repo, presenter);
        interactor.execute(inputData);
    }
}
