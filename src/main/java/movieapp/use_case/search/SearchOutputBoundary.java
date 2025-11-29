package movieapp.use_case.search;

import movieapp.entity.Movie;

import java.util.List;

public interface SearchOutputBoundary {


    void prepareSuccessView(SearchOutputData outputData,  String searchQuery);
    void prepareFailView(String errorMessage);

}
