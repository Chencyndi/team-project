package movieapp.use_case.search;

public interface SearchOutputBoundary {


    void prepareSuccessView(SearchOutputData outputData,  String searchQuery);
    void prepareFailView(String errorMessage);

}
