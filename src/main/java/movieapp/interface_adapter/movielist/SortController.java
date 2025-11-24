package movieapp.interface_adapter.movielist;

import movieapp.entity.MovieList;
import movieapp.use_case.movielist.SortMoviesInputBoundary;
import movieapp.use_case.movielist.SortMoviesInputData;
import movieapp.use_case.movielist.SortType;

/**
 * Controller for the movie sorting use case.
 * Interface Adapters layer.
 *
 * Reads the current MovieList from MovieListState,
 * converts a UI sort option string into SortMoviesInputData,
 * and invokes the SortMovies interactor.
 */
public class SortController {

    private final SortMoviesInputBoundary sortMoviesInteractor;
    private final MovieListState movieListState;

    public SortController(SortMoviesInputBoundary sortMoviesInteractor,
                          MovieListState movieListState) {
        this.sortMoviesInteractor = sortMoviesInteractor;
        this.movieListState = movieListState;
    }

    /**
     * Called when the user selects a sort option from the UI.
     *
     * @param sortOption the sort option string selected in the UI
     *                   (e.g., "Average Rating (High→Low)")
     */
    public void onSortSelected(String sortOption) {
        if (!movieListState.hasList()) {
            // No movies loaded yet; nothing to sort.
            return;
        }

        MovieList currentMovieList = movieListState.get();
        SortType sortType = mapSortOption(sortOption);

        SortMoviesInputData inputData = new SortMoviesInputData(
                currentMovieList.getName(),
                sortType,
                currentMovieList.getMovies()
        );

        sortMoviesInteractor.sortMovies(inputData);
    }

    private SortType mapSortOption(String sortOption) {
        if (sortOption == null) {
            return SortType.NONE;
        }

        switch (sortOption) {
            case "Alphabetical (A→Z)":
                return SortType.ALPHABETICAL_AZ;
            case "Alphabetical (Z→A)":
                return SortType.ALPHABETICAL_ZA;
            case "Average Rating (High→Low)":
                return SortType.RATING_DESC;
            case "Average Rating (Low→High)":
                return SortType.RATING_ASC;
            case "Number of Ratings (High→Low)":
                return SortType.POPULARITY_DESC;
            case "Number of Ratings (Low→High)":
                return SortType.POPULARITY_ASC;
            default:
                return SortType.NONE;
        }
    }
}