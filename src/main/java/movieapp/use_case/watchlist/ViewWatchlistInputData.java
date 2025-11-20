package movieapp.use_case.watchlist;

public class ViewWatchlistInputData {
    private final String username;

    public ViewWatchlistInputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}