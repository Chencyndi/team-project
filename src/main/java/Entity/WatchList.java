package Entity;

import java.util.List;

public class WatchList {
    private final List<Movie> list;

    public WatchList(List<Movie> list) {
        this.list = list;
    }
    public List<Movie> getList() {
        return list;
    }
}
