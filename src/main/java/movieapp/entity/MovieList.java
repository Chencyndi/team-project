package movieapp.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Domain entity representing a named list of movies.
 */
public class MovieList {

    private final String name;
    private final List<Movie> movies;

    public MovieList(String name, List<Movie> movies) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Movie list name cannot be null or blank");
        }
        if (movies == null) {
            throw new IllegalArgumentException("Movies list cannot be null");
        }
        this.name = name;
        this.movies = new ArrayList<>(movies);
    }

    public String getName() {
        return name;
    }

    public List<Movie> getMovies() {
        return Collections.unmodifiableList(movies);
    }

    public int size() {
        return movies.size();
    }

    public boolean isEmpty() {
        return movies.isEmpty();
    }

    /**
     * Determines whether this MovieList is equal to another object.
     * <p>
     * Two MovieList instances are considered equal if and only if:
     * <ul>
     *   <li>they have the same name, and</li>
     *   <li>their underlying movie collections contain the same elements
     *       in the same order.</li>
     * </ul>
     * This method follows the general contract of {@link Object#equals(Object)}.
     *
     * @param o the object to compare with this MovieList
     * @return true if the objects are logically equal; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovieList that)) return false;
        return name.equals(that.name) && movies.equals(that.movies);
    }

    /**
     * Computes the hash code for this MovieList.
     * <p>
     * The hash code is derived from the list’s name and its movie collection,
     * ensuring that two MovieList instances which are equal according to
     * {@link #equals(Object)} produce the same hash value. This maintains
     * the required contract between equals and hashCode, allowing instances
     * to be used safely in hash-based collections.
     *
     * @return a hash code value for this MovieList
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, movies);
    }

    public String getCurrentSort() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCurrentSort'");
    }
}