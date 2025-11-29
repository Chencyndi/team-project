package movieapp.interface_adapter.search;

import movieapp.use_case.search.SearchOutputBoundary;
import movieapp.use_case.search.SearchOutputData;
import movieapp.entity.Movie;

import java.util.ArrayList;
import java.util.List;

public class SearchPresenter implements SearchOutputBoundary {

    private final SearchViewModel viewModel;

    public SearchPresenter(SearchViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(SearchOutputData outputData, String searchQuery) {
        viewModel.setLastQuery(searchQuery);
        List<MovieViewModel> movieViewModels = new ArrayList<>();

        for (Movie movie : outputData.getMovies()) {
            String title = movie.getTitle();
            String overview = movie.getOverview();
            double rating = movie.getVoteAverage();
            int comments = movie.getComments().size();

            movieViewModels.add(new MovieViewModel(title, overview, rating, comments));
        }

        viewModel.setSuccess(true);
        viewModel.setMessage(outputData.getMessage());
        viewModel.setMovies(movieViewModels);
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.setSuccess(false);
        viewModel.setMessage(errorMessage);
        viewModel.setMovies(new ArrayList<>());
    }
}