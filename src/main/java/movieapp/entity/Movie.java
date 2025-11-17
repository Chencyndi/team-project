package movieapp.entity;

import java.util.List;

public class Movie {
    private final String name;
    private final String overview;
    private final double rate;
    private final List<Comment> comments;


    public Movie(String name, String overview, double rate, List<Comment> comments) {
        this.name = name;
        this.overview = overview;
        this.rate = rate;
        this.comments = comments;
    }
    public String getName() {
        return name;
    }
    public double getRate() {
        return rate;
    }
    public  String getOverview() {return overview;}
    public List<Comment> getComments() {
        return comments;
    }
}
