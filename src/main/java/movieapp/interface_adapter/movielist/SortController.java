package movieapp.interface_adapter.movielist;

import movieapp.entity.MovieList;
import movieapp.use_case.movielist.SortMoviesInputBoundary;
import movieapp.use_case.movielist.SortMoviesInputData;
import movieapp.use_case.movielist.SortType;

/**
 * Controller for sorting movies.
 * Interface Adapters layer.
 */
public class SortController {

    private final SortMoviesInputBoundary sortMoviesInteractor;
    private MovieList currentMovieList;

    public SortController(SortMoviesInputBoundary sortMoviesInteractor) {
        this.sortMoviesInteractor = sortMoviesInteractor;
    }

    // Called when the current movie list is updated (e.g., after fetching)
    public void setCurrentMovieList(MovieList movieList) {
        this.currentMovieList = movieList;
    }


    // Called when the user selects a sort option from the UI
    public void onSortSelected(String sortOption) {
        if (currentMovieList == null) {
            return;
        }

        SortType sortType = mapSortOption(sortOption);

        SortMoviesInputData inputData = new SortMoviesInputData(
                currentMovieList.getName(),
                sortType,
                currentMovieList.getMovies()
        );

        sortMoviesInteractor.sortMovies(inputData);
    }

    // Called when the user selects to change a sort option from the UI
    private SortType mapSortOption(String sortOption) {
        return switch (sortOption) {
            case "Alphabetical (A→Z)" -> SortType.ALPHABETICAL_AZ;
            case "Alphabetical (Z→A)" -> SortType.ALPHABETICAL_ZA;
            case "Average Rating (High→Low)" -> SortType.RATING_DESC;
            case "Average Rating (Low→High)" -> SortType.RATING_ASC;
            case "Number of Ratings (High→Low)" -> SortType.POPULARITY_DESC;
            case "Number of Ratings (Low→High)" -> SortType.POPULARITY_ASC;
            default -> SortType.NONE;
        };
    }
}