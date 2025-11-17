package movieapp.use_case.search;

public class SearchInputData {
    private final String searchQuery;

    public SearchInputData(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getSearchQuery() { return searchQuery; }
}