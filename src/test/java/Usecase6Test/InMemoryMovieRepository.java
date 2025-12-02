package Usecase6Test;

import movieapp.entity.Movie;
import movieapp.interface_adapter.search.MovieRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryMovieRepository implements MovieRepository {
        private final List<Movie> movies = new ArrayList<>();

        public void addMovie(Movie movie) {
            movies.add(movie);
        }

        @Override
        public List<Movie> searchMovies(String query) {
            List<Movie> result = new ArrayList<>();
            for (Movie m : movies) {
                if (m.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    result.add(m);
                }
            }
            return result;
        }
    }
