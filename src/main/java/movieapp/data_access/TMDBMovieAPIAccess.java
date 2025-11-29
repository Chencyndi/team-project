package movieapp.data_access;

import io.github.cdimascio.dotenv.Dotenv;
import movieapp.entity.Comment;
import movieapp.entity.Movie;
import movieapp.use_case.movielist.MovieDataSource;
import org.json.JSONArray;
import org.json.JSONObject;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Frameworks & Drivers layer implementation of MovieDataSource.
 *
 * Communicates with the TMDB API using HTTP and returns Movie entities.
 * No business logic. No UI. No use case knowledge.
 */
public class TMDBMovieAPIAccess implements MovieDataSource {

    private static final Dotenv dotenv = Dotenv.configure()
            .directory("./")
            .filename("api_key.env")
            .ignoreIfMissing()
            .load();

    private final OkHttpClient client = new OkHttpClient();

    private static final String API_KEY = dotenv.get("API_KEY");
    private static final String BASE_URL = dotenv.get("BASE_URL");
    private static final String IMAGE_BASE_URL = dotenv.get("IMAGE_BASE_URL");

    private static final int SUCCESS_CODE = 200;

    // -------------------------------------------------------------------------
    // API CALL UTILITY
    // -------------------------------------------------------------------------

    private JSONObject makeApiCall(String endpoint) throws Exception {
        Request request = new Request.Builder()
                .url(endpoint)
                .addHeader("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                throw new IOException("HTTP request failed: " + response.code());
            }

            ResponseBody body = response.body();
            if (body == null) {
                throw new IOException("API returned empty body");
            }

            JSONObject json = new JSONObject(body.string());

            if (json.optInt("status_code", SUCCESS_CODE) != SUCCESS_CODE) {
                throw new IOException("API-level error: " + json.optInt("status_code"));
            }

            return json;
        }
    }

    // -------------------------------------------------------------------------
    // MOVIE PARSING
    // -------------------------------------------------------------------------

    private Movie parseMovie(JSONObject json) {
        int id = json.optInt("id", -1);
        String title = json.optString("title", "");
        String overview = json.optString("overview", "");
        String releaseDate = json.optString("release_date", "");
        String posterPath = json.optString("poster_path", "");
        double voteAverage = json.optDouble("vote_average", 0.0);
        double popularity = json.optDouble("popularity", 0.0);
        int voteCount = json.optInt("vote_count", 0);

        // Essential fields: id & title
        if (id < 0 || title.isEmpty()) {
            return null;
        }

        String posterUrl = (posterPath != null && !posterPath.isEmpty())
                ? IMAGE_BASE_URL + posterPath
                : null;

        return new Movie(
                id,
                title,
                overview,
                releaseDate,
                posterUrl,
                voteAverage,
                popularity,
                voteCount,
                new ArrayList<Comment>()
        );
    }

    // -------------------------------------------------------------------------
    // POPULAR MOVIES
    // -------------------------------------------------------------------------

    @Override
    public List<Movie> fetchPopularMovies(int count) throws Exception {
        List<Movie> movies = new ArrayList<>();
        int page = 1;

        while (movies.size() < count) {
            String url = BASE_URL + "/movie/popular?api_key=" + API_KEY + "&page=" + page;
            JSONObject json = makeApiCall(url);

            JSONArray results = json.optJSONArray("results");
            if (results == null || results.isEmpty()) break;

            for (int i = 0; i < results.length() && movies.size() < count; i++) {
                Movie movie = parseMovie(results.getJSONObject(i));
                if (movie != null) {
                    movies.add(movie);
                }
            }

            page++;

            if (page > 12) break; // safety break
        }

        return movies;
    }

    // -------------------------------------------------------------------------
    // RECENTLY RELEASED MOVIES
    // -------------------------------------------------------------------------

    @Override
    public List<Movie> fetchRecentMovies(int count) throws Exception {
        List<Movie> movies = new ArrayList<>();
        int page = 1;
        int maxPages = 12;  // “wider parameters” expansion

        while (movies.size() < count && page <= maxPages) {
            String url = BASE_URL + "/movie/upcoming?api_key=" + API_KEY + "&page=" + page;
            JSONObject json = makeApiCall(url);

            JSONArray results = json.optJSONArray("results");
            if (results == null || results.isEmpty()) break;

            for (int i = 0; i < results.length(); i++) {
                JSONObject obj = results.getJSONObject(i);
                Movie movie = parseMovie(obj);

                // Skip if missing release date (needed for sorting)
                if (movie != null &&
                    movie.getReleaseDate() != null &&
                    !movie.getReleaseDate().isEmpty()) {

                    movies.add(movie);
                }
            }

            page++;
        }

        // Sort by release date descending
        movies.sort((m1, m2) -> m2.getReleaseDate().compareTo(m1.getReleaseDate()));

        // Trim to count
        return movies.subList(0, Math.min(count, movies.size()));
    }

    // -------------------------------------------------------------------------
    // FIND MOVIE BY ID
    // -------------------------------------------------------------------------

    @Override
    public Movie findById(int id) throws Exception {
        String url = BASE_URL + "/movie/" + id + "?api_key=" + API_KEY;

        JSONObject json;
        try {
            json = makeApiCall(url);
        } catch (IOException e) {
            // If TMDB returns "movie not found", return null instead of throwing
            return null;
        }

        Movie movie = parseMovie(json);
        return movie;
    }
}