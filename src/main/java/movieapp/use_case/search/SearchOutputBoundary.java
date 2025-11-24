package movieapp.use_case.search;

import movieapp.entity.Movie;

import java.util.List;

public interface SearchOutputBoundary {


    SearchOutputData prepareSuccessView(String message, List<Movie> movies);
    SearchOutputData prepareFailView(String errorMessage);

}
