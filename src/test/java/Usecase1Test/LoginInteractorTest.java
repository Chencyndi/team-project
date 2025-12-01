package Usecase1Test;

import data_access.InMemoryUserDAO;
import movieapp.use_case.login.*;
import movieapp.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginInteractorTest {

    @Test
    void loginSuccessTest() {
        InMemoryUserDAO repo = new InMemoryUserDAO();
        repo.addUser(new User("Paul", "password"));

        LoginInputData input = new LoginInputData("Paul", "password");

        LoginOutputBoundary presenter = new LoginOutputBoundary() {
            @Override
            public void presentSuccess(LoginOutputData loginOutputData) {
                assertTrue(loginOutputData.isSuccess());
                assertEquals("Paul", loginOutputData.getUsername());
            }

            @Override
            public void presentUserNotFound(String message) {
                fail("Unexpected: user should exist");
            }

            @Override
            public void presentInvalidPassword(String message) {
                fail("Unexpected: password should match");
            }

            @Override
            public void presentValidationError(String message) {
                fail("Unexpected validation error");
            }
        };

        LoginInputBoundary interactor = new LoginInteractor(repo, presenter);
        interactor.execute(input);
    }

    @Test
    void loginUserNotExistFailTest() {
        InMemoryUserDAO repo = new InMemoryUserDAO();
        LoginInputData input = new LoginInputData("Paul", "password");

        LoginOutputBoundary presenter = new LoginOutputBoundary() {
            @Override
            public void presentSuccess(LoginOutputData loginOutputData) {
                fail("Unexpected: should not succeed");
            }

            @Override
            public void presentUserNotFound(String message) {
                assertEquals("User does not exist", message);
            }

            @Override
            public void presentInvalidPassword(String message) {
                fail("Unexpected: user does not exist");
            }

            @Override
            public void presentValidationError(String message) {
                fail("Unexpected validation error");
            }
        };

        LoginInputBoundary interactor = new LoginInteractor(repo, presenter);
        interactor.execute(input);
    }

    @Test
    void loginPasswordIncorrectTest() {
        InMemoryUserDAO repo = new InMemoryUserDAO();
        repo.addUser(new User("Paul", "password"));

        LoginInputData input = new LoginInputData("Paul", "wrong");

        LoginOutputBoundary presenter = new LoginOutputBoundary() {
            @Override
            public void presentSuccess(LoginOutputData loginOutputData) {
                fail("Unexpected: incorrect password");
            }

            @Override
            public void presentUserNotFound(String message) {
                fail("Unexpected: user exists");
            }

            @Override
            public void presentInvalidPassword(String message) {
                assertEquals("Invalid password", message);
            }

            @Override
            public void presentValidationError(String message) {
                fail("Unexpected validation error");
            }
        };

        LoginInputBoundary interactor = new LoginInteractor(repo, presenter);
        interactor.execute(input);
    }
    @Test
    void loginValidationErrorTest() {
        InMemoryUserDAO repo = new InMemoryUserDAO();

        User invalidUser = new User("Paul", "password") {
            @Override
            public boolean validateCredentials(String password) {
                throw new IllegalArgumentException("Validation exception");
            }
        };
        repo.addUser(invalidUser);

        LoginInputData input = new LoginInputData("Paul", "password");

        LoginOutputBoundary presenter = new LoginOutputBoundary() {
            @Override
            public void presentSuccess(LoginOutputData loginOutputData) {
                fail("Unexpected: should not succeed");
            }

            @Override
            public void presentUserNotFound(String message) {
                fail("Unexpected: user exists");
            }

            @Override
            public void presentInvalidPassword(String message) {
                fail("Unexpected: validation error expected");
            }

            @Override
            public void presentValidationError(String message) {
                assertEquals("Validation exception", message);
            }
        };

        LoginInputBoundary interactor = new LoginInteractor(repo, presenter);
        interactor.execute(input);
    }

}
