package movieapp.interface_adapter.search;

public class MovieViewModel {
    private final String name;
    private final String overview;
    private final double rating;
    private final int commentCount;
    
    public MovieViewModel(String name, String overview, double rating, int commentCount) {
        this.name = name;
        this.overview = overview;
        this.rating = rating;
        this.commentCount = commentCount;
    }
    
    // Getters
    public String getName() { return name; }
    public String getOverview() { return overview; }
    public double getRating() { return rating; }
    public int getCommentCount() { return commentCount; }
}
