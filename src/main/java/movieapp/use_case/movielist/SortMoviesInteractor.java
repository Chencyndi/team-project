package movieapp.use_case.movielist;

import movieapp.entity.Movie;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Use Case Interactor for sorting movies in memory.
 */
public class SortMoviesInteractor implements SortMoviesInputBoundary {

    private final SortMoviesOutputBoundary outputBoundary;

    public SortMoviesInteractor(SortMoviesOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void sortMovies(SortMoviesInputData inputData) {
        List<Movie> sorted = new ArrayList<>(inputData.getMovies());

        Comparator<Movie> comparator = buildComparator(inputData.getSortType());

        if (comparator != null) {
            sorted.sort(comparator);
        }

        SortMoviesOutputData outputData = new SortMoviesOutputData(
                inputData.getListName(),
                inputData.getSortType(),
                sorted
        );

        outputBoundary.presentSortedMovies(outputData);
    }

    private Comparator<Movie> buildComparator(SortType sortType) {
        if (sortType == null || sortType == SortType.NONE) {
            return null;
        }

        return switch (sortType) {
            case ALPHABETICAL_AZ ->
                    Comparator.comparing(Movie::getTitle, String.CASE_INSENSITIVE_ORDER);
            case ALPHABETICAL_ZA ->
                    Comparator.comparing(Movie::getTitle, String.CASE_INSENSITIVE_ORDER).reversed();
            case RATING_DESC ->
                    Comparator.comparingDouble(Movie::getVoteAverage).reversed()
                            .thenComparing(Movie::getTitle, String.CASE_INSENSITIVE_ORDER);
            case RATING_ASC ->
                    Comparator.comparingDouble(Movie::getVoteAverage)
                            .thenComparing(Movie::getTitle, String.CASE_INSENSITIVE_ORDER);
            case POPULARITY_DESC ->
                    Comparator.comparingDouble(Movie::getPopularity).reversed()
                            .thenComparing(Movie::getTitle, String.CASE_INSENSITIVE_ORDER);
            case POPULARITY_ASC ->
                    Comparator.comparingDouble(Movie::getPopularity)
                            .thenComparing(Movie::getTitle, String.CASE_INSENSITIVE_ORDER);
        };
    }
}
