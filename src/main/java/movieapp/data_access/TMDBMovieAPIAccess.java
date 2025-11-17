package movieapp.data_access;

import movieapp.entity.Comment;
import movieapp.entity.Movie;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TMDBMovieAPIAccess {
    private static final String API_KEY = "YOUR_API_KEY_HERE"; // Replace with your actual API key
    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    /**
     * Fetch the 100 most popular movies from TMDB API
     * Uses multiple pages since API returns 20 movies per page
     */
    public List<Movie> fetchPopularMovies() throws Exception {
        List<Movie> movies = new ArrayList<>();
        int targetCount = 100;
        int page = 1;

        while (movies.size() < targetCount) {
            String endpoint = BASE_URL + "/movie/popular?api_key=" + API_KEY + "&page=" + page;
            JSONObject response = makeApiCall(endpoint);

            if (response == null) break;

            JSONArray results = response.optJSONArray("results");
            if (results == null || results.length() == 0) break;

            for (int i = 0; i < results.length() && movies.size() < targetCount; i++) {
                JSONObject movieJson = results.getJSONObject(i);
                Movie movie = parseMovie(movieJson);
                if (movie != null) {
                    movies.add(movie);
                }
            }

            page++;

            // Safety check to avoid infinite loop
            if (page > 10) break;
        }

        return movies;
    }

    /**
     * Fetch the 100 most recently released movies
     * Uses upcoming endpoint and sorts by release date
     */
    public List<Movie> fetchRecentlyReleasedMovies() throws Exception {
        List<Movie> movies = new ArrayList<>();
        int page = 1;

        // Fetch multiple pages to ensure we get enough movies
        while (movies.size() < 150 && page <= 8) {
            String endpoint = BASE_URL + "/movie/now_playing?api_key=" + API_KEY + "&page=" + page;
            JSONObject response = makeApiCall(endpoint);

            if (response == null) break;

            JSONArray results = response.optJSONArray("results");
            if (results == null || results.length() == 0) break;

            for (int i = 0; i < results.length(); i++) {
                JSONObject movieJson = results.getJSONObject(i);
                Movie movie = parseMovie(movieJson);
                if (movie != null && movie.getReleaseDate() != null) {
                    movies.add(movie);
                }
            }

            page++;
        }

        // Sort by release date (most recent first)
        movies.sort((m1, m2) -> m2.getReleaseDate().compareTo(m1.getReleaseDate()));

        // Return top 100
        return movies.subList(0, Math.min(100, movies.size()));
    }

    /**
     * Make HTTP GET request to TMDB API
     */
    private JSONObject makeApiCall(String endpoint) {
        try {
            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                System.err.println("API call failed with response code: " + responseCode);
                return null;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return new JSONObject(response.toString());

        } catch (Exception e) {
            System.err.println("Error making API call: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parse JSON movie object into Movie entity
     */

    private Movie parseMovie(JSONObject json) {
        try {
            int id = json.optInt("id", -1);
            String title = json.optString("title", "");
            String overview = json.optString("overview", "");
            String releaseDate = json.optString("release_date", "");
            String posterURL = json.optString("poster_path", null);
            double voteAverage = json.optDouble("vote_average", 0.0);
            double popularity = json.optDouble("popularity", 0.0);
            int voteCount = json.optInt("vote_count", 0);
            List<Comment> comments = new ArrayList<>();

            // Skip movies with missing essential data
            if (id == -1 || title.isEmpty()) {
                return null;
            }

            String fullPosterUrl = (posterURL != null && !posterURL.isEmpty())
                    ? IMAGE_BASE_URL + posterURL
                    : null;

            return new Movie(id, title, overview, releaseDate, fullPosterUrl, voteAverage, popularity, voteCount,
                    comments);

        } catch (Exception e) {
            System.err.println("Error parsing movie: " + e.getMessage());
            return null;
        }
    }

    /**
     * Get the base URL for movie poster images
     */
    public static String getImageBaseUrl() {
        return IMAGE_BASE_URL;
    }
}