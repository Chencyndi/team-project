package movieapp.data_access;

import movieapp.entity.Movie;
import movieapp.entity.User;
import movieapp.entity.Watchlist;
import movieapp.use_case.common.UserDataAccessInterface;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class UserDataAccessObject implements UserDataAccessInterface {
    final Path FILE_PATH = Paths.get("src/main/java/movieapp/data/users.json");

    private JSONArray readJSONFile() {
        try {
            String jsonString = Files.readString(FILE_PATH);
            return new JSONArray(jsonString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeJSONFile(JSONArray jsonArray) {
        try {
            Files.writeString(FILE_PATH, jsonArray.toString(4));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existsByUsername(String username) {
        JSONArray jsonArray = readJSONFile();
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).getString("username").equals(username)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addUser(User user) {
        JSONArray jsonArray = readJSONFile();
        JSONObject newUser = new JSONObject();
        newUser.put("username", user.getUsername());
        newUser.put("password", user.getPassword());
        newUser.put("watchlist", new JSONArray());
        newUser.put("ratings", new JSONObject());
        jsonArray.put(newUser);
        writeJSONFile(jsonArray);
    }

    @Override
    public User findByUsername(String username) {
        JSONArray jsonArray = readJSONFile();
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).getString("username").equals(username)) {
                JSONObject newUser = jsonArray.getJSONObject(i);
                String password = newUser.getString("password");
                return new User(username, password);
            }
        }
        return null;
    }

    @Override
    public Watchlist getWatchlist(String username) {
        JSONArray jsonArray = readJSONFile();
        TMDBMovieAPIAccess tmdbMovieAPIAccess = new TMDBMovieAPIAccess();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject newUser = jsonArray.getJSONObject(i);
            if (newUser.getString("username").equals(username)) {
                JSONArray watchlistID = newUser.getJSONArray("watchlist");
                List<Movie> movies = new ArrayList<>();
                for (int j = 0; j < watchlistID.length(); j++) {
                    int id = watchlistID.getInt(j);
                    try {
                        Movie movie = tmdbMovieAPIAccess.fetchRecentMovies(id).get(j);
                        if (movie != null) {
                            movies.add(movie);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                return new Watchlist(movies);
            }
        }
        return new Watchlist();
    }

    @Override
    public void addWatchlist(Integer movieID, String username) {
        JSONArray jsonArray = readJSONFile();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject user = jsonArray.getJSONObject(i);
            if (user.getString("username").equals(username)) {
                JSONArray list = user.getJSONArray("watchlist");
                if (!list.toList().contains(movieID)) {
                    list.put(movieID);
                }
                writeJSONFile(jsonArray);
                return;
            }
        }
    }

    @Override
    public void removeWatchlist(Integer movieID, String username) {
        JSONArray jsonArray = readJSONFile();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject user = jsonArray.getJSONObject(i);
            if (user.getString("username").equals(username)) {
                JSONArray watchlist = user.getJSONArray("watchlist");
                for (int j = 0; j < watchlist.length(); j++) {
                    if (watchlist.getInt(j) == movieID) {
                        watchlist.remove(j);
                        break;
                    }
                }
                writeJSONFile(jsonArray);
                return;
            }
        }
    }

    @Override
    public Integer getMovieRating(String username, Integer movieID) {
        JSONArray jsonArray = readJSONFile();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject user = jsonArray.getJSONObject(i);
            if (user.getString("username").equals(username)) {
                JSONObject ratings = user.getJSONObject("ratings");
                if (ratings.has(movieID.toString())) {
                    return ratings.getInt(movieID.toString());
                }
            }
        }
        return null;
    }

    @Override
    public void addRating(Integer movieID, String username, Integer rating) {
        JSONArray jsonArray = readJSONFile();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject user = jsonArray.getJSONObject(i);
            if (user.getString("username").equals(username)) {
                JSONObject ratings = user.getJSONObject("ratings");
                ratings.put(movieID.toString(), rating);
                writeJSONFile(jsonArray);
                return;
            }
        }
    }

    @Override
    public void removeRating(Integer movieID, String username) {
        JSONArray jsonArray = readJSONFile();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject user = jsonArray.getJSONObject(i);

            if (user.getString("username").equals(username)) {
                JSONObject ratings = user.getJSONObject("ratings");
                ratings.remove(movieID.toString());
                writeJSONFile(jsonArray);
                return;
            }
        }
    }
}
