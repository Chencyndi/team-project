package Usecase6Test;

import movieapp.entity.Movie;
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
        repo.addMovie(new Movie("Movie2","noon", 12.0, null));
        repo.addMovie(new Movie("Movie3","night", 8.0, null));
        repo.addMovie(new Movie("Movie4","sunrise",0.0,null));

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
        repo.addMovie(new Movie("Movie12","noon", 12.0, null));
        repo.addMovie(new Movie("Movie3","night", 8.0, null));
        repo.addMovie(new Movie("Movie4","sunrise",0.0,null));

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

    private static class InMemoryMovieRepository implements MovieRepository {
        private final List<Movie> movies = new ArrayList<>();

        public void addMovie(Movie movie) {
            movies.add(movie);
        }

        @Override
        public List<Movie> searchMovies(String query) {
            List<Movie> result = new ArrayList<>();
            for (Movie m : movies) {
                if (m.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    result.add(m);
                }
            }
            return result;
        }
    }

}
