package movieapp.entity;

import java.util.List;

public class Movie {

    private final int id;
    private final String title;
    private final String overview;
    private final String releaseDate;
    private final String posterUrl;
    private double voteAverage; // rating (final TBD)
    private double popularity; // popularity (final TBD)
    private int voteCount; // (final TBD)
    private List<Comment> comments;

    public Movie(int id, String title, String overview, String releaseDate, String posterUrl, double voteAverage,
                 double popularity, int voteCount,  List<Comment> comments) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.posterUrl = posterUrl;
        this.popularity = popularity;
        this.comments = comments;
    }

    // Getter methods
    public int getId() { return id; }
    public String getName() { return title; }
    public String getOverview() { return overview; }
    public String getReleaseDate() { return releaseDate; }
    public double getRate() { return voteAverage; }
    public double getPopularity() { return popularity; }
    public int getVoteCount() { return voteCount; }
    public String getPosterUrl() { return posterUrl; }
    public List<Comment> getComments() { return comments; }

    // Setter methods
    public void setVoteAverage(double voteAverage) { this.voteAverage = voteAverage; }
    public void setPopularity(double popularity) { this.popularity = popularity; }
    public void setVoteCount(int voteCount) { this.voteCount = voteCount; }
    public void setComments(List<Comment> comments) { this.comments = comments; }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", voteAverage=" + voteAverage +
                ", popularity=" + popularity +
                ", voteCount=" + voteCount +
                '}';
    }
}
