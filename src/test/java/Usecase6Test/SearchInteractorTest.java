package Usecase6Test;

import data_access.InMemoryUserDAO;
import movieapp.entity.Movie;
import movieapp.entity.User;
import movieapp.interface_adapter.search.MovieRepository;
import movieapp.use_case.search.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SearchInteractorTest {

    @Test
    void searchSuccess() {
        InMemoryMovieRepository repo = new InMemoryMovieRepository();
        repo.addMovie(new Movie("Movie1", "morning", 8.0, null));
        repo.addMovie(new Movie("Movie2", "noon", 12.0, null));
        repo.addMovie(new Movie("Movie3", "night", 8.0, null));
        repo.addMovie(new Movie("Movie4", "sunrise", 0.0, null));

        SearchInputData input = new SearchInputData("Movie");

        SearchOutputBoundary presenter = new SearchOutputBoundary() {
            @Override
            public void prepareSuccessView(SearchOutputData outputData, String searchQuery) {
                assertTrue(outputData.isSuccess());
                assertEquals(3, outputData.getMovies().size());
                assertEquals("Found 3 movies", outputData.getMessage());
                assertEquals("Movie", searchQuery);
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Unexpected failure");
            }
        };

        SearchInputBoundary interactor = new SearchInteractor(repo, presenter);
        interactor.execute(input);
    }

    @Test
    void searchSuccessWith2Results() {
        InMemoryMovieRepository repo = new InMemoryMovieRepository();
        repo.addMovie(new Movie("Movie11", "morning", 8.0, null));
        repo.addMovie(new Movie("Movie12", "noon", 12.0, null));
        repo.addMovie(new Movie("Movie3", "night", 8.0, null));
        repo.addMovie(new Movie("Movie4", "sunrise", 0.0, null));

        SearchInputData input = new SearchInputData("Movie1");

        SearchOutputBoundary presenter = new SearchOutputBoundary() {
            @Override
            public void prepareSuccessView(SearchOutputData outputData, String searchQuery) {
                assertTrue(outputData.isSuccess());
                assertEquals(2, outputData.getMovies().size());
                assertEquals("Found 2 movies", outputData.getMessage());
                assertEquals("Movie1", searchQuery);
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Unexpected failure");
            }
        };

        SearchInputBoundary interactor = new SearchInteractor(repo, presenter);
        interactor.execute(input);
    }

    @Test
    void searchFail() {
        InMemoryMovieRepository repo = new InMemoryMovieRepository();

        SearchInputData input = new SearchInputData("");

        SearchOutputBoundary presenter = new SearchOutputBoundary() {
            @Override
            public void prepareSuccessView(SearchOutputData outputData, String searchQuery) {
                fail("Unexpected success");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Please enter a search term", errorMessage);
            }
        };

        SearchInputBoundary interactor = new SearchInteractor(repo, presenter);
        interactor.execute(input);
    }

    @Test
    void searchUnexpectFail() {
        InMemoryMovieRepository repo = new InMemoryMovieRepository();
        repo.addMovie(new Movie("Movie11", "morning", 8.0, null));

        SearchInputData input = new SearchInputData(""){
            @Override
            public String getSearchQuery() {
                throw new IllegalArgumentException("Validation exception");
            }
        };

        SearchOutputBoundary presenter = new SearchOutputBoundary() {
            @Override
            public void prepareSuccessView(SearchOutputData outputData, String searchQuery) {
                fail("Unexpected success");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Error: Validation exception", errorMessage);
            }
        };

        SearchInputBoundary interactor = new SearchInteractor(repo, presenter);
        interactor.execute(input);
    }
}


