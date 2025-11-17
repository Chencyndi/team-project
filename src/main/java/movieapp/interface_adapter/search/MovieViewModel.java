package movieapp.interface_adapter.search;

public class MovieViewModel {
    private final String name;
    private final double rating;
    private final int commentCount;
    
    public MovieViewModel(String name, double rating, int commentCount) {
        this.name = name;
        this.rating = rating;
        this.commentCount = commentCount;
    }
    
    // Getters
    public String getName() { return name; }
    public double getRating() { return rating; }
    public int getCommentCount() { return commentCount; }
}
