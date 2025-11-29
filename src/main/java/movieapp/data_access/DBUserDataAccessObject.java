// DBUserDataAccessObject.java
package movieapp.data_access;

import movieapp.entity.User;
import movieapp.interface_adapter.login.AccountRepository;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Optional;

/**
 * The DAO for user data using HTTP API calls.
 */
public class DBUserDataAccessObject implements AccountRepository {
    private static final int SUCCESS_CODE = 200;
    private static final String CONTENT_TYPE_LABEL = "Content-Type";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String STATUS_CODE_LABEL = "status_code";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String MESSAGE = "message";
    
    private String currentUsername;
    private final String baseUrl;

    public DBUserDataAccessObject(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        final OkHttpClient client = new OkHttpClient().newBuilder().build();
        final Request request = new Request.Builder()
                .url(String.format("%s/user?username=%s", baseUrl, username))
                .addHeader("Content-Type", CONTENT_TYPE_JSON)
                .build();
        try {
            final Response response = client.newCall(request).execute();

            final JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getInt(STATUS_CODE_LABEL) == SUCCESS_CODE) {
                final JSONObject userJSONObject = responseBody.getJSONObject("user");
                final String name = userJSONObject.getString(USERNAME);
                final String password = userJSONObject.getString(PASSWORD);

                return Optional.of(new User(name, password));
            }
            else {
                return Optional.empty();
            }
        }
        catch (IOException | JSONException ex) {
            throw new RuntimeException("Error fetching user: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void save(User user) {
        final OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        // POST METHOD
        final MediaType mediaType = MediaType.parse(CONTENT_TYPE_JSON);
        final JSONObject requestBody = new JSONObject();
        requestBody.put(USERNAME, user.getUsername());
        requestBody.put(PASSWORD, user.getPassword());
        final RequestBody body = RequestBody.create(requestBody.toString(), mediaType);
        final Request request = new Request.Builder()
                .url(baseUrl + "/user")
                .method("POST", body)
                .addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_JSON)
                .build();
        try {
            final Response response = client.newCall(request).execute();

            final JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getInt(STATUS_CODE_LABEL) == SUCCESS_CODE) {
                // success!
            }
            else {
                throw new RuntimeException(responseBody.getString(MESSAGE));
            }
        }
        catch (IOException | JSONException ex) {
            throw new RuntimeException("Error saving user: " + ex.getMessage(), ex);
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        final OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        final Request request = new Request.Builder()
                .url(String.format("%s/checkIfUserExists?username=%s", baseUrl, username))
                .addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_JSON)
                .build();
        try {
            final Response response = client.newCall(request).execute();

            final JSONObject responseBody = new JSONObject(response.body().string());

            return responseBody.getInt(STATUS_CODE_LABEL) == SUCCESS_CODE;
        }
        catch (IOException | JSONException ex) {
            throw new RuntimeException("Error checking user existence: " + ex.getMessage(), ex);
        }
    }

    // Additional methods for current user management
    public void setCurrentUsername(String name) {
        currentUsername = name;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    // Change password method if needed
    public void changePassword(User user) {
        final OkHttpClient client = new OkHttpClient().newBuilder()
                                        .build();

        // PUT METHOD for updating
        final MediaType mediaType = MediaType.parse(CONTENT_TYPE_JSON);
        final JSONObject requestBody = new JSONObject();
        requestBody.put(USERNAME, user.getUsername());
        requestBody.put(PASSWORD, user.getPassword());
        final RequestBody body = RequestBody.create(requestBody.toString(), mediaType);
        final Request request = new Request.Builder()
                                    .url(baseUrl + "/user")
                                    .method("PUT", body)
                                    .addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_JSON)
                                    .build();
        try {
            final Response response = client.newCall(request).execute();

            final JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getInt(STATUS_CODE_LABEL) == SUCCESS_CODE) {
                // success!
            }
            else {
                throw new RuntimeException(responseBody.getString(MESSAGE));
            }
        }
        catch (IOException | JSONException ex) {
            throw new RuntimeException("Error changing password: " + ex.getMessage(), ex);
        }
    }
}