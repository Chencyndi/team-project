package movieapp.interface_adapter.movielist;


import movieapp.entity.MovieList;

/**
 * Controller for Use Case 5 - Sorting Movies
 * Interface Adapters Layer
 * Handles user sorting actions
 */

public class SortController {
    private MovieList movieList;
    private MoviePresenter presenter;

    /**
     * Set the movie list to be sorted
     */

    public void setMovieList(MovieList movieList) {
        this.movieList = movieList;
    }

    /**
     * Set the presenter for displaying results
     */

    public void setPresenter(MoviePresenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Handle user sort selection
     * Called when user selects a sort option from dropdown
     *
     * @param sortOption the sort option selected (e.g., "Alphabetical A→Z")
     */

    public void onSortSelected(String sortOption) {
        if (movieList == null || presenter == null) {
            return;
        }

        // Convert UI sort option to entity sort type
        MovieList.SortType sortType = mapSortOption(sortOption);

        // Execute sort on entity
        movieList.sort(sortType);

        // Present updated list
        presenter.presentSortedMovies(movieList);
    }

    /**
     * Map UI sort option to entity sort type
     */

    private MovieList.SortType mapSortOption(String sortOption) {
        return switch (sortOption) {
            case "Alphabetical (A→Z)" -> MovieList.SortType.ALPHABETICAL_AZ;
            case "Alphabetical (Z→A)" -> MovieList.SortType.ALPHABETICAL_ZA;
            case "Average Rating (High→Low)" -> MovieList.SortType.RATING_DESC;
            case "Average Rating (Low→High)" -> MovieList.SortType.RATING_ASC;
            case "Number of Ratings (High→Low)" -> MovieList.SortType.VOTE_COUNT_DESC;
            case "Number of Ratings (Low→High)" -> MovieList.SortType.VOTE_COUNT_ASC;
            default -> MovieList.SortType.NONE;
        };
    }
}