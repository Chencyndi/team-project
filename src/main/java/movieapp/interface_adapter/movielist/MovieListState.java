package movieapp.interface_adapter.movielist;

import movieapp.entity.MovieList;

/**
 * Shared state holder for the currently displayed MovieList.
 * Interface Adapters layer.
 *
 * Written by MoviePresenter after fetching movies,
 * read by SortController when the user triggers sorting.
 */
public class MovieListState {

    private MovieList currentMovieList;

    /**
     * Store the current movie list being displayed.
     */
    public void set(MovieList movieList) {
        this.currentMovieList = movieList;
    }

    /**
     * Returns the current movie list, or null if none has been set.
     */
    public MovieList get() {
        return currentMovieList;
    }

    /**
     * @return true if a current movie list is available.
     */
    public boolean hasList() {
        return currentMovieList != null;
    }
}
