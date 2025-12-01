// TMDBMovieRepository.java
package movieapp.data_access;

import movieapp.interface_adapter.search.MovieRepository;
import movieapp.entity.Movie;
import movieapp.entity.Comment;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TMDBMovieRepository implements MovieRepository {

    private final String apiKey = "cb14c2564a324df140f3ebe21f348f8f";
    
    @Override
    public List<Movie> searchMovies(String query) {
        List<Movie> movies = new ArrayList<>();
        
        try {
            String urlString = "https://api.themoviedb.org/3/search/movie?api_key=" + apiKey + "&query=" + query.replace(" ", "%20");
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("API request failed");
            }
            
            Scanner scanner = new Scanner(url.openStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            scanner.close();
            
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray results = jsonResponse.getJSONArray("results");
            
            for (int i = 0; i < results.length(); i++) {
                JSONObject movieData = results.getJSONObject(i);
                Movie movie = createMovieFromJSON(movieData);
                movies.add(movie);
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to search movies: " + e.getMessage());
        }
        
        return movies;
    }
    @Override
    public void addMovie(Movie movie) {
        return;
    }
    
    private Movie createMovieFromJSON(JSONObject movieData) {
        String title = movieData.getString("title");
        String overview = movieData.getString("overview");
        double rating = movieData.getDouble("vote_average");
        List<Comment> comments = new ArrayList<>(); // Empty comments for now
        
        return new Movie(title, overview, rating, comments);
    }
}