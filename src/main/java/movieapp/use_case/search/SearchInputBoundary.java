package movieapp.use_case.search;

public interface SearchInputBoundary {
    SearchOutputData execute(SearchInputData inputData);
}