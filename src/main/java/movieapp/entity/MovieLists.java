package movieapp.entity;

import java.util.List;

public class MovieLists {
    private final int length;
    private final String name;
    private final List<Movie> list;
    private final String sort;


    public MovieLists(int length, String name, List<Movie> list, String sort) {
        this.length = length;
        this.name = name;
        this.list = list;
        this.sort = sort;
    }
    public int getLength() {
        return length;
    }
    public String getName() {
        return name;
    }
    public List<Movie> getList() {
        return list;
    }
    public String getSort() {
        return sort;
    }
}
