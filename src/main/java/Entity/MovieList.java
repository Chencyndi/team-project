package Entity;

import java.util.List;

public class MovieList {
    private final List<Movie> list;

    public MovieList(List<Movie> list) {
        this.list = list;
    }
    public List<Movie> getList() {
        return list;
    }
}
