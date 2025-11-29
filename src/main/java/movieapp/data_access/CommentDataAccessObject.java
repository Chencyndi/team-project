package movieapp.data_access;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import movieapp.entity.Comment;
import movieapp.entity.User;
import movieapp.use_case.comment.CommentDataAccessInterface;
import movieapp.use_case.common.UserDataAccessInterface;

/**
 * Data Access Object for managing comment data persistence.
 * Handles reading and writing comments to JSON file storage.
 */
public class CommentDataAccessObject implements CommentDataAccessInterface {
    private static final Path FILE_PATH = Paths.get("src/main/java/movieapp/data/comments.json");
    private static final int JSON_INDENT = 4;
    private static final String MOVIE_ID_KEY = "movieID";
    private static final String COMMENT_ID_KEY = "commentID";
    private static final String USERNAME_KEY = "username";
    private static final String TEXT_KEY = "text";
    private static final String REPLIES_KEY = "replies";
    private final UserDataAccessInterface userDataAccess;

    /**
     * Constructs a CommentDataAccessObject with the specified user data access interface.
     *
     * @param userDataAccess the user data access interface to use for retrieving user information
     */
    public CommentDataAccessObject(UserDataAccessInterface userDataAccess) {
        this.userDataAccess = userDataAccess;
    }

    private JSONArray readJSONFile() {
        try {
            final String jsonString = Files.readString(FILE_PATH);
            return new JSONArray(jsonString);
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void writeJSONFile(JSONArray jsonArray) {
        try {
            Files.writeString(FILE_PATH, jsonArray.toString(JSON_INDENT));
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Comment> getComments(Integer movieID) {
        final JSONArray jsonArray = readJSONFile();
        final Map<String, Comment> map = new HashMap<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            final JSONObject obj = jsonArray.getJSONObject(i);

            if (obj.getInt(MOVIE_ID_KEY) == movieID) {
                final String id = obj.get(COMMENT_ID_KEY).toString();
                final String username = obj.getString(USERNAME_KEY);
                final User user = userDataAccess.findByUsername(username);

                if (user != null) {
                    map.put(id, new Comment(id, user, new ArrayList<>(), obj.getString(TEXT_KEY), movieID));
                }
            }
        }

        extracted(movieID, jsonArray, map);

        final List<String> allReplyIds = getStrings(movieID, jsonArray);

        final List<Comment> topLevel = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            final JSONObject obj = jsonArray.getJSONObject(i);
            if (obj.getInt(MOVIE_ID_KEY) == movieID) {
                final String commentId = obj.getString(COMMENT_ID_KEY);
                if (!allReplyIds.contains(commentId)) {
                    final Comment comment = map.get(commentId);
                    if (comment != null) {
                        topLevel.add(comment);
                    }
                }
            }
        }

        return topLevel;
    }

    private static void extracted(Integer movieID, JSONArray jsonArray, Map<String, Comment> map) {
        for (int i = 0; i < jsonArray.length(); i++) {
            final JSONObject obj = jsonArray.getJSONObject(i);

            if (obj.getInt(MOVIE_ID_KEY) != movieID) {
                continue;
            }

            final Comment parent = map.get(obj.getString(COMMENT_ID_KEY));
            if (parent == null) {
                continue;
            }

            final JSONArray replyIds = obj.getJSONArray(REPLIES_KEY);

            for (int r = 0; r < replyIds.length(); r++) {
                final Comment child = map.get(replyIds.getString(r));
                if (child != null) {
                    parent.addReply(child);
                }
            }
        }
    }

    private static List<String> getStrings(Integer movieID, JSONArray jsonArray) {
        final List<String> allReplyIds = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            final JSONObject obj = jsonArray.getJSONObject(i);
            if (obj.getInt(MOVIE_ID_KEY) == movieID) {
                final JSONArray replyIds = obj.getJSONArray(REPLIES_KEY);
                for (int r = 0; r < replyIds.length(); r++) {
                    allReplyIds.add(replyIds.getString(r));
                }
            }
        }
        return allReplyIds;
    }

    @Override
    public void addComment(Comment comment) {
        final JSONArray jsonArray = readJSONFile();

        final JSONObject newObj = new JSONObject();
        newObj.put(COMMENT_ID_KEY, comment.getCommentID());
        newObj.put(USERNAME_KEY, comment.getCommenter().getUsername());
        newObj.put(MOVIE_ID_KEY, comment.getMovieID());
        newObj.put(TEXT_KEY, comment.getText());
        newObj.put(REPLIES_KEY, new JSONArray());

        jsonArray.put(newObj);
        writeJSONFile(jsonArray);
    }

    @Override
    public void addReply(Comment reply, String parentCommentID) {
        final JSONArray jsonArray = readJSONFile();
        final JSONObject replyObj = new JSONObject();
        replyObj.put(COMMENT_ID_KEY, reply.getCommentID());
        replyObj.put(USERNAME_KEY, reply.getCommenter().getUsername());
        replyObj.put(MOVIE_ID_KEY, reply.getMovieID());
        replyObj.put(TEXT_KEY, reply.getText());
        replyObj.put(REPLIES_KEY, new JSONArray());
        jsonArray.put(replyObj);
        for (int i = 0; i < jsonArray.length(); i++) {
            final JSONObject obj = jsonArray.getJSONObject(i);
            if (obj.getString(COMMENT_ID_KEY).equals(parentCommentID)) {
                final JSONArray replies = obj.getJSONArray(REPLIES_KEY);
                replies.put(reply.getCommentID());
                break;
            }
        }

        writeJSONFile(jsonArray);
    }
}
