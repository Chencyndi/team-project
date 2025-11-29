package movieapp.interface_adapter.search;

import movieapp.use_case.search.SearchInputBoundary;
import movieapp.use_case.search.SearchInputData;
import movieapp.use_case.search.SearchOutputData;

public class SearchController {
    private final SearchInputBoundary searchUseCase;

    public SearchController(SearchInputBoundary searchUseCase) {
        this.searchUseCase = searchUseCase;
    }
    
    public void search(String query) {
        SearchInputData inputData = new SearchInputData(query);
        searchUseCase.execute(inputData);
    }
}