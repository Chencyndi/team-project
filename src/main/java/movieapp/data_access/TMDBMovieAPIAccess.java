package movieapp.data_access;

import movieapp.entity.Comment;
import movieapp.entity.Movie;
import org.json.JSONArray;
import org.json.JSONObject;
import okhttp3.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TMDBMovieAPIAccess {
    private final OkHttpClient client = new OkHttpClient();
    private static final int SUCCESS_CODE = 200;
    private static final String API_KEY = "cb14c2564a324df140f3ebe21f348f8f";
    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String STATUS_CODE_LABEL = "status_code";
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";
    private static final String FALLBACK_IMAGE_URL = //TODO: Might want to remove
            "https://images.pexels.com/photos/321552/pexels-photo-321552.jpeg"; // Fallback if poster not found

    /**
     * Make HTTP GET request to TMDB API
     **/
    public JSONObject makeApiCall(String endpoint) throws IOException {
        Request request = new Request.Builder()
                .url(endpoint)
                .get()
                .addHeader("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("API call failed with HTTP code: " + response.code());
                return null;
            }

            ResponseBody body = response.body();
            if (body == null) {
                System.err.println("API call returned empty body");
                return null;
            }

            JSONObject jsonMovieList = new JSONObject(body.string()); // converts response object into JSONObject

            if (jsonMovieList.optInt(STATUS_CODE_LABEL, SUCCESS_CODE) != SUCCESS_CODE) {
                System.err.println("API-level error with status: " + jsonMovieList.optInt(STATUS_CODE_LABEL));
                return null;
            }

            return jsonMovieList;
        }
        catch (IOException e) {
            throw new RuntimeException("Network error during API call", e); //TODO: Create a custom exception
        }
    }

    /**
     * Parse JSON movie object into Movie entity
     **/
    private Movie parseMovie(JSONObject jsonMovieList) throws IOException {
        try {
            int id = jsonMovieList.optInt("id", -1);
            String title = jsonMovieList.optString("title", "");
            String overview = jsonMovieList.optString("overview", "");
            String releaseDate = jsonMovieList.optString("release_date", "");
            String posterURL = jsonMovieList.optString("poster_path", "");
            double voteAverage = jsonMovieList.optDouble("vote_average", 0.0);
            double popularity = jsonMovieList.optDouble("popularity", 0.0);
            int voteCount = jsonMovieList.optInt("vote_count", 0);
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
            throw new IOException("Error parsing movie: " + e.getMessage());
        }
    }

    /**
     * Fetch the 100 most popular movies from TMDB API
     * Uses multiple pages since API returns 20 movies per page
     **/
    public List<Movie> fetchPopularMovies() throws IOException {
        List<Movie> movieList = new ArrayList<>();
        int targetCount = 100;
        int page = 1;

        while (movieList.size() < targetCount) {
            String endpoint = BASE_URL + "/movie/popular?api_key=" + API_KEY + "&page=" + page;
            JSONObject response = makeApiCall(endpoint);

            if (response == null) break;

            JSONArray results = response.optJSONArray("results");
            if (results == null || results.isEmpty()) break;

            for (int i = 0; i < results.length() && movieList.size() < targetCount; i++) {
                JSONObject movieJson = results.getJSONObject(i);
                Movie movie = parseMovie(movieJson);
                if (movie != null) {
                    movieList.add(movie);
                }
            }
            page++;

            // Safety check to avoid infinite loop
            if (page > 10) break;
        }
        return movieList;
    }

    /**
     * Fetch the 100 most recently released movies
     * Uses upcoming endpoint and sorts by release date
     **/
    public List<Movie> fetchRecentlyReleasedMovies() throws IOException {
        List<Movie> movieList = new ArrayList<>();
        int page = 1;

        // Fetch multiple pages to ensure we get enough movies
        while (movieList.size() < 150 && page <= 8) {
            String endpoint = BASE_URL + "/movie/now_playing?api_key=" + API_KEY + "&page=" + page;
            JSONObject response = makeApiCall(endpoint);

            if (response == null) break;

            JSONArray results = response.optJSONArray("results");
            if (results == null || results.isEmpty()) break;

            for (int i = 0; i < results.length(); i++) {
                JSONObject movieJson = results.getJSONObject(i);
                Movie movie = parseMovie(movieJson);
                if (movie != null && movie.getReleaseDate() != null) {
                    movieList.add(movie);
                }
            }

            page++;
        }

        // Sort by release date (most recent first)
        movieList.sort((m1, m2) -> m2.getReleaseDate().compareTo(m1.getReleaseDate()));

        // Returns list top 100
        return movieList.subList(0, Math.min(100, movieList.size()));
    }

    /**
     * Get the base URL for movie poster images
     **/
    public static String getImageBaseUrl() {
        return IMAGE_BASE_URL;
    }
}