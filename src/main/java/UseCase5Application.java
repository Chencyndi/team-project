import movieapp.data_access.TMDBMovieAPIAccess;
import movieapp.interface_adapter.movielist.*;
import movieapp.use_case.movielist.*;
import movieapp.view.MovieListSwingView;

import javax.swing.SwingUtilities;

public class UseCase5Application {
        
    public static void main(String[] args) {

        // --- Shared state (Interface Adapters) ---
        MovieListState movieListState = new MovieListState();

        // --- Data access (Frameworks & Drivers) ---
        MovieDataSource movieDataSource = new TMDBMovieAPIAccess();

        // --- Presenters ---
        MoviePresenter moviePresenter = new MoviePresenter(movieListState);
        SortPresenter sortPresenter = new SortPresenter();

        // --- Use case interactors ---
        FetchMoviesInputBoundary fetchMoviesInteractor =
                new FetchMoviesInteractor(movieDataSource, moviePresenter);

        SortMoviesInputBoundary sortMoviesInteractor =
                new SortMoviesInteractor(sortPresenter);

        // --- Controllers ---
        MovieController movieController =
                new MovieController(fetchMoviesInteractor);

        SortController sortController =
                new SortController(sortMoviesInteractor, movieListState);

        // --- UI ---
        SwingUtilities.invokeLater(() -> {
            MovieListSwingView view = new MovieListSwingView();

            // Presenters → view
            moviePresenter.setView(view);
            sortPresenter.setView(view);

            // View → controllers
            view.setOnPopularPressed(movieController::onPopularMoviesRequested);
            view.setOnRecentPressed(movieController::onRecentMoviesRequested);
            view.setOnSortSelected(sortController::onSortSelected);

            view.setVisible(true);
        });
    }
}