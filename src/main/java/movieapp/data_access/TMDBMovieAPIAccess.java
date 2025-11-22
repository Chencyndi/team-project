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
 * TMDB-based MovieDataSource implementation.
 * Frameworks & Drivers layer.
 */
public class TMDBMovieAPIAccess implements MovieDataSource {

    private static final Dotenv dotenv = Dotenv.configure()
            .directory("./")
            .filename("api_key.env")
            .ignoreIfMissing()
            .load();

    private final OkHttpClient client = new OkHttpClient();

    private static final int SUCCESS_CODE = 200;

    private static final String API_KEY = dotenv.get("API_KEY");
    private static final String BASE_URL = dotenv.get("BASE_URL");
    private static final String IMAGE_BASE_URL = dotenv.get("IMAGE_BASE_URL");

    private static final String STATUS_CODE_LABEL = "status_code";

    private JSONObject makeApiCall(String endpoint) throws Exception {
        Request request = new Request.Builder()
                .url(endpoint)
                .get()
                .addHeader("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("HTTP error " + response.code());
            }

            ResponseBody body = response.body();
            if (body == null) {
                throw new IOException("Empty response body");
            }

            JSONObject json = new JSONObject(body.string());
            if (json.optInt(STATUS_CODE_LABEL, SUCCESS_CODE) != SUCCESS_CODE) {
                throw new IOException("API-level error status: " + json.optInt(STATUS_CODE_LABEL));
            }

            return json;
        } catch (IOException e) {
            throw new Exception("Network error during API call", e);
        }
    }

    private Movie parseMovie(JSONObject json) throws IOException {
        try {
            int id = json.optInt("id", -1);
            String title = json.optString("title", "");
            String overview = json.optString("overview", "");
            String releaseDate = json.optString("release_date", "");
            String posterPath = json.optString("poster_path", "");
            double voteAverage = json.optDouble("vote_average", 0.0);
            double popularity = json.optDouble("popularity", 0.0);
            int voteCount = json.optInt("vote_count", 0);

            List<Comment> comments = new ArrayList<>();

            if (id == -1 || title.isEmpty()) {
                return null;
            }

            String fullPosterUrl = (posterPath != null && !posterPath.isEmpty())
                    ? IMAGE_BASE_URL + posterPath
                    : null;

            // Adjust as needed
            return new Movie(id, title, overview, releaseDate,
                    fullPosterUrl, voteAverage, popularity, voteCount, comments);

        } catch (Exception e) {
            throw new IOException("Error parsing movie: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Movie> fetchPopularMovies(int count) throws Exception {
        List<Movie> movies = new ArrayList<>();
        int page = 1;

        while (movies.size() < count && page <= 10) {
            String endpoint = BASE_URL + "/movie/popular?api_key=" + API_KEY + "&page=" + page;
            JSONObject response = makeApiCall(endpoint);

            JSONArray results = response.optJSONArray("results");
            if (results == null || results.isEmpty()) break;

            for (int i = 0; i < results.length() && movies.size() < count; i++) {
                JSONObject movieJson = results.getJSONObject(i);
                Movie movie = parseMovie(movieJson);
                if (movie != null) {
                    movies.add(movie);
                }
            }
            page++;
        }
        return movies;
    }

    @Override
    public List<Movie> fetchRecentMovies(int count) throws Exception {
        List<Movie> movies = new ArrayList<>();
        int page = 1;

        while (movies.size() < count * 2 && page <= 8) {
            String endpoint = BASE_URL + "/movie/now_playing?api_key=" + API_KEY + "&page=" + page;
            JSONObject response = makeApiCall(endpoint);

            JSONArray results = response.optJSONArray("results");
            if (results == null || results.isEmpty()) break;

            for (int i = 0; i < results.length(); i++) {
                JSONObject movieJson = results.getJSONObject(i);
                Movie movie = parseMovie(movieJson);
                if (movie != null && movie.getReleaseDate() != null && !movie.getReleaseDate().isEmpty()) {
                    movies.add(movie);
                }
            }
            page++;
        }

        movies.sort((m1, m2) -> m2.getReleaseDate().compareTo(m1.getReleaseDate()));

        return movies.subList(0, Math.min(count, movies.size()));
    }
}