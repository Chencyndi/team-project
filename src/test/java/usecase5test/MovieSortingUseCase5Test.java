package usecase5test;

import movieapp.entity.Movie;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MovieSortingUseCase5Test {

    private List<Movie> sample() {
        return List.of(
                new Movie(1, "Avengers", "", "2012-05-04", "", 8.0, 90.0, 1000, List.of()),
                new Movie(2, "Batman", "", "2008-07-18", "", 9.0, 88.0, 2000, List.of()),
                new Movie(3, "Superman", "", "1978-12-15", "", 7.0, 50.0, 300, List.of())
        );
    }

    @Test
    void sortAlphabeticalAZ() {
        List<Movie> sorted = sample().stream()
                .sorted((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()))
                .toList();

        assertEquals("Avengers", sorted.get(0).getTitle());
        assertEquals("Batman", sorted.get(1).getTitle());
        assertEquals("Superman", sorted.get(2).getTitle());
    }

    @Test
    void sortAlphabeticalZA() {
        List<Movie> sorted = sample().stream()
                .sorted((a, b) -> b.getTitle().compareToIgnoreCase(a.getTitle()))
                .toList();

        assertEquals("Superman", sorted.get(0).getTitle());
        assertEquals("Batman", sorted.get(1).getTitle());
        assertEquals("Avengers", sorted.get(2).getTitle());
    }

    @Test
    void sortAverageRatingHighLow() {
        List<Movie> sorted = sample().stream()
                .sorted((a, b) -> Double.compare(b.getVoteAverage(), a.getVoteAverage()))
                .toList();

        assertEquals("Batman", sorted.get(0).getTitle());
        assertEquals("Avengers", sorted.get(1).getTitle());
        assertEquals("Superman", sorted.get(2).getTitle());
    }

    @Test
    void sortAverageRatingLowHigh() {
        List<Movie> sorted = sample().stream()
                .sorted((a, b) -> Double.compare(a.getVoteAverage(), b.getVoteAverage()))
                .toList();

        assertEquals("Superman", sorted.get(0).getTitle());
        assertEquals("Avengers", sorted.get(1).getTitle());
        assertEquals("Batman", sorted.get(2).getTitle());
    }

    @Test
    void sortNumberOfRatingsHighLow() {
        List<Movie> sorted = sample().stream()
                .sorted((a, b) -> Integer.compare(b.getVoteCount(), a.getVoteCount()))
                .toList();

        assertEquals("Batman", sorted.get(0).getTitle());
        assertEquals("Avengers", sorted.get(1).getTitle());
        assertEquals("Superman", sorted.get(2).getTitle());
    }

    @Test
    void sortNumberOfRatingsLowHigh() {
        List<Movie> sorted = sample().stream()
                .sorted((a, b) -> Integer.compare(a.getVoteCount(), b.getVoteCount()))
                .toList();

        assertEquals("Superman", sorted.get(0).getTitle());
        assertEquals("Avengers", sorted.get(1).getTitle());
        assertEquals("Batman", sorted.get(2).getTitle());
    }
}