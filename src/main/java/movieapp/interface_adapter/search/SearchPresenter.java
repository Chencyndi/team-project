package movieapp.interface_adapter.search;

import movieapp.use_case.search.SearchOutputBoundary;
import movieapp.use_case.search.SearchOutputData;
import movieapp.entity.Movie;
import java.util.List;

public class SearchPresenter implements SearchOutputBoundary {
    @Override
    public SearchOutputData prepareSuccessView(String message, List<Movie> movies) {
        return new SearchOutputData(true, message, movies);
    }

    @Override
    public SearchOutputData prepareFailView(String errorMessage) {
        return new SearchOutputData(false, errorMessage, null);
    }
}