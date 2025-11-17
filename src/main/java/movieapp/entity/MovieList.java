package movieapp.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MovieList {
    private String name;
    private List<Movie> movieList;
    private String currentSort;
    private boolean ascending;

    public enum SortType {
        ALPHABETICAL_AZ,
        ALPHABETICAL_ZA,
        RATING_DESC,
        RATING_ASC,
        VOTE_COUNT_DESC,
        VOTE_COUNT_ASC,
        NONE
    }

    public MovieList(String name, List<Movie> movieList) {
        this.name = name;
        this.movieList = new ArrayList<>(movieList);
        this.currentSort = "NONE";
        this.ascending = true;
    }

    // Getters

    public List<Movie> getMovieList() { return new ArrayList<>(movieList); }
    public String getCurrentSort() { return currentSort; }
    public String getName() { return name; }
    public int getListSize() { return movieList.size(); }

    /**
     * Sort the movie list by the specified type
     */
    public void sort(SortType sortType) {
        Comparator<Movie> comparator = null;

        switch (sortType) {
            case ALPHABETICAL_AZ:
                comparator = Comparator.comparing(Movie::getTitle)
                        .thenComparing(Movie::getTitle); // Tiebreaker
                currentSort = "Alphabetical (A→Z)";
                ascending = true;
                break;

            case ALPHABETICAL_ZA:
                comparator = Comparator.comparing(Movie::getTitle).reversed()
                        .thenComparing(Movie::getTitle); // Tiebreaker
                currentSort = "Alphabetical (Z→A)";
                ascending = false;
                break;

            case RATING_DESC:
                comparator = Comparator.comparingDouble(Movie::getVoteAverage).reversed()
                        .thenComparing(Movie::getTitle); // Tiebreaker
                currentSort = "Average Rating (High→Low)";
                ascending = false;
                break;

            case RATING_ASC:
                comparator = Comparator.comparingDouble(Movie::getVoteAverage)
                        .thenComparing(Movie::getTitle); // Tiebreaker
                currentSort = "Average Rating (Low→High)";
                ascending = true;
                break;

            case VOTE_COUNT_DESC:
                comparator = Comparator.comparingInt(Movie::getVoteCount).reversed()
                        .thenComparing(Movie::getTitle); // Tiebreaker
                currentSort = "Number of Ratings (High→Low)";
                ascending = false;
                break;

            case VOTE_COUNT_ASC:
                comparator = Comparator.comparingInt(Movie::getVoteCount)
                        .thenComparing(Movie::getTitle); // Tiebreaker
                currentSort = "Number of Ratings (Low→High)";
                ascending = true;
                break;

            case NONE:
            default:
                currentSort = "Default";
                return;
        }

        if (comparator != null) {
            movieList.sort(comparator);
        }
    }

    /**
     * Check if movielist is empty
     */
    public boolean isEmpty() {
        return movieList.isEmpty();
    }
}