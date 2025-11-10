package Entity;

import java.util.List;

public class Movie {
    private final String name;
    private final double rate;
    private final List<Comment> comments;


    public Movie(String name, double rate, List<Comment> comments) {
        this.name = name;
        this.rate = rate;
        this.comments = comments;
    }
    public String getName() {
        return name;
    }
    public double getRate() {
        return rate;
    }
    public List<Comment> getComments() {
        return comments;
    }
}
