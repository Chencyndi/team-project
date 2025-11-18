package movieapp.data_access;

import io.github.cdimascio.dotenv.Dotenv;
import movieapp.entity.Comment;
import movieapp.entity.Movie;
import org.json.JSONArray;
import org.json.JSONObject;
import okhttp3.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TMDBMovieAPIAccess {
    private static final Dotenv dotenv = Dotenv.configure() // checks if api_key.env exists in root directory
            .directory("./")
            .filename("api_key.env")
            .ignoreIfMissing()
            .load();

    private final OkHttpClient client = new OkHttpClient();

    private static final int SUCCESS_CODE = 200;

    private static final String API_KEY = dotenv.get("API_KEY");
    private static final String BASE_URL = dotenv.get("BASE_URL");
    private static final String IMAGE_BASE_URL = dotenv.get("IMAGE_BASE_URL");
    private static final String IMAGE_FALLBACK_URL = dotenv.get("IMAGE_FALLBACK_URL");

    private static final String STATUS_CODE_LABEL = "status_code";

    /**
     * Make HTTP GET request to TMDB API
     **/
    public JSONObject makeApiCall(String endpoint) throws Exception {
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
            throw new Exception("Network error during API call", e);
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


            if (id == -1 || title.isEmpty()) { // skips movies missing essential data
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
    public List<Movie> fetchPopularMovies() throws Exception {
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

            if (page > 10) break; // avoids infinite loop
        }
        return movieList;
    }

    /**
     * Fetch the 100 most recently released movies
     * Uses upcoming endpoint and sorts by release date
     **/
    public List<Movie> fetchRecentlyReleasedMovies() throws Exception {
        List<Movie> movieList = new ArrayList<>();
        int page = 1;


        while (movieList.size() < 150 && page <= 8) { // fetch multiple pages to ensure enough movies returned
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

        movieList.sort((m1, m2) -> m2 // sort by release date (most recent first)
                .getReleaseDate()
                .compareTo(m1.getReleaseDate()));

        return movieList.subList(0, Math.min(100, movieList.size())); // return list top 100
    }

    /**
     * Fetch a single movie by its TMDB ID
     * @param movieID the TMDB movie ID
     * @return Movie object or null if not found
     * @throws IOException if network error occurs
     */
    public Movie fetchMovieByID(int movieID) throws Exception {
        String endpoint = BASE_URL + "/movie/" + movieID + "?api_key=" + API_KEY;

        JSONObject response = makeApiCall(endpoint);

        if (response == null) {
            throw new IOException("Failed to fetch movie with ID: " + movieID);
        }

        return parseMovie(response);
    }

    /**
     * Get the base URL for movie poster images
     **/
    public static String getImageBaseUrl() {
        return IMAGE_BASE_URL;
    }
}