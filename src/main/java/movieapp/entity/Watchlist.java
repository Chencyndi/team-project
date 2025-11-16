package movieapp.entity;

import java.util.ArrayList;
import java.util.List;

public class Watchlist {
    private final List<Movie> list;

    public Watchlist(List<Movie> list) {
        this.list = list;
    }
    public Watchlist() {this.list = new ArrayList<>();}
    public List<Movie> getList() {
        return list;
    }
}
