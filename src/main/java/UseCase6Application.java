import movieapp.interface_adapter.search.MovieRepository;
import movieapp.interface_adapter.search.SearchViewModel;
import movieapp.use_case.search.SearchInputBoundary;
import movieapp.use_case.search.SearchInteractor;
import movieapp.data_access.TMDBMovieAPIAccess;
import movieapp.interface_adapter.search.SearchController;
import movieapp.interface_adapter.search.SearchPresenter;
import movieapp.view.SearchView;

import javax.swing.*;

public class UseCase6Application {
    public static void main(String[] args) {
        // Dependency injection setup
        MovieRepository movieRepository = new TMDBMovieAPIAccess();
        SearchViewModel searchViewModel = new SearchViewModel();
        SearchPresenter searchPresenter = new SearchPresenter(searchViewModel);
        SearchInputBoundary searchInteractor = new SearchInteractor(movieRepository, searchPresenter);
        SearchController searchController = new SearchController(searchInteractor);

        // Start UI
        SwingUtilities.invokeLater(() -> {
            SearchView searchView = new SearchView(searchController, searchViewModel);
            searchView.setVisible(true);
        });
    }
}