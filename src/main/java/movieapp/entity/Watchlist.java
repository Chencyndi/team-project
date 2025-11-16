package movieapp.entity;

import java.util.ArrayList;
import java.util.List;

public class MovieList {
    private final List<Movie> list;

    public MovieList(List<Movie> list) {
        this.list = list;
    }
    public Watchlist() {this.list = new ArrayList<>();}
    public List<Movie> getList() {
        return list;
    }
}
