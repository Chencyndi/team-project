package Entity;

import java.util.List;

public class Watchlist {
    private final List<Movie> list;

    public Watchlist(List<Movie> list) {
        this.list = list;
    }
    public List<Movie> getList() {
        return list;
    }
}
